package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.mapper.CartMapper;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.OrderedBook;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.BookType;
import com.yaniv.bookshelf.service.BookService;
import com.yaniv.bookshelf.service.InvoiceService;
import com.yaniv.bookshelf.service.VisitorService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    private final BookService bookService;
    private final InvoiceService invoiceService;
    private final CartMapper cartMapper;
    private final VisitorService visitorService;

    @Autowired
    public InvoiceController(BookService bookService, InvoiceService invoiceService, VisitorService visitorService) {
        this.bookService = bookService;
        this.invoiceService = invoiceService;
        this.visitorService = visitorService;
        this.cartMapper = new CartMapper();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add")
    public ResponseEntity addProduct(@CookieValue(value = "invoice", required = false) String invoice, String isbn) throws IOException {
        LOGGER.info("book: {}", isbn);
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        if (cart.containsKey(isbn)) {
            cart.put(isbn, cart.get(isbn) + 1);
        } else {
            cart.put(isbn, 1);
        }
        LOGGER.info("invoice: {}", cart);
        var cookie = ResponseCookie.from("invoice", URLEncoder.encode(cartMapper.toJson(cart)))
                .path("/cart").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add-electronic")
    public ResponseEntity addElectronic(@CookieValue(value = "elversion", required = false) String invoice, String isbn) throws IOException {
        LOGGER.info("book electronic: {}", isbn);
        Set<String> cart = cartMapper.toSet(invoice);
        cart.add(isbn);
        LOGGER.info("invoice: {}", cart);
        var cookie = ResponseCookie.from("elversion", URLEncoder.encode(cartMapper.setToJson(cart)))
                .path("/cart").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping
    @SneakyThrows
    public ModelAndView getInvoice(@CookieValue(value = "invoice", defaultValue = "null") String invoice,
                                   @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {
        ModelAndView modelAndView = new ModelAndView("cart");
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);
        LOGGER.info("Cart: {}", invoice);
        if (cart != null) {
            List<OrderedBook> books = new ArrayList<>(cart.entrySet().stream().map(entry ->
                    new OrderedBook("id", bookService.findById(entry.getKey()).orElse(new Book()),
                            entry.getValue(), 0, BookType.PAPER)).toList());
            if (cartElect != null) {
                for (String id : cartElect) {
                    books.add(new OrderedBook("id", bookService.findById(id).orElse(new Book()),
                            1, 0, BookType.ELECTRONIC));
                }
            }
            modelAndView.addObject("books", books);
        } else {
            modelAndView.addObject("books", new LinkedList<OrderedBook>());
        }
        return modelAndView;
    }

    @DeleteMapping("/delete-electronic")
    @SneakyThrows
    public ResponseEntity deleteElectronic(@RequestParam String isbn,
                                           @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {
        LOGGER.info("On delete electronic {}", isbn);
        Set<String> cart = cartMapper.toSet(elVersion);
        LOGGER.info("Invoice {}", cart);
        cart.remove(isbn);
        ResponseCookie cookie = ResponseCookie.from("elversion", URLEncoder.encode(cartMapper.setToJson(cart)))
                .path("/cart").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @DeleteMapping
    @SneakyThrows
    public ResponseEntity delete(@RequestParam String isbn, @CookieValue(value = "invoice", defaultValue = "null") String invoice) {
        LOGGER.info("On delete {}", isbn);
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        LOGGER.info("Invoice {}", cart);
        cart.remove(isbn);
        ResponseCookie cookie = ResponseCookie.from("invoice", URLEncoder.encode(cartMapper.toJson(cart)))
                .path("/cart").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PutMapping
    @SneakyThrows
    public ResponseEntity update(@RequestParam String isbn, @RequestParam int count, @CookieValue(value = "invoice", defaultValue = "null") String invoice) {
        LOGGER.info("On update {}-{}", isbn, count);
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        cart.put(isbn, count);
        ResponseCookie cookie = ResponseCookie.from("invoice", URLEncoder.encode(cartMapper.toJson(cart)))
                .path("/cart").build();
        LOGGER.info("cart now-{}", cart);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity createInvoice(@CookieValue(value = "invoice", defaultValue = "null") String invoice,
                                        @CookieValue(value = "elversion", defaultValue = "null") String elVersion,
                                        Principal principal) {
        LOGGER.info("Create invoice");
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);

        LOGGER.info("Create paper");
        Set<OrderedBook> books = cart.entrySet().stream().map(entry ->
                        new OrderedBook(null, bookService.findById(entry.getKey()).orElse(new Book()),
                                entry.getValue(), bookService.findById(entry.getKey()).orElse(new Book()).getPrice(),
                                BookType.PAPER))
                .collect(Collectors.toSet());
        if (cartElect != null) {
            LOGGER.info("Create electronic");
            for (String id : cartElect) {
                books.add(new OrderedBook(null, bookService.findById(id).orElse(new Book()),
                        1, bookService.findById(id).orElse(new Book()).getPrice(), BookType.ELECTRONIC));
            }
        }
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());

        Invoice inv = new Invoice();
        inv.setBooksInOrder(books);
        inv.setBuyer(visitor);

        invoiceService.save(inv);
        ResponseCookie cookie = ResponseCookie.from("invoice",
                        URLEncoder.encode(cartMapper.toJson(new HashMap<>())))
                .path("/cart").maxAge(0).build();
        ResponseCookie cookieElectronic = ResponseCookie.from("elversion",
                        URLEncoder.encode(cartMapper.setToJson(new HashSet<>())))
                .path("/cart").maxAge(0).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString(), cookieElectronic.toString())
                .build();
    }

    @GetMapping("/create")
    public ModelAndView createInvoice(@CookieValue(value = "invoice", defaultValue = "null") String invoice,
                                      @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);
        ModelAndView model = new ModelAndView("cart_create");
        Set<OrderedBook> books = cart.entrySet().stream().map(entry ->
                        new OrderedBook(null, bookService.findById(entry.getKey()).orElse(new Book()),
                                entry.getValue(), bookService.findById(entry.getKey()).orElse(new Book()).getPrice(),
                                BookType.PAPER))
                .collect(Collectors.toSet());
        if (cartElect != null) {
            for (String id : cartElect) {
                books.add(new OrderedBook(null, bookService.findById(id).orElse(new Book()),
                        1, bookService.findById(id).orElse(new Book()).getPrice(), BookType.ELECTRONIC));
            }
        }
        model.addObject("books", books);
        model.addObject("total", books.stream()
                .mapToDouble(orderedBook -> orderedBook.getPrice() * orderedBook.getQuantity()).sum());
        return model;
    }

    @GetMapping("/all")
    public ModelAndView all(Principal principal) {
        List<Invoice> invoices = invoiceService.getAllByEmail(principal.getName());
        ModelAndView model = new ModelAndView("fragment/all_invoices");
        model.addObject("invoices", invoices);
        return model;
    }

    @GetMapping("/by-id")
    public ModelAndView getById(@RequestParam String id) {
        Optional<Invoice> optionalInvoice = invoiceService.findById(id);
        Invoice invoice = optionalInvoice.orElse(new Invoice());
        ModelAndView modelAndView = new ModelAndView("fragment/invoice");
        modelAndView.addObject("books", invoice.getBooksInOrder());
        modelAndView.addObject("total", invoice.getBooksInOrder().stream()
                .mapToDouble(orderedBook -> orderedBook.getPrice() * orderedBook.getQuantity()).sum());
        return modelAndView;
    }
}
