package com.yaniv.bookshelf.model;

import com.yaniv.bookshelf.model.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
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

    @Column(unique = true)
    private String userName;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Invoice> invoices;

    @Column(unique = true)
    @NotBlank
    private String email;

    private String password;

    private LocalDate subscribeExp;

    @PrePersist
    public void prePersist() {
        if(StringUtils.isBlank(userName)){
            userName = email.split("@")[0];
        }
    }
}
