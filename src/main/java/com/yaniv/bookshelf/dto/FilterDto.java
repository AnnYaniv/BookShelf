package com.yaniv.bookshelf.dto;

import com.yaniv.bookshelf.dto.enums.Sort;
import com.yaniv.bookshelf.model.enums.Genre;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {
    Double min;
    Double max;
    Sort sort;
    List<Genre> genre;

    Integer page;

    @Override
    public String toString() {
        return "FilterDto{" +
                "min=" + min +
                ", max=" + max +
                ", sort=" + sort +
                ", genre=" + genre +
                ", page=" + page +
                '}';
    }
}
