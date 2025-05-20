package com.jovantomovic.pulsdana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegWithCredRequest {
    private String username;
    private String email;
    private String password;
}
