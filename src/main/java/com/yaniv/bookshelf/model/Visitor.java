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
import java.io.Serializable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Visitor implements Serializable {
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

    @PrePersist
    public void prePersist() {
        if(StringUtils.isBlank(userName)){
            userName = email.split("@")[0];
        }
    }
}
