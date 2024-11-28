package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.user.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.UserDto;
import com.onlinelearning.online_learning_platform.dto.user.UserUpdateDto;
import com.onlinelearning.online_learning_platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/online-learning/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(){
//        return userService.login();
//    }

    @GetMapping("/")
    public ResponseEntity<?> findAllUsers(){
        List<AllUsersDto> users = userService.findAll();
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users");
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        try {
            String message = userService.delete(userId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> addInstructorRole(@PathVariable Integer userId){
        try {
            String message =  userService.addInstructorRole(userId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto){
        try {
            String message = userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userDto, @PathVariable Integer userId){

        try {
            String message = userService.update(userDto, userId);;
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> findUserById(@PathVariable Integer userId){
//        return userService.findById(userId);
//    }
}
