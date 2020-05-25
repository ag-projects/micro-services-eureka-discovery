package com.agharibi.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Size(min = 2, message = "First name cannot be less than two characters")
    @NotNull(message = "First name cannot be empty")
    private String firstName;

    @Size(min = 2, message = "Last name cannot be less than two characters")
    @NotNull(message = "Last name cannot be empty")
    private String lastName;

    @Email
    @NotNull(message = "Email cannot be empty")
    private String email;

    @Size(min = 6, max = 16, message = "Password must be greater than 6 and less than 16 characters")
    @NotNull(message = "Password cannot be empty")
    private String password;
}
