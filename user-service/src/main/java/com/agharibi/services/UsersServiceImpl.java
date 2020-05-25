package com.agharibi.services;

import com.agharibi.clients.AlbumsServiceClient;
import com.agharibi.entities.AlbumResponseModel;
import com.agharibi.entities.User;
import com.agharibi.entities.UserDTO;
import com.agharibi.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment environment;
    private AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            Environment environment,
                            AlbumsServiceClient albumsServiceClient) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {

        userDTO.setUserId(UUID.randomUUID().toString());
        userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User user = mapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);

        return mapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s is not found", email)));
        ModelMapper mapper = new ModelMapper();

        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUserId(String userId) {

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User % is not found", userId)));
        ModelMapper mapper = new ModelMapper();

        logger.info("Before calling albums micro-service..");
        List<AlbumResponseModel> albumList = albumsServiceClient.getAlbums(userId);
        logger.info("After calling albums micro-service..");

        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setAlbums(albumList);

        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName)
            .orElseThrow(() ->
                new UsernameNotFoundException(String.format("user %s does not exists", userName)));
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getEncryptedPassword(),
            true, true, true, true, new ArrayList<>());
    }
}
