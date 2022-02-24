package com.challenge.disneyworld.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.challenge.disneyworld.dto.Tokens;
import com.challenge.disneyworld.entity.Role;
import com.challenge.disneyworld.entity.User;
import com.challenge.disneyworld.repository.RoleRepository;
import com.challenge.disneyworld.repository.UserRepository;
import com.challenge.disneyworld.service.interfaces.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createUser(ModelRegistrationUser user) {
        ResponseEntity<?> responseFieldsEmpty = 
        new ResponseEntity<>("The fields name, UserName, Password and email can't be empty",
        HttpStatus.NOT_ACCEPTABLE);
        
        ResponseEntity<?> responseEmailInvalid = 
        new ResponseEntity<>("Email invalid",
        HttpStatus.NOT_ACCEPTABLE);

        ResponseEntity<?> responsePassInvalid = 
        new ResponseEntity<>("Pass invalid, requeriments Password must contain at least one digit [0-9].\n Password must contain at least one lowercase Latin character [a-z].\n Password must contain at least one uppercase Latin character [A-Z]. \nPassword must contain at least one special character like ! @ # & ( ). \nPassword must contain a length of at least 8 characters and a maximum of 20 characters.",
        HttpStatus.NOT_ACCEPTABLE);

        ResponseEntity<?> responseInvalidByEmailNoUnique = 
        new ResponseEntity<>("Error.The email is be used ",
        HttpStatus.NOT_ACCEPTABLE);

        ResponseEntity<?> responseInvalidByUserNameNoUnique = 
        new ResponseEntity<>("Error.The username is be used ",
        HttpStatus.NOT_ACCEPTABLE);

        if(controlEmptyField(user)) return responseFieldsEmpty;
        if(!validateEmail(user.getEmail())) return responseEmailInvalid;
        if(!validatePassword(user.getPassword())) return responsePassInvalid;
        if(controlUsernameUnique(user.getUsername())) return 
        responseInvalidByUserNameNoUnique;
        if(controlEmailUnique(user.getEmail())) return responseInvalidByEmailNoUnique;
        userCreator(user);

        return new ResponseEntity<>("User created succesfully!", HttpStatus.OK);
    }

    private Boolean controlEmptyField(ModelRegistrationUser user){
        return (
            user.getName() == null || user.getName().replaceAll("\\s+","").isEmpty() ||
            user.getEmail() == null || user.getEmail().replaceAll("\\s+","").isEmpty() ||
            user.getPassword() == null || user.getPassword().replaceAll("\\s+","").isEmpty() ||
            user.getUsername() == null || user.getUsername().replaceAll("\\s+","").isEmpty()
        )? true : false;
    } 

    private boolean validateEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =  
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 
        Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean validatePassword(String password) {
        /**
         *  Password must contain at least one digit [0-9].
            Password must contain at least one lowercase Latin character [a-z].
            Password must contain at least one uppercase Latin character [A-Z].
            Password must contain at least one special character like ! @ # & ( ).
            Password must contain a length of at least 8 characters and a maximum of 20 characters.
         */
        String PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        
        return matcher.matches();
    }

    private boolean controlUsernameUnique(String username) {
        Optional<User> user= userRepository.findByUsername(username);
        return(user.isPresent())?true:false;
    }

    private boolean controlEmailUnique(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return(user.isPresent())?true:false;
    }

    private void userCreator(ModelRegistrationUser user) {
        User userCreated = new User();
        userCreated.setName(user.getName());
        userCreated.setEmail(user.getEmail());
        userCreated.setPassword(passwordEncoder.encode(user.getPassword()));
        userCreated.setUsername(user.getUsername());
        
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        userCreated.getRoles().add(role.get());

        userRepository.save(userCreated);
    }

    public ResponseEntity<?> createAdmin(String email){
        User admin = new User();
        Optional<User> userRequest = userRepository.findByEmail(email);

        if(!userRequest.isPresent()){
            admin.setEmail(email);
            admin.setName(email);
            admin.setUsername(email);
            admin.setPassword(passwordEncoder.encode("Administrador@1999"));
            
            Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
            admin.getRoles().add(role.get());
            userRepository.save(admin);
            
            return new ResponseEntity<>("ADMIN succesfully!", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("EMAIL ADMIN ALREADY EXISTS", 
            HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //En este caso tomamos el email, pero podemos tomar el username.
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            //Usuario no encontrado.
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        //Tomamos todos los roles
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        user.get().getRoles()
            .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                authorities);
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {

        ResponseEntity<?> responseUserNotExists= 
        new ResponseEntity<>("The user isn't found !",
        HttpStatus.NOT_FOUND);

        ResponseEntity<?> responseSuccesfullyDeleted= 
        new ResponseEntity<>("Succesfully Deleted !",
        HttpStatus.OK);
        
        Optional<User> userRequest = userRepository.findById(id);
        if(userRequest.isPresent()){
            userRepository.delete(userRequest.get());
            return responseSuccesfullyDeleted;
        }else{
            return responseUserNotExists;
        }
        
    }

    @Override
    public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
        String authorizationHeader = request.getHeader("AUTHORIZATION");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<User> user = userRepository.findByUsername(username);
                String accessToken = JWT.create()
                        .withSubject(user.get().getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //Diez minutos de vida
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Tokens tokens = new Tokens();
                tokens.setAccessToken(accessToken);
                tokens.setRefreshToken(refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
        
    }

    @Override
    public ResponseEntity<?> getUser() {
        return new ResponseEntity<>(userRepository.findAll(),
        HttpStatus.OK);
    }

    

    
}
