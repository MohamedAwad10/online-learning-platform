package com.onlinelearning.online_learning_platform.model;

import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@ToString
@SuperBuilder
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;

    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    private String email;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Column(name = "joined_date")
    @CreationTimestamp
    private LocalDate joinedDate;

    @Column(name = "profile_image", columnDefinition = "TEXT")
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
            cascade = {CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private Set<UserContacts> contacts;

    public void addRole(Role role){
        if(this.roles == null){
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

}
