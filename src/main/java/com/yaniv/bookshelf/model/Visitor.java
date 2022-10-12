package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Visitor {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String userName;

    @OneToMany(mappedBy = "id" ,fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Invoice> invoices;

    @NotBlank
    private String email;

    private String password;
}
