package com.dima.mytwitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
//@NamedEntityGraph(
//        name = "graph.User.followings",
//        attributeNodes = @NamedAttributeNode("followings"))
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
