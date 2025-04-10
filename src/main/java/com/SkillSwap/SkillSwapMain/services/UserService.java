package com.SkillSwap.SkillSwapMain.services;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get a user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    // Update a user's profile
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setLocation(updatedUser.getLocation());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}