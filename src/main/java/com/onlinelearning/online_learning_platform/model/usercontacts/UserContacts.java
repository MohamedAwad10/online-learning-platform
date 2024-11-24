package com.onlinelearning.online_learning_platform.model.usercontacts;

import com.onlinelearning.online_learning_platform.model.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_contact_info")
@IdClass(UserContacts.class)
public class UserContacts {

    @Id
    @Column(name = "contact")
    private String contact;

    @Id
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private Users user;
}
