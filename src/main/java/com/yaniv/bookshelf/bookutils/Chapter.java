package com.yaniv.bookshelf.bookutils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Chapter {
    private String title;
    private List<String> paragraphs;

    public Chapter(){
        paragraphs = new ArrayList<>();
    }
}
