package com.challenge.disneyworld.service;

import java.util.Optional;

import com.challenge.disneyworld.entity.Role;
import com.challenge.disneyworld.repository.RoleRepository;
import com.challenge.disneyworld.service.interfaces.RoleService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public void createRole(String role) {
        Optional<Role> roleRequest = roleRepository.findByName(role);

        if(!roleRequest.isPresent()){ 
            Role newRole = new Role(); 
            newRole.setName(role); 
            roleRepository.save(newRole);
        }
    }

    
}
