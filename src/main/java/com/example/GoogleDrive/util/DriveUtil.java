package com.example.GoogleDrive.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Component
public class DriveUtil {

    public String appendDateStampInFileName(String fileName) {
        return getFileNameWithoutExtension(fileName) + " (" + getFormatedDate(new Date()) + ")" + getFileExtension(fileName);
    }

    public String getFileExtension(String fileName) {
        int i = 0;
        for (i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                break;
            }
        }

        return fileName.substring(i, fileName.length());
    }

    public String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.length() - getFileExtension(fileName).length());
    }

    public String getFormatedDate(Date date) {
        return new SimpleDateFormat("MM-dd-yyyy hh:mm:ss.SSS").format(date);
    }
}
