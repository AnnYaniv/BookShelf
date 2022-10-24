package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Invoice {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany
    private Set<OrderedBook> booksInOrder;

    @ManyToOne
    private Visitor buyer;

    @PrePersist
    public void prePersist() {
        if(orderedAt == null) {
            orderedAt = LocalDateTime.now();
        }
        if(status == null) {
            status = OrderStatus.CREATING;
        }
    }
}
