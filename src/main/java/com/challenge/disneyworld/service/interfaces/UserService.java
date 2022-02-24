package com.challenge.disneyworld.service.interfaces;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<?> createUser(ModelRegistrationUser user);
    public ResponseEntity<?> createAdmin(String email);
    public ResponseEntity<?> deleteUser(Long id);
    public ResponseEntity<?> getUser();
    public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws StreamWriteException, DatabindException, IOException;
}
