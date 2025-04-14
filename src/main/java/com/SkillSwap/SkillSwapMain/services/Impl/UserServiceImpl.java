package com.SkillSwap.SkillSwapMain.services.Impl;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import com.SkillSwap.SkillSwapMain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            // Only update fields that are non-null in the input
            if (updatedUser.getName() != null) user.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getBio() != null) user.setBio(updatedUser.getBio());
            if (updatedUser.getLocation() != null) user.setLocation(updatedUser.getLocation());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        }

    }
}