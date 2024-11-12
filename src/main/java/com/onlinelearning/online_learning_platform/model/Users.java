package com.onlinelearning.online_learning_platform.model;

import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@ToString
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 character")
    @NotBlank(message = "First name cannot be blank")
    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    @NotBlank(message = "Last name cannot be blank")
    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Enter a correct pattern for your email")
    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&+/#.-])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter," +
                    " one lowercase letter, one number, and one special character ")
    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "profile_image")
    private String profileImage;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<UserContacts> contacts;
}
