package com.yaniv.bookshelf.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//TODO DI on fields of folders id on gdrive
//TODO as an actual spring service
public class DriveService {
    private static DriveService instance;
    private final Drive service;
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    //@Value("${drive.cover}")
    private String coverFolderId = "1Z0Fn2rKLYJsU4PAmlxafnjztqELcmO9p";

    //@Value("${drive.book}")
    private String bookFolderId = "1vChrZnJlcodQUbwYwECYtCP5ij5LFGpc";

    @SneakyThrows
    private DriveService() {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(DriveScopes.all());
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credentials);

        service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Drive samples")
                .build();
    }

    public static DriveService getInstance() {
        if (instance == null) {
            instance = new DriveService();
        }
        return instance;
    }

    @SneakyThrows
    public List<File> getFiles() {
        FileList files = service.files().list()
                .setFields("nextPageToken, files(id, name)")
                .setPageSize(10)
                .execute();
        for (File file : files.getFiles()) {
            LOGGER.info("Found file: {} ({})", file.getName(), file.getId());
        }
        return files.getFiles();
    }

    @SneakyThrows
    public void upload(String isbn,byte[] uploadFile, Folder folder, String type){
        File fileMetadata = new File();
        fileMetadata.setName(isbn);
        if(folder == Folder.COVER) {
            fileMetadata.setParents(Collections.singletonList(coverFolderId));
        }
        if(folder == Folder.BOOK) {
            fileMetadata.setParents(Collections.singletonList(bookFolderId));
        }

        try {
            File file = service.files().create(fileMetadata, new ByteArrayContent(type,
                    uploadFile)).setFields("id").execute();
            DriveService.getInstance().getFiles();
            LOGGER.info("File: {} ({}) saved", file.getName(), file.getId());
        } catch (GoogleJsonResponseException e) {
            LOGGER.error("Unable to upload file: {}", e.getMessage());
        }

    }



    @SneakyThrows
    public ByteArrayOutputStream downloadFile(String filename) {
        List<File> files = getFiles();
        List<File> filesMatches = files.stream().filter(file -> file.getName().equals(filename)).collect(Collectors.toList());

        if(!filesMatches.isEmpty()) {
            String realFileId = filesMatches.get(0).getId();

            try {
                OutputStream outputStream = new ByteArrayOutputStream();
                service.files().get(realFileId).executeMediaAndDownloadTo(outputStream);

                LOGGER.info("File {} has id {}", files.get(0).getName(), files.get(0).getId());
                return (ByteArrayOutputStream) outputStream;
            } catch (GoogleJsonResponseException e) {
                LOGGER.error("Unable to download file: {}", e.getDetails());
                throw e;
            }
        } else
        {
            //TODO return default picture
            LOGGER.info("Download file return null");
            return null;
        }
    }

    public enum Folder {
        COVER, BOOK
    }
}
