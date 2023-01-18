package com.yaniv.bookshelf.service;

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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class DriveService {
    private final Drive service;
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    @Value("${drive.cover}")
    private String coverFolderId;
    @Value("${drive.book}")
    private String bookFolderId;

    @SneakyThrows
    private DriveService() {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(DriveScopes.all());
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        service = new Drive
                .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
                .setApplicationName("Drive samples").build();
    }

    @SneakyThrows
    public List<File> getFiles() {
        FileList files = service.files().list()
                .setFields("nextPageToken, files(id, name, parents)")
                .setPageSize(10).execute();
        for (File file : files.getFiles()) {
            LOGGER.info("Found file: {} ({})", file.getName(), file.getId());
        }
        return files.getFiles();
    }

    @SneakyThrows
    public void upload(String filename, MultipartFile uploadFile, Folder folder) {
        File fileMetadata = new File();

        fileMetadata.setName((!StringUtils.isBlank(uploadFile.getContentType())) ? filename :
                filename + "." + FilenameUtils.getExtension(uploadFile.getOriginalFilename()));
        ByteArrayContent mediaContent = null;
        if (folder == Folder.COVER) {
            fileMetadata.setParents(Collections.singletonList(coverFolderId));
            mediaContent = new ByteArrayContent(uploadFile.getContentType(),
                    compressImage(uploadFile));
        }
        if (folder == Folder.BOOK) {
            fileMetadata.setParents(Collections.singletonList(bookFolderId));
            mediaContent = new ByteArrayContent(uploadFile.getContentType(), uploadFile.getBytes());
        }
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id").execute();
        LOGGER.info("File: {} ({}) saved", file.getName(), file.getId());
    }

    @SneakyThrows
    public ByteArrayOutputStream download(String filename, Folder folder) {
        List<File> files = getFiles();
        String folderId = (folder.equals(Folder.BOOK)) ? bookFolderId : coverFolderId;
        List<File> filesMatches = files.stream()
                .filter(file -> file.getName().equals(FilenameUtils.removeExtension(filename))
                        && file.getParents() != null && file.getParents().get(0).equals(folderId)).toList();

        if (!filesMatches.isEmpty()) {
            String realFileId = filesMatches.get(0).getId();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            service.files().get(realFileId).executeMediaAndDownloadTo(outputStream);
            LOGGER.info("File {} has id {}", files.get(0).getName(), files.get(0).getId());
            return outputStream;
        } else {
            throw new IllegalArgumentException("Wrong filename=" + filename);
        }
    }


    @SneakyThrows
    private byte[] compressImage(MultipartFile mpFile) {
        float quality = 0.1f;
        String extension = FilenameUtils.getExtension(mpFile.getOriginalFilename());
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(extension).next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(quality);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(baos);
        imageWriter.setOutput(imageOutputStream);
        BufferedImage originalImage = null;
        try (InputStream inputStream = mpFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            return baos.toByteArray();
        }
        IIOImage image = new IIOImage(originalImage, null, null);
        imageWriter.write(null, image, imageWriteParam);
        imageWriter.dispose();

        return baos.toByteArray();
    }

    public enum Folder {
        COVER, BOOK
    }
}
