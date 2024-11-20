package com.onlinelearning.online_learning_platform.service;

import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Instructor;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.repository.InstructorRepository;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InstructorService {

    private UserRepository userRepository;
    private InstructorRepository instructorRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserMapper userMapper,
                             UserRepository userRepository, RoleRepository roleRepository) {
        this.instructorRepository = instructorRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public String addInstructorRole(Integer userId) throws Exception {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        Users user = optionalUser.get();

        Role instructorRole = roleRepository.findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new Exception("INSTRUCTOR role not found"));

        user.addRole(instructorRole);
        userRepository.save(user);

        return "Instructor role added successfully";
    }
}
