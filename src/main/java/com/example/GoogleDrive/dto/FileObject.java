package com.example.GoogleDrive.dto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hasin.raiyan
 * @since 10/30/23
 */
public class FileObject implements Serializable {

    String folderName;

    List<File> files;

    List<String> urls;

    public FileObject() {
        this.files = new ArrayList<File>();
        this.urls = new ArrayList<String>();
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    private String getFileNames() {
        String names = "File Names are:\n";

        for (File file : this.files) {
            names += file.getName() + "\n";
        }

        return names;
    }

    @Override
    public String toString() {
        return "Folder Name: " + folderName + "\n" +
                getFileNames() +
                "Urls:\n" + getUrls();
    }
}
