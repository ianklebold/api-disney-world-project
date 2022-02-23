package com.challenge.disneyworld.config;

import com.challenge.disneyworld.service.interfaces.RoleService;
import com.challenge.disneyworld.service.interfaces.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class DataLoaderRunner implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        roleService.createRole("ROLE_ADMIN");
        roleService.createRole("ROLE_USER");

        userService.createAdmin("ianstgo@gmail.com");
    }
}
