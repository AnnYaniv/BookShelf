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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

@Service
public class DriveService {
    private final Drive service;
    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    @Value("${drive.cover}")
    public String coverFolderId;

    @Value("${drive.book}")
    public String bookFolderId;

    @SneakyThrows
    private DriveService() {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(DriveScopes.all());
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(),
                requestInitializer).build();
    }

    @SneakyThrows
    public String upload(String filename, String contentType, byte[] fileBytes) {
        File fileMetadata = new File().setName(filename);
        MimeType type;
        try {
            type = MimeTypeUtils.parseMimeType(contentType);
        } catch (Exception ex) {
            type = MimeTypeUtils.TEXT_XML;
        }

        ByteArrayContent mediaContent;
        if("image".equals(type.getType())){
            fileMetadata.setParents(Collections.singletonList(coverFolderId));
            mediaContent = new ByteArrayContent(contentType, compressImage(fileBytes));
        } else {
            fileMetadata.setParents(Collections.singletonList(bookFolderId))
                    .setViewersCanCopyContent(false);
            mediaContent = new ByteArrayContent(contentType, fileBytes);
        }

        File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        return file.getId();
    }


    @SneakyThrows
    public String getExtension(String id) {
        File file = service.files().get(id).execute();
        return file.getMimeType();
    }

    @SneakyThrows
    public File getFileData(String id){
        return service.files().get(id).setFields("id, name, parents").execute();
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
    private byte[] compressImage(byte[] imageBytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        JPEGImageWriter writer = (JPEGImageWriter) ImageIO.getImageWritersByFormatName("JPEG").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
        writer.write(null, new IIOImage(image, null, null), param);
        return byteArrayOutputStream.toByteArray();
    }
}
