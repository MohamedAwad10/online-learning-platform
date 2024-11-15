package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.user.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.UserDto;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public List<AllUsersDto> findAll() {

        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toUserResponseDto(user)).toList();
    }

    @Transactional
    public String register(UserDto userDto) throws Exception{

        Optional<Users> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            throw new Exception("This email is already in use");
        }

        Users user = userMapper.toUserEntity(userDto);
        userRepository.save(user);

        return "User signed in successfully";
    }

    public String delete(Integer userId) throws Exception{

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new Exception("User not found");
        }

        Users user = optionalUser.get();
        userRepository.delete(user);

        return "User deleted successfully";
    }

//    public String update(UserDto userDto) {
//
//
//    }

//    public ResponseEntity<?> findById(Integer userId) {
//
//    }
}
