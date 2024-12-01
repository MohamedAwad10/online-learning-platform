package com.onlinelearning.online_learning_platform.controller;

import com.onlinelearning.online_learning_platform.dto.user.response.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserUpdateDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UpdatedUserResponseDto;
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
    public ResponseEntity<?> findAllUsers() {
        List<AllUsersDto> users = userService.findAll();
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users");
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){

        String message = userService.delete(userId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> addInstructorRole(@PathVariable Integer userId){

        String message =  userService.addInstructorRole(userId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto){

        String message = userService.register(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UpdatedUserResponseDto> updateUser(@RequestBody UserUpdateDto userDto, @PathVariable Integer userId){
        return ResponseEntity.ok(userService.update(userDto, userId));
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> findUserById(@PathVariable Integer userId){
//        return userService.findById(userId);
//    }
}
