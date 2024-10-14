package com.example.GoogleDrive.controller;

import com.example.GoogleDrive.dto.FileObject;
import com.example.GoogleDrive.rabbitMQpublisher.RabbitMQPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author hasin.raiyan
 * @since 10/30/23
 */
@RestController
public class MessageController {

    @Autowired
    private DriveController driveController;

    @Autowired
    private RabbitMQPublisher publisher;

    public void uploadFileAndNotify(FileObject fileObject) throws GeneralSecurityException, IOException {
        publisher.publishMessageToNotify(driveController.uploadFile(fileObject));
    }

    @GetMapping
    public void testUpload() {
//        System.out.println("Inside the method. Let;'s upload");
//        FileObject fileObject = new FileObject();
//        fileObject.setFolderName("newMQ Folder");
//        fileObject.getFiles().add(new File("/usr/local/projects/therap/practice/GoogleDrive/src/main/resources/static/pic.jpg"));
//        fileObject.getFiles().add(new File("/usr/local/projects/therap/practice/GoogleDrive/src/main/resources/static/ss.png"));
//        fileObject.getFiles().add(new File("/usr/local/projects/therap/practice/GoogleDrive/src/main/resources/static/photo.jpg"));
//        fileObject.getFiles().add(new File("/usr/local/projects/therap/practice/GoogleDrive/src/main/resources/static/Git-Cheatsheet.pdf"));

//        publisher.sendingTestUploadCommand(fileObject);
    }
}
