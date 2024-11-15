package com.onlinelearning.online_learning_platform.mapper;

import com.onlinelearning.online_learning_platform.dto.user.AllUsersDto;
import com.onlinelearning.online_learning_platform.dto.user.UserDto;
import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.Users;
import com.onlinelearning.online_learning_platform.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public AllUsersDto toUserResponseDto(Users user){
        return AllUsersDto.builder()
                .fullName(user.getFirstName()+" "+user.getLastName())
                .email(user.getEmail())
                .image(user.getProfileImage())
                .createdAt(user.getCreatedAt().toString())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .build();
    }

    public Users toUserEntity(UserDto userDto){

        Set<Role> rolesSet = new HashSet<>();
        for(String role: userDto.getRoles()){
            Optional<Role> optionalRole = roleRepository.findByRoleName(role);
            rolesSet.add(optionalRole.get());
        }

        return Users.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(rolesSet)
                .contacts(userDto.getContacts())
                .profileImage(userDto.getImage())
                .build();
    }
}
