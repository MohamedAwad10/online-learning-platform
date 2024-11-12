package com.onlinelearning.online_learning_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Role must not be null")
    @NotBlank(message = "Role cannot be blank")
    @Size(min = 3, message = "Role name must be more than 2 character")
    @Column(
            name = "role_name",
            nullable = false,
            unique = true
    )
    private String roleName;
}
