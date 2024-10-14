package GoogleDrive.controller;

import com.example.GoogleDrive.config.DriveConfig;
import com.example.GoogleDrive.service.DriveService;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Slf4j
@RestController
public class DriveController {

    private static final String DEFAULT_PARENT_FOLDER = "Employee Documents";

    @Autowired
    private DriveService driveService;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(MultipartFile file, String folderName, String parentFolderName) throws GeneralSecurityException, IOException {
        if (Objects.isNull(parentFolderName) || parentFolderName.equals("")) {
            parentFolderName = DEFAULT_PARENT_FOLDER;
        }

        log.info("Uploading to Folder: {}/{}", parentFolderName, folderName);

        String fileId = driveService.uploadFile(file, folderName, parentFolderName);

        return ResponseEntity.ok("https://drive.google.com/uc?id=" + fileId + "&export=download");
    }

    @GetMapping(value = "/deleteFile")
    public ResponseEntity<String> deleteFile(String fileId) throws GeneralSecurityException {
        log.info("Deleting file with Id: {}", fileId);

        try {
            driveService.deleteFile(fileId);

            return ResponseEntity.ok("File deleted");
        } catch (IOException e) {
            return new ResponseEntity<>("Could not delete file", BAD_REQUEST);
        }
    }

    @GetMapping(value = "/print")
    public ResponseEntity<String> printIt(String p) throws IOException, GeneralSecurityException {
        String s = "";
        String pageToken = null;
        Drive dService = new DriveConfig().getService();

        do {
            com.google.api.services.drive.model.FileList result = dService.files().list()
                    .setFields("files(id)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                s += "\n\n\n" + file.getId();
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);


        return ResponseEntity.ok("we are printing ------>>>   "+s);
    }

    /*
    @GetMapping("/uploadFiles")
    public ResponseEntity<List<String>> uploadFiles(String folderName, List<MultipartFile> files) throws GeneralSecurityException, IOException {
        List<String> filesInfo = driveService.uploadFiles(folderName, files);

        return ResponseEntity.ok(filesInfo);
    }

    @GetMapping("/singleFileDownloadableLink")
    public ResponseEntity<String> getSingleFileDownloadableLink(String fName, String folderName) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(driveService.getDownloadableLink(fName, folderName));
    }

    @GetMapping("/allFilesDownloadableLink")
    public ResponseEntity<List<String>> getAllFilesDownloadableLinkByFolderName(String folderName) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(driveService.getAllFilesDownloadableLinkByFolderName(folderName));
    }
     */
}