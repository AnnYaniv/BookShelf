package com.yaniv.bookshelf.service;

import com.yaniv.bookshelf.model.Book;
import com.yaniv.bookshelf.model.Favourite;
import com.yaniv.bookshelf.model.Visitor;
import com.yaniv.bookshelf.repository.FavouriteRepository;
import com.yaniv.bookshelf.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public FavouriteService(FavouriteRepository favouriteRepository, VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
        this.favouriteRepository = favouriteRepository;
    }

    public Favourite getFavourite(Visitor visitor) {
        if(visitorRepository.existsById(visitor.getId())){
            return favouriteRepository.findByVisitor(visitor).orElseGet(() -> {
                Favourite favourite = new Favourite();
                favourite.setVisitor(visitor);
                favouriteRepository.save(favourite);
                return favourite;
            }
            );
        } else {
            return null;
        }
    }

    public Iterable<Book> getBooks(int page, String id){
        return favouriteRepository.getBooks(PageRequest.of(page, 9), id);
    }

    public Favourite save(Favourite favourite) {
        return favouriteRepository.save(favourite);
    }
}
