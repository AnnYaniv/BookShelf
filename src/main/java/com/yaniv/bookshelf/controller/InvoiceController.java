package com.yaniv.bookshelf.controller;

import com.yaniv.bookshelf.dto.OrderedBookDto;
import com.yaniv.bookshelf.mapper.CartMapper;
import com.yaniv.bookshelf.mapper.OrderedBookMapper;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.OrderedBook;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.OrderStatus;
import com.yaniv.bookshelf.service.InvoiceService;
import com.yaniv.bookshelf.service.VisitorService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger("controller-log");
    private final InvoiceService invoiceService;
    private final OrderedBookMapper orderedBookMapper;
    private final VisitorService visitorService;
    private final CartMapper cartMapper;

    @Autowired
    public InvoiceController(OrderedBookMapper orderedBookMapper, InvoiceService invoiceService, VisitorService visitorService) {
        this.orderedBookMapper = orderedBookMapper;
        this.invoiceService = invoiceService;
        this.visitorService = visitorService;
        this.cartMapper = new CartMapper();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add")
    public ResponseEntity addProduct(@CookieValue(value = "invoice", required = false) String invoice, String isbn) throws UnsupportedEncodingException {
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        if (cart.containsKey(isbn)) {
            cart.put(isbn, cart.get(isbn) + 1);
        } else {
            cart.put(isbn, 1);
        }
        LOGGER.debug("/add invoice: {}", cart);
        var cookie = ResponseCookie.from("invoice",
                        URLEncoder.encode(cartMapper.toJson(cart),"UTF-8"))
                .path("/").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add-electronic")
    public ResponseEntity addElectronic(@CookieValue(value = "elversion", required = false) String invoice, String isbn) throws UnsupportedEncodingException {
        Set<String> cart = cartMapper.toSet(invoice);
        cart.add(isbn);
        var cookie = ResponseCookie.from("elversion", URLEncoder.encode(cartMapper.setToJson(cart),"UTF-8"))
                .path("/").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping
    @SneakyThrows
    public ModelAndView getInvoice(@CookieValue(value = "invoice", defaultValue = "null") String invoice,
                                   @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {
        LOGGER.debug("/ paper books: {} , electronic books {}", invoice, elVersion);
        ModelAndView modelAndView = new ModelAndView("cart");
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);
        Set<OrderedBook> orderedBooks = orderedBookMapper.merge(cart, cartElect);
        Set<OrderedBookDto> books = orderedBooks.stream()
                .map(orderedBookMapper::toDto).collect(Collectors.toSet());
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @DeleteMapping("/delete-electronic")
    @SneakyThrows
    public ResponseEntity deleteElectronic(@RequestParam String isbn,
                                           @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {

        Set<String> cart = cartMapper.toSet(elVersion);
        LOGGER.debug("/delete-electronic delete {}", isbn);
        LOGGER.debug("/delete-electronic electronic books {}", cart);
        cart.remove(isbn);
        ResponseCookie cookie = ResponseCookie.from("elversion", URLEncoder.encode(cartMapper.setToJson(cart), "UTF-8"))
                .path("/").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @DeleteMapping
    @SneakyThrows
    public ResponseEntity delete(@RequestParam String isbn,
                                 @CookieValue(value = "invoice", defaultValue = "null") String invoice) {
        LOGGER.debug("/ [delete] isbn {}", isbn);
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        LOGGER.debug("/ [delete] paper books {}", cart);
        cart.remove(isbn);
        ResponseCookie cookie = ResponseCookie.from("invoice", URLEncoder.encode(cartMapper.toJson(cart), "UTF-8"))
                .path("/").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PutMapping
    @SneakyThrows
    public ResponseEntity update(@RequestParam String isbn,
                                 @RequestParam int count,
                                 @CookieValue(value = "invoice", defaultValue = "null") String invoice) {
        LOGGER.debug("/ [put] book {}-{}", isbn, count);
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        cart.put(isbn, count);
        ResponseCookie cookie = ResponseCookie.from("invoice", URLEncoder.encode(cartMapper.toJson(cart), "UTF-8"))
                .path("/").build();
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
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);
        Set<OrderedBook> books = orderedBookMapper.merge(cart, cartElect);
        Visitor visitor = visitorService.findByEmail(principal.getName()).orElse(new Visitor());

        Invoice inv = new Invoice();
        inv.setBooksInOrder(books);
        inv.setBuyer(visitor);
        invoiceService.save(inv);
        ResponseCookie cookie = ResponseCookie.from("invoice", "%7B+%7D")
                .path("/").build();
        ResponseCookie cookieElectronic = ResponseCookie.from("elversion", "%5B+%5D")
                .path("/").build();
        ResponseCookie deleteCookie = ResponseCookie.from("delete", URLEncoder.encode("delete", "UTF-8")).path("/").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString(), cookieElectronic.toString(), deleteCookie.toString())
                .build();
    }

    @GetMapping("/create")
    public ModelAndView createInvoice(@CookieValue(value = "invoice", defaultValue = "null") String invoice,
                                      @CookieValue(value = "elversion", defaultValue = "null") String elVersion) {
        Map<String, Integer> cart = cartMapper.toMap(invoice);
        Set<String> cartElect = cartMapper.toSet(elVersion);
        ModelAndView model = new ModelAndView("cart_create");
        Set<OrderedBook> books = orderedBookMapper.merge(cart, cartElect);
        model.addObject("books", books);
        model.addObject("total", books.stream()
                .mapToDouble(orderedBook -> orderedBook.getPrice() * orderedBook.getQuantity()).sum());
        return model;
    }

    @GetMapping("/all")
    public ModelAndView all(Principal principal, @RequestParam int page) {
        Page<Invoice> invoicePage = invoiceService.getAllByEmail(principal.getName(), page);
        if ((invoicePage.getTotalPages() - 1 < page) && (invoicePage.getTotalPages() != 0)) {
            page = invoicePage.getTotalPages() - 1;
            invoicePage = invoiceService.getAllByEmail(principal.getName(), page);
        }
        ModelAndView model = new ModelAndView("fragment/all_invoices");
        model.addObject("invoices", invoicePage.getContent());
        model.addObject("page", page);
        return model;
    }

    @GetMapping("/manage")
    public ModelAndView manage(@RequestParam int page, @RequestParam OrderStatus status) {
        Page<Invoice> invoicePage = invoiceService.getAllByStatus(status, page);
        if ((invoicePage.getTotalPages() - 1 < page) && (invoicePage.getTotalPages() != 0)) {
            page = invoicePage.getTotalPages() - 1;
            invoicePage = invoiceService.getAllByStatus(status, page);
        }
        ModelAndView modelAndView = new ModelAndView("fragment/filter_invoice");
        modelAndView.addObject("invoices", invoicePage.getContent());
        modelAndView.addObject("cur_status", status);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @GetMapping("/by-id")
    public ModelAndView getById(@RequestParam String id) {
        Optional<Invoice> optionalInvoice = invoiceService.findById(id);
        Invoice invoice = optionalInvoice.orElse(new Invoice());
        ModelAndView modelAndView = new ModelAndView("fragment/invoice");
        modelAndView.addObject("books", invoice.getBooksInOrder());
        modelAndView.addObject("inv", id);
        modelAndView.addObject("total", invoice.getBooksInOrder().stream()
                .mapToDouble(orderedBook -> orderedBook.getPrice() * orderedBook.getQuantity()).sum());
        return modelAndView;
    }

    @PutMapping("/manage")
    public ModelAndView updateInvoice(@RequestParam String id, @RequestParam OrderStatus status) {
        LOGGER.info("invoice:{}, new status:{}", id, status);
        Invoice invoice = invoiceService.findById(id).orElse(new Invoice());
        invoiceService.save(invoiceService.changeStatus(invoice, status));
        return getById(id);
    }
}
