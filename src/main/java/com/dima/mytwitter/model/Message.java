package com.dima.mytwitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Size(min = 1, max = 140)
    private String text;

    @OneToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    private LocalDateTime createdDateTime;

    public Message(@Size(min = 1, max = 140) String text) {
        this.text = text;
    }
}
