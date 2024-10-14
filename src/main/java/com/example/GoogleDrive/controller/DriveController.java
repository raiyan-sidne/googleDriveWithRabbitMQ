package com.example.GoogleDrive.controller;

import com.example.GoogleDrive.dto.FileObject;
import com.example.GoogleDrive.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Component
public class DriveController {

    @Autowired
    private DriveService driveService;

    public FileObject uploadFile(FileObject fileObject) throws GeneralSecurityException, IOException {
        fileObject.setUrls(driveService.uploadFiles(fileObject.getFolderName(), fileObject.getFiles()));

        return fileObject;
    }

    public FileObject getSingleFileDownloadableLink(FileObject fileObject) throws GeneralSecurityException, IOException {
        String url = driveService.getDownloadableLink(fileObject.getFiles().get(0).getName(), fileObject.getFolderName());
        fileObject.setUrls(new ArrayList<String>());
        fileObject.getUrls().add(url);

        return fileObject;
    }

    public FileObject getAllFilesDownloadableLinkByFolderName(FileObject fileObject) throws GeneralSecurityException, IOException {
        fileObject.setUrls(driveService.getAllFilesDownloadableLinkByFolderName(fileObject.getFolderName()));

        return fileObject;
    }
}