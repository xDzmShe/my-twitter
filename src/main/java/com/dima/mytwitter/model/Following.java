package com.dima.mytwitter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Following {
    @Size(min = 1)
    private String userName;
}
