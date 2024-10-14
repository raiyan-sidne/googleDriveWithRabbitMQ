package GoogleDrive.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Component
public class DriveUtil {

    private static final String FILE_EXTENSION_REGEX = ".[^.]+$";
    private static final String FILE_ID_EXTRACTION_REGEX = "\\?id=([^&]+)";

    public String appendDateStampInFileName(String fileName) {
        return getFileNameWithoutExtension(fileName) + " (" + getFormatedDate(new Date()) + ")" + getFileExtension(fileName);
    }

    public String getFileExtension(String fileName) {
        return getMatchedRegEx(fileName, FILE_EXTENSION_REGEX);
    }

    public String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.length() - getFileExtension(fileName).length());
    }

    public String getFormatedDate(Date date) {
        return new SimpleDateFormat("MM-dd-yyyy hh:mm:ss.SSS").format(date);
    }

    public String getDownloadableLinkByID(String id) {
        return "https://drive.google.com/uc?id=" + id + "&export=download";
    }

    public String extractFileId(String url) {
        return getMatchedRegEx(url, FILE_ID_EXTRACTION_REGEX);
    }

    private String getMatchedRegEx(String input, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                return matcher.group(1);
            } else {
                throw new Exception("ID not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while Extracting id from attachment url");
        }
    }
}
