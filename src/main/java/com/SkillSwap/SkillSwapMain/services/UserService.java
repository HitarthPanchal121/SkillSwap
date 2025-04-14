package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public Optional<User> getUserById(Long id);

    public List<User> getAllUser();

    public User updateUser(Long id, User updatedUser);

    public void deleteUserById(Long id);
}
