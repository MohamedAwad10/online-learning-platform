package com.onlinelearning.online_learning_platform.model.usercontacts;

import com.onlinelearning.online_learning_platform.model.Users;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_contact_info")
@IdClass(UserContacts.class)
public class UserContacts {

    @Id
    @Column(name = "contact")
    private String contact;

    @Id
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private Users user;
}
