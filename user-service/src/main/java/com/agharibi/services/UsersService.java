package com.agharibi.services;


import com.agharibi.entities.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDTO create(UserDTO user);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUserId(String userId);


}
