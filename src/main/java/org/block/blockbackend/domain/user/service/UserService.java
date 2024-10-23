package org.block.blockbackend.domain.user.service;

import org.block.blockbackend.domain.user.dto.SignUpDTO;
import org.block.blockbackend.domain.user.entity.Role;
import org.block.blockbackend.domain.user.entity.User;
import org.block.blockbackend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {

    public UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(SignUpDTO signUpDTO) {

        User user = new User(
                signUpDTO.getId(),
                signUpDTO.getPasswd(),
                signUpDTO.getName(),
                3000,
                Role.USER
        );

        return userRepository.save(user);
    }

    public void removeUser(int userNo) {
        userRepository.deleteById(userNo);
    }
}
