package org.block.blockbackend.user.service;

import org.block.blockbackend.user.dto.SignUpDTO;
import org.block.blockbackend.user.entity.Role;
import org.block.blockbackend.user.entity.User;
import org.block.blockbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
