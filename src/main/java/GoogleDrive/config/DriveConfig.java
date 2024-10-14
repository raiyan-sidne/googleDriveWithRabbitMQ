package GoogleDrive.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author hasin.raiyan
 * @since 10/23/23
 */
@Component
public class DriveConfig {

    private static final String APPLICATION_NAME = "GoogleDrive TH";
    private static final String CREDENTIALS_FILE_PATH = "/static/credentials.json";

    public Drive getService() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(DriveConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
                .createScoped("https://www.googleapis.com/auth/drive");

        HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);

        return new Drive.Builder(httpTransport, jsonFactory, credentialsAdapter)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}