package com.dima.mytwitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {@Index(name = "IDX_USERNAME", columnList = "userName")})
public class User {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String userName;

    @OneToMany
    @JsonIgnore
    private Set<User> followings = new HashSet<>();

    public User(String userName) {
        this.userName = userName;
    }
}
