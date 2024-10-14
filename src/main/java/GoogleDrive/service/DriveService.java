package GoogleDrive.service;

import com.example.GoogleDrive.config.DriveConfig;
import com.example.GoogleDrive.util.DriveUtil;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Slf4j
@Service
public class DriveService {

    @Autowired
    private DriveConfig driveConfig;

    @Autowired
    private DriveUtil driveUtil;

    public String uploadFile(MultipartFile file, String folderName, String parentFolderName) throws IOException, GeneralSecurityException {
        String parentFolderId = getFolderIdAndCreateFolderIfNotExists(parentFolderName, null);
        String folderId = getFolderIdAndCreateFolderIfNotExists(folderName, parentFolderId);

        Drive driveService = driveConfig.getService();

        return uploadFile(file, folderId, parentFolderId, driveService);
    }

    public void deleteFile(String fileId) throws IOException, GeneralSecurityException {
        Drive driveService = driveConfig.getService();

        driveService.files().delete(fileId).execute();
    }

/*
    public List<String> uploadFiles(String folderName, List<MultipartFile> files, String parentFolderName) throws IOException, GeneralSecurityException {
        String parentFolderId = getFolderIdAndCreateFolderIfNotExists(parentFolderName, null);
        String folderId = getFolderIdAndCreateFolderIfNotExists(folderName, parentFolderId);
        Drive driveService = driveConfig.getService();

        List<String> filesInfo = new ArrayList<>();
        for (MultipartFile file : files) {
            filesInfo.add(uploadFile(file, folderId, parentFolderId, driveService));
        }

        return filesInfo;
    }

    public String getDownloadableLink(String fileName, String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();
        FileList result = driveService.files().list()
                .setQ("'" + getFolderId(folderName, driveService) + "' in parents")
                .setQ("name='" + fileName + "'")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return driveUtil.getDownloadableLinkByID(result.getFiles().get(0).getId());
        }

        return null;
    }

    public List<String> getAllFilesDownloadableLinkByFolderName(String folderName) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();

        return driveService.files().list()
                .setQ("'" + getFolderId(folderName, driveService) + "' in parents")
                .setFields("files(id, trashed)")
                .execute().getFiles().stream()
                .filter(file -> Boolean.FALSE.equals(file.getTrashed()))
                .map(file -> driveUtil.getDownloadableLinkByID(file.getId()))
                .collect(Collectors.toList());
    }
 */

    private String uploadFile(MultipartFile multipartFile, String folderId, String parentFolderId, Drive driveService) throws IOException {
        String finalFileName = driveUtil.appendDateStampInFileName(multipartFile.getOriginalFilename());

        File fileMetadata = new File();
        fileMetadata.setName(finalFileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        InputStreamContent mediaContent = new InputStreamContent(multipartFile.getContentType(), new ByteArrayInputStream(multipartFile.getBytes()));

        return driveService.files()
                .create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()
                .getId();
    }

    private String getFolderIdAndCreateFolderIfNotExists(String folderName, String parentFolderId) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();

        FileList result = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }

        return createFolderAndGetFolderId(folderName, parentFolderId);
    }

    private String createFolderAndGetFolderId(String folderName, String parentFolderId) throws GeneralSecurityException, IOException {
        Drive driveService = driveConfig.getService();
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");

        if (parentFolderId != null) {
            folderMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        return driveService.files()
                .create(folderMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

/*
    private String getFolderId(String folderName, Drive driveService) throws IOException {
        FileList folderResult = driveService.files().list()
                .setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'")
                .execute();

        if (folderResult.getFiles() != null && !folderResult.getFiles().isEmpty()) {
            return folderResult.getFiles().get(0).getId();
        }

        return null;
    }
 */
}
