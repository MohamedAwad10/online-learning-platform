package com.onlinelearning.online_learning_platform.service.user;

import com.onlinelearning.online_learning_platform.dto.user.response.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserRequestDto;
import com.onlinelearning.online_learning_platform.dto.user.request.UserUpdateDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UpdatedUserResponseDto;
import com.onlinelearning.online_learning_platform.dto.user.response.UserResponseDto;
import com.onlinelearning.online_learning_platform.exception.EmailAlreadyInUseException;
import com.onlinelearning.online_learning_platform.exception.RoleAlreadyExistException;
import com.onlinelearning.online_learning_platform.exception.RoleNotFoundException;
import com.onlinelearning.online_learning_platform.exception.UserNotFoundException;
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

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserResponseDto register(UserRequestDto userRequestDto) {

        checkEmailUniqueness(userRequestDto.getEmail());
        Users user = userMapper.toStudentEntity(userRequestDto);

        Optional<Role> optionalRole = roleRepository.findByRoleName("STUDENT");
        user.addRole(optionalRole.orElseThrow(() -> new RoleNotFoundException("STUDENT role not found")));

        Users savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    public List<AllUsersDto> findAll() {
        List<Users> users = userRepository.findAll();
        return users.stream().map(user -> userMapper.toAllUsersDto(user)).toList();
    }

    @Transactional
    public String delete(Integer userId) {

        Users user = checkUserExist(userId);
        userRepository.delete(user);
        return "User deleted successfully with ID: "+ user.getId();
    }

    @Transactional
    public UpdatedUserResponseDto update(UserUpdateDto userDto, Integer userId) {

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

        Users updatedUser = userRepository.save(user);
        return userMapper.toUpdatedUserResponseDto(updatedUser, userDto.getContacts());
    }

    @Transactional
    public String addInstructorRole(Integer userId) {

        Users user = checkUserExist(userId);

        Role instructorRole = checkRole(user, "INSTRUCTOR");
        user.addRole(instructorRole);
        userRepository.save(user);

        return "Instructor role added successfully";
    }

    public UserResponseDto findById(Integer userId) {

        Users user = checkUserExist(userId);
        return userMapper.toUserResponseDto(user);
    }

    public String setAdminRole(Integer userId) {

        Users user = checkUserExist(userId);

        Role adminRole = checkRole(user, "ADMIN");
        user.addRole(adminRole);
        userRepository.save(user);

        return "Admin role added successfully to user";
    }

    public Users checkUserExist(Integer userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        return optionalUser.get();
    }

    public void checkEmailUniqueness(String email){
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new EmailAlreadyInUseException("This email is already in use");
        }
    }

    public Role checkRole(Users user, String roleName){
        if(user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(roleName))){
            throw new RoleAlreadyExistException("User already has "+ roleName +" role");
//            throw new RoleAlreadyExistException("User already has instructor role, redirect him to instructor page.");
        }

        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName +" role not found"));
    }
}
