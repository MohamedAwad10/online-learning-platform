package com.onlinelearning.online_learning_platform.service.user;

import com.onlinelearning.online_learning_platform.dto.user.response.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserRequestDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserUpdateDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UpdatedUserResponseDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UserResponseDto;
import com.onlinelearning.online_learning_platform.exception.RoleNotFoundException;
import com.onlinelearning.online_learning_platform.mapper.UserMapper;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import com.onlinelearning.online_learning_platform.repository.UserRepository;
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

    private UserHandlerService userHandlerService;

    public UserService(UserRepository userRepository, UserMapper userMapper
            , RoleRepository roleRepository, UserHandlerService userHandlerService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.userHandlerService = userHandlerService;
    }

    public List<AllUsersDto> findAll() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toAllUsersDto(user)).toList();
    }

    public UserResponseDto findById(Integer userId) {

        Users user = userHandlerService.checkUserExist(userId);
        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto register(UserRequestDto userRequestDto) {

        userHandlerService.checkEmailUniqueness(userRequestDto.getEmail());
        Users user = userMapper.toStudentEntity(userRequestDto);

        Optional<Role> optionalRole = roleRepository.findByRoleName("STUDENT");
        user.addRole(optionalRole.orElseThrow(() -> new RoleNotFoundException("STUDENT role not found")));

        Users savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Transactional
    public UpdatedUserResponseDto update(UserUpdateDto userDto, Integer userId) {

        Users user = userHandlerService.checkUserExist(userId);

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

        Users updatedUser = userRepository.save(user);
        return userMapper.toUpdatedUserResponseDto(updatedUser, userDto.getContacts());
    }

    @Transactional
    public String delete(Integer userId) {

        Users user = userHandlerService.checkUserExist(userId);
        userRepository.delete(user);
        return "User deleted successfully with ID: "+ user.getId();
    }

    @Transactional
    public String addInstructorRole(Integer userId) {

        Users user = userHandlerService.checkUserExist(userId);

        Role instructorRole = userHandlerService.checkRole(user, "INSTRUCTOR");
        user.addRole(instructorRole);
        userRepository.save(user);

        return "Instructor role added successfully";
    }

    public String setAdminRole(Integer userId) {

        Users user = userHandlerService.checkUserExist(userId);

        Role adminRole = userHandlerService.checkRole(user, "ADMIN");
        user.addRole(adminRole);
        userRepository.save(user);

        return "Admin role added successfully to user";
    }
}
