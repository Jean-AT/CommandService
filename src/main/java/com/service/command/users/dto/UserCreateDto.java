package com.service.command.users.dto;

import com.service.command.users.models.UsersRol;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreateDto {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private UsersRol rol;
}