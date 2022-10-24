package com.yaniv.bookshelf.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Invoice;
import com.yaniv.bookshelf.model.OrderedBook;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.model.enums.BookType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CartMapper {
    private final ObjectMapper objectMapper;
    public CartMapper() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @SneakyThrows
    public Map<String, Integer> toMap(String invoice) {
        Map<String, Integer> cart = new HashMap<>();
        if(!StringUtils.isBlank(invoice)){
            cart = objectMapper.readValue(URLDecoder.decode(invoice, "UTF-8"), Map.class);
        }
        return cart;
    }

    @SneakyThrows
    public String toJson(Map<String, Integer> cart) {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, cart);
        return writer.getBuffer().toString();
    }
}
