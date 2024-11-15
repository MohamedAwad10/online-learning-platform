package com.onlinelearning.online_learning_platform.dto.user;

import com.onlinelearning.online_learning_platform.model.Role;
import com.onlinelearning.online_learning_platform.model.usercontacts.UserContacts;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class UserUpdatedDto {

    @NotNull(message = "First name cannot be null")
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 character")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&+/#.-])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter," +
                    " one lowercase letter, one number, and one special character ")
    private String password;

    private Set<UserContacts> contacts;

    private String image;

    private String bio;

    private String birthDate;

    private int yearsOfExperience;
}
