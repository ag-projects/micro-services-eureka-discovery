package com.agharibi.controllers;


import com.agharibi.entities.UserDTO;
import com.agharibi.entities.UserResponseModel;
import com.agharibi.entities.UserResponse;
import com.agharibi.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("users")
@RestController
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    private UsersService userService;

    @GetMapping("status")
    public String status() {
        return "User service is up & running on port: "
            + env.getProperty("local.server.port")
            + ", with token= " + env.getProperty("token.secret");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserDTO userDTO) {
        ModelMapper mapper = new ModelMapper();
        UserDTO user = userService.create(userDTO);
        UserResponse mappedUser = mapper.map(user, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(mappedUser);
    }

    @GetMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
        UserDTO userDto = userService.getUserByUserId(userId);
        UserResponseModel responseModel = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

}
