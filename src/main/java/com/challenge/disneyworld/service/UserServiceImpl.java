package com.challenge.disneyworld.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.challenge.disneyworld.entity.User;
import com.challenge.disneyworld.repository.UserRepository;
import com.challenge.disneyworld.service.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;


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
        userCreated.setPassword(user.getPassword());
        userCreated.setUsername(user.getUsername());
        //TODO SETEAR EL ROL!
        userCreated.setRoles(null);

        userRepository.save(userCreated);
    }
    
}