package com.yaniv.bookshelf.repository;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Favourite;
import com.yaniv.bookshelf.model.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, String> {
    Optional<Favourite> findByVisitor(Visitor visitor);

    @Query(value = "SELECT fav_b FROM Favourite as f JOIN f.books as fav_b where f.id = :favouriteId")
    Page<Book> getBooks(Pageable pageable, @Param("favouriteId") String id);
}
