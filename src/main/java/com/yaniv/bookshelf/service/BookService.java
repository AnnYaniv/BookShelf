package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.repository.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger("service-log");
    private static final int ITEMS_PER_PAGE = 9;
    private BookRepository bookRepository;
    private BookFilter bookFilter;
    private DriveService driveService;

    @Autowired
    public BookService(BookRepository bookRepository, BookFilter bookFilter, DriveService driveService) {
        this.driveService = driveService;
        this.bookRepository = bookRepository;
        this.bookFilter = bookFilter;
    }

    public Optional<Book> findByUserIdAndIsbn(String userId, String isbn) {
        return bookRepository.findByUserIdAndIsbn(userId, isbn);
    }

    public Optional<Book> findElectronicByUserIdAndIsbn(String userId, String isbn) {
        return bookRepository.findElectronicByUserIdAndIsbn(userId, isbn);
    }

    public boolean isBookBought(String userId, String isbn) {
        return bookRepository.findByUserIdAndIsbn(userId, isbn).isPresent();
    }

    public Page<Book> getAll(int page) {
        return bookRepository.findAll(PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public void incrementVisited(String id) {
        bookRepository.findByIsbn(id).ifPresent(book -> {
            book.incrementVisited();
            save(book);
        });
    }

    public Book save(Book book) {
        Book beforeUpd = findById(book.getIsbn()).orElseGet(Book::new);
        if (StringUtils.isBlank(book.getBookUrl())) {
            book.setBookUrl(beforeUpd.getBookUrl());
        }
        if (StringUtils.isBlank(book.getCoverUrl())) {
            book.setCoverUrl(beforeUpd.getCoverUrl());
        }
        return bookRepository.save(book);
    }


    @SneakyThrows
    public Book setBookFiles(Book book, MultipartFile cover, MultipartFile bookFile) {
        Optional<Book> current = findById(book.getIsbn());
        if (!cover.isEmpty()) {
            if (current.isPresent() && !StringUtils.isBlank(current.get().getCoverUrl())) {
                driveService.delete(current.get().getCoverUrl());
            }
            String id = driveService.upload(book.getIsbn(), cover, DriveService.Folder.COVER);
            book.setCoverUrl(id);
        }
        if (!bookFile.isEmpty()) {
            if (current.isPresent() && !StringUtils.isBlank(current.get().getBookUrl())) {
                driveService.delete(current.get().getBookUrl());
            }
            String id = driveService.upload(book.getIsbn(), bookFile, DriveService.Folder.BOOK);
            book.setBookUrl(id);
        }
        return book;
    }

    public String getBookCover(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with isbn=" + isbn + " dont exist"));
        return Base64.getEncoder().encodeToString(driveService.download(book.getCoverUrl()).toByteArray());
    }

    public byte[] getBookFile(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with isbn=" + isbn + " dont exist"));
        return driveService.download(book.getBookUrl()).toByteArray();
    }

    public String getBookFileExtension(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with isbn=" + isbn + " dont exist"));
        return driveService.getExtension(book.getBookUrl());
    }

    public Optional<Book> findById(String id) {
        return bookRepository.findByIsbn(id);
    }

    public Page<Book> findByNameLike(String name, int page) {
        return bookRepository.findByNameLike(name, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Book> findByUser(String id, int page) {
        return bookRepository.findByUser(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public Page<Book> findByUserElectronic(String id, int page) {
        return bookRepository.findByUserElectronic(id, PageRequest.of(page, ITEMS_PER_PAGE));
    }

    public List<Book> filterBook(FilterDto filterDto) {
        BookFilter filter = bookFilter.clearQuery();
        if ((filterDto.getGenre() != null) && (!filterDto.getGenre().isEmpty())) {
            filter.filterByGenres(filterDto.getGenre());
        }
        double min = 0;
        double max = Double.MAX_VALUE;
        if (filterDto.getMin() != null) {
            min = filterDto.getMin();
        }
        if (filterDto.getMax() != null) {
            max = filterDto.getMax();
        }
        filter.filterByPrice(min, max);

        switch (filterDto.getSort()) {
            case PRICE -> filter.sortByPrice();
            case POPULARITY -> filter.sortByPopularity();
            case PRICE_DESC -> filter.sortByPriceDesc();
            case POPULARITY_DESC -> filter.sortByPopularityDesc();
        }
        return filter.getResults(filterDto.getPage());
    }

    public double getAvgByBook(String isbn) {
        double result = 0;
        Double review = bookRepository.getAvgByBook(isbn);
        if (review != null) {
            result = review;
        }
        return Math.round(result);
    }
}
