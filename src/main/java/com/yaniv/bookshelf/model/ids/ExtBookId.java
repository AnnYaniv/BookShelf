package com.yaniv.bookshelf.model.ids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtBookId implements Serializable {
    private String extension;
    private String url;
}
