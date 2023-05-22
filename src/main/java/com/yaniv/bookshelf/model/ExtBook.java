package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.ids.ExtBookId;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ExtBookId.class)
public class ExtBook {
    @Id
    private String extension;

    @Id
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtBook extBook = (ExtBook) o;
        return Objects.equals(extension, extBook.extension) && Objects.equals(url, extBook.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extension, url);
    }
}
