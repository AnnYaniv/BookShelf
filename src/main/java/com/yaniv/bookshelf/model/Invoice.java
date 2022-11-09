package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.OrderStatus;
import com.yaniv.bookshelf.model.states.*;
import lombok.*;
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
    @ToString.Exclude
    private Set<OrderedBook> booksInOrder;

    @ManyToOne
    private Visitor buyer;

    @Transient
    private State state;

    @PrePersist
    public void prePersist() {
        if (orderedAt == null) {
            orderedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = OrderStatus.CREATING;
            state = new CreatingState(this);
        }
    }

    @PostLoad
    public void postLoad() {
        switch (status) {
            case COMPLETED -> state = new CompletedState(this);
            case SHIPMENT -> state = new ShipmentState(this);
            case DECLINED -> state = new DeclinedState(this);
            case CREATING -> state = new CreatingState(this);
        }
    }
}
