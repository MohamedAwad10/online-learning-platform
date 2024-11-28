package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.dto.user.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.UserDto;
import com.onlinelearning.online_learning_platform.dto.user.UserUpdateDto;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public String register(UserDto userDto) throws Exception{

        Optional<Users> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if(optionalUser.isPresent()){
            throw new Exception("This email is already in use");
        }
        Users user = userMapper.toStudentEntity(userDto);

        Optional<Role> optionalRole = roleRepository.findByRoleName("STUDENT");
        user.addRole(optionalRole.orElseThrow(() -> new Exception("STUDENT role not found")));

        Users savedUser = userRepository.save(user);

        return "User signed in successfully with id: " +savedUser.getId();
    }


    public List<AllUsersDto> findAll() {

        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toUserResponseDto(user)).toList();
    }

    public String delete(Integer userId) throws Exception{

        Users user = checkUserExist(userId);
        userRepository.delete(user);

        return "User deleted successfully";
    }

    @Transactional
    public String update(UserUpdateDto userDto, Integer userId) throws Exception{

        Users user = checkUserExist(userId);

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setContacts(userDto.getContacts()
                .stream().map(userContactDto -> {
                            UserContacts contact = userMapper.toUserContactsEntity(userContactDto);
                            contact.setUser(user);
                            return contact;
                        })
                .collect(Collectors.toSet()));
        user.setProfileImage(userDto.getImage());

        userRepository.save(user);

        return "User Updated Successfully with ID: "+user.getId();
    }

    @Transactional
    public String addInstructorRole(Integer userId) throws Exception {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        Users user = optionalUser.get();

        if(user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("INSTRUCTOR"))){
            throw new Exception("User already has instructor role, redirect him to instructor page.");
        }

        Role instructorRole = roleRepository.findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new Exception("INSTRUCTOR role not found"));

        user.addRole(instructorRole);
        userRepository.save(user);

        return "Instructor role added successfully";
    }

    public Users checkUserExist(Integer userId) throws Exception{
        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new Exception("User not found");
        }

        return optionalUser.get();
    }

//    public ResponseEntity<?> findById(Integer userId) {
//
//    }
}
