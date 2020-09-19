package com.user.analytics.controller;

import com.user.analytics.exception.ResourceNotFoundException;
import com.user.analytics.exception.UnauthorizedException;
import com.user.analytics.model.User;
import com.user.analytics.repository.UserRepository;
import com.user.analytics.utility.PasswordUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
    }

    @PostMapping("createUser")
    public User createUser(@Valid @RequestBody User user){
        String salt = PasswordUtility.getSalt(30);
        String securedPassword =  PasswordUtility.generateSecurePassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(securedPassword);
        return userRepository.save(user);
    }

    @PostMapping("/updateUser/{id}")
    public User updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User user){
        userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
        return userRepository.save(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/userLogin")
    public User userLogin(@RequestBody User user){
        User savedUser = userRepository.findById(user.getId()).orElseThrow(()-> new ResourceNotFoundException("User", "id", user.getId()));
        boolean checkPassword = PasswordUtility.verifyUserPassword(user.getPassword(), savedUser.getPassword(), savedUser.getSalt());
        if(!checkPassword){
            throw new UnauthorizedException("User", "userName", savedUser.getUserName());
        }
        return savedUser;
    }

}
