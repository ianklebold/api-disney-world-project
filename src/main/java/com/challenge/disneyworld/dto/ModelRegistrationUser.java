package com.challenge.disneyworld.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ModelRegistrationUser {
    private String username;
    private String email; 
    private String name;
    private String password;

    public ModelRegistrationUser(){}
}
