package com.user.analytics.controller;

import com.user.analytics.exception.ResourceNotFoundException;
import com.user.analytics.model.User;
import com.user.analytics.repository.UserRepository;
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
        return userRepository.save(user);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User user){
        User savedUser = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setUserName(user.getUserName());
        return userRepository.save(savedUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

}
