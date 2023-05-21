package com.yaniv.bookshelf.service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.twelvemonkeys.imageio.plugins.jpeg.JPEGImageWriter;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

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

        service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(),
                requestInitializer).build();
    }

    @SneakyThrows
    public String upload(String filename, MultipartFile uploadFile, Folder folder) {
        File fileMetadata = new File();
        fileMetadata.setName((!StringUtils.isBlank(uploadFile.getContentType())) ? filename :
                filename + "." + FilenameUtils.getExtension(uploadFile.getOriginalFilename()));
        ByteArrayContent mediaContent = switch (folder) {
            case COVER -> {
                fileMetadata.setParents(Collections.singletonList(coverFolderId));

                yield new ByteArrayContent(uploadFile.getContentType(), compressImage(uploadFile));
            }
            case BOOK -> {
                fileMetadata.setParents(Collections.singletonList(bookFolderId))
                        .setViewersCanCopyContent(false);
                yield new ByteArrayContent(uploadFile.getContentType(), uploadFile.getBytes());
            }
        };

        File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        return file.getId();
    }

    @SneakyThrows
    public String getExtension(String id) {
        File file = service.files().get(id).execute();
        return file.getMimeType();
    }

    public boolean delete(String id) {
        try {
            service.files().delete(id).execute();
            return true;
        } catch (IOException e) {
            LOGGER.info("Failed to delete file {}", id);
            return false;
        }
    }

    @SneakyThrows
    public ByteArrayOutputStream download(String id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(id).executeMediaAndDownloadTo(outputStream);
        return outputStream;
    }

    @SneakyThrows
    private byte[] compressImage(MultipartFile mpFile) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(mpFile.getInputStream());
        JPEGImageWriter writer = (JPEGImageWriter) ImageIO.getImageWritersByFormatName("JPEG").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
        writer.write(null, new IIOImage(image, null, null), param);
        return byteArrayOutputStream.toByteArray();
    }

    public enum Folder {
        COVER, BOOK
    }
}
