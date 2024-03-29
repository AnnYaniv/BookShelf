package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.dto.FilterDto;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.ExtBook;
import com.yaniv.bookshelf.repository.BookFilter;
import com.yaniv.bookshelf.repository.BookRepository;
import com.yaniv.bookshelf.repository.ExtBookRepository;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

    private static final int ITEMS_PER_PAGE = 9;
    private BookRepository bookRepository;
    private BookFilter bookFilter;
    private DriveService driveService;
    private ExtBookRepository extBookRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookFilter bookFilter, DriveService driveService, ExtBookRepository extBookRepository) {
        this.driveService = driveService;
        this.bookRepository = bookRepository;
        this.bookFilter = bookFilter;
        this.extBookRepository = extBookRepository;
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
        if (book.getBookUrls().isEmpty()) {
            book.setBookUrls(beforeUpd.getBookUrls());
        }
        if (StringUtils.isBlank(book.getCoverUrl())) {
            book.setCoverUrl(beforeUpd.getCoverUrl());
        }
        extBookRepository.saveAll(book.getBookUrls());
        return bookRepository.save(book);
    }


    @SneakyThrows
    public Book setBookCover(Book book, MultipartFile cover) {
        Optional<Book> current = findById(book.getIsbn());
        if (!cover.isEmpty()) {
            if (current.isPresent() && !StringUtils.isBlank(current.get().getCoverUrl())) {
                driveService.delete(current.get().getCoverUrl());
            }
            String filename = book.getIsbn() + "." + FilenameUtils.getExtension(cover.getOriginalFilename());
            String id = driveService.upload(filename, cover.getContentType(), cover.getBytes());
            book.setCoverUrl(id);
        }
        return book;
    }

    @SneakyThrows
    public boolean setBookFiles(Book book, MultipartFile[] bookFiles){
        for (MultipartFile file : bookFiles) {
            String filename = book.getIsbn() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            ExtBook extBook = new ExtBook(FilenameUtils.getExtension(file.getOriginalFilename()),
                    driveService.upload(filename, file.getContentType(), file.getBytes()));
            if (!book.getBookUrls().add(extBook)) {
                return false;
            }
        }
        return true;
    }

    public String getBookCover(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with isbn=" + isbn + " dont exist"));
        return Base64.getEncoder().encodeToString(driveService.download(book.getCoverUrl()).toByteArray());
    }

    public byte[] getBookFile(String isbn) {
        return driveService.download(getExtBook(isbn).getUrl()).toByteArray();
    }

    public byte[] getBookFileByUrl(String url) {
        return driveService.download(url).toByteArray();
    }

    public ExtBook getExtBook(String isbn) {
        ExtBook extBook = new ExtBook();
        bookRepository.findByIsbn(isbn).flatMap(book -> book.getBookUrls().stream()
                        .min((a, b) -> b.getExtension().compareToIgnoreCase(a.getExtension())))
                .ifPresent(eb -> {
                    extBook.setUrl(eb.getUrl());
                    extBook.setExtension(eb.getExtension());
                });
        return extBook;
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
