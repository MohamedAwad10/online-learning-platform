package com.onlinelearning.online_learning_platform.model.usercontacts;

import com.onlinelearning.online_learning_platform.model.Users;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserContactID implements Serializable {

    private String phone;
    private Users user;
}
