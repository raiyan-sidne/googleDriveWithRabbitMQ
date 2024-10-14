package com.example.GoogleDrive.service;

import com.example.GoogleDrive.config.DriveConfig;
import com.example.GoogleDrive.util.DriveUtil;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Service
public class DriveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriveService.class);

    @Autowired
    private DriveConfig driveConfig;

    @Autowired
    private DriveUtil driveUtil;

    public List<String> uploadFiles(String folderName, List<java.io.File> files) throws IOException, GeneralSecurityException {
        String folderId = getFolderIdAndCreateFolderIfNotExists(folderName);
        Drive driveService = driveConfig.getService();

        LOGGER.info(String.format("Folder name is %s", folderName));

        List<String> filesInfo = new ArrayList<>();
        for(java.io.File file: files) {
            filesInfo.add(uploadFile(file, folderId, driveService));
        }

        return filesInfo; //this is to be stored. edit here to make iw how we want it to be
    }

    public String uploadFile(java.io.File file, String folderId, Drive driveService) throws IOException {
        String finalFileName = driveUtil.appendDateStampInFileName(file.getName());

        LOGGER.info(String.format("Uploading file with the name %s",finalFileName));

        File fileMetadata = new File();
        fileMetadata.setName(finalFileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        FileContent mediaContent = new FileContent("application/octet-stream", file);

        return driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()
                .getId();
    }

    private String getFolderIdAndCreateFolderIfNotExists(String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();

        FileList result = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }

        return createFolderAndGetFolderId(folderName);
    }

    private String createFolderAndGetFolderId(String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        return driveService.files()
                .create(folderMetadata)
                .setFields("id")
                .execute()
                .getId();
    }


    public String getDownloadableLink(String fileName, String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();
        FileList result = driveService.files().list()
                .setQ("'" + getFolderId(folderName, driveService) + "' in parents") //Not needed if file names are unique
                .setQ("name='" + fileName + "'")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return "https://drive.google.com/uc?id="
                    + result.getFiles().get(0).getId()
                    + "&export=download";
        }

        return null;
    }


    private String getFolderId(String folderName, Drive driveService) throws IOException {
        FileList folderResult = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'")
                .execute();

        if (folderResult.getFiles() != null && !folderResult.getFiles().isEmpty()) {
            return folderResult.getFiles().get(0).getId();
        }

        return null;
    }

    public List<String> getAllFilesDownloadableLinkByFolderName(String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();

        return driveService.files().list()
                .setQ("'" + getFolderId(folderName, driveService) + "' in parents")
                .setFields("files(id, trashed, name)")
                .execute().getFiles().stream()
                .filter(file -> Boolean.FALSE.equals(file.getTrashed())) // Exclude files in the trash
                .map(file -> file.getName() + " id = "+file.getId())
                //.map(file -> "https://drive.google.com/uc?id=" + file.getId() + "&export=download")
                .collect(Collectors.toList());
    }
}
