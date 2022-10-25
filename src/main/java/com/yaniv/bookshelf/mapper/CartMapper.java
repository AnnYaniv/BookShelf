package com.yaniv.bookshelf.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Set<String> toSet(String invoice){
        Set<String> cart = new HashSet<>();
        if(!StringUtils.isBlank(invoice)){
            cart = objectMapper.readValue(URLDecoder.decode(invoice, "UTF-8"), Set.class);
        }
        return cart;
    }

    @SneakyThrows
    public String setToJson(Set<String> cart) {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, cart);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    public String toJson(Map<String, Integer> cart) {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, cart);
        return writer.getBuffer().toString();
    }
}
