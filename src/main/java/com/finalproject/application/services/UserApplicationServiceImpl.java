package com.finalproject.application.services;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.application.mapper.UserDataMapper;
import com.finalproject.application.ports.input.services.UserApplicationService;
import com.finalproject.application.ports.output.repository.UserRepository;
import com.finalproject.application.ports.output.security.CurrentUser;
import com.finalproject.application.ports.output.security.PasswordEncryptor;
import com.finalproject.domain.entity.User;
import com.finalproject.domain.exception.UserNotFoundException;

import java.util.Optional;

public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;
    private final CurrentUser currentUser;
    private final PasswordEncryptor passwordEncryptor;


    public UserApplicationServiceImpl(UserRepository userRepository,
                                      UserDataMapper userDataMapper,
                                      CurrentUser currentUser,
                                      PasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.userDataMapper = userDataMapper;
        this.currentUser = currentUser;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public FindUserResponse findUser(String username, String password) {
        Optional<User> userOptional = userRepository
                .findByUsernameAndPassword(username, passwordEncryptor.encrypt(password));
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with provided credentials does not exist!");
        }

        return userDataMapper.toFindUserResponse(userOptional.get());
    }

    @Override
    public boolean isCurrentUserAdmin() {
        return currentUser.isAdmin();
    }
}
