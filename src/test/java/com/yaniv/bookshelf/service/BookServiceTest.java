package com.yaniv.bookshelf.service;

import com.google.api.services.drive.model.File;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.ExtBook;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class BookServiceTest {
    @Autowired
    BookService target;

    @Autowired
    DriveService drive;

    final Queue<String> bookUrl = new PriorityQueue<>();
    Book book;
    MultipartFile fb2File, pdfFile;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        pdfFile = new MockMultipartFile("test.pdf",
                "test.pdf","application/pdf",
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("static/test.pdf")
                        .readAllBytes());

        fb2File = new MockMultipartFile("test.fb2",
                "test.fb2", "",
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("static/test.fb2"));

        book = new Book();
        book.setIsbn(UUID.randomUUID().toString());
        book.setCount(23);
        book.setAnnotation("Annotation");
        book.setPrice(234.5f);
        book.setTitle("New Book");
        book.setYear(2023);
    }

    @Test
    void setBook_WithMultipleFilesAttached(){
        target.setBookFiles(book, new MultipartFile[] {fb2File, pdfFile});

        Map<String, String> extUrl = book.getBookUrls().stream()
                .peek(extBook -> bookUrl.add(extBook.getUrl()))
                .collect(Collectors.toMap(ExtBook::getExtension, ExtBook::getUrl));

        File fb2 = drive.getFileData(extUrl.get("fb2"));
        File pdf = drive.getFileData(extUrl.get("pdf"));

        Assertions.assertTrue(!StringUtils.isBlank(fb2.getName()) && !StringUtils.isBlank(pdf.getName()));
    }

    @Test
    void setBook_WithOneFileAttached(){
        target.setBookFiles(book, new MultipartFile[] {fb2File});

        Map<String, String> extUrl = book.getBookUrls().stream()
                .peek(extBook -> bookUrl.add(extBook.getUrl()))
                .collect(Collectors.toMap(ExtBook::getExtension, ExtBook::getUrl));

        File fb2 = drive.getFileData(extUrl.get("fb2"));

        Assertions.assertFalse(StringUtils.isBlank(fb2.getName()));
    }

    @Test
    void setBook_WithNoFileAttached(){
        target.setBookFiles(book, new MultipartFile[] {});

        Map<String, String> extUrl = book.getBookUrls().stream()
                .peek(extBook -> bookUrl.add(extBook.getUrl()))
                .collect(Collectors.toMap(ExtBook::getExtension, ExtBook::getUrl));

        Assertions.assertTrue(extUrl.isEmpty());
    }

    @AfterEach
    void tearDown() {
        while (!bookUrl.isEmpty()){
            drive.delete(bookUrl.remove());
        }
    }
}
