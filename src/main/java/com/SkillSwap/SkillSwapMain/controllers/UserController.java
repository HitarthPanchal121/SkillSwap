package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.services.Impl.UserServiceImpl;
import com.SkillSwap.SkillSwapMain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/userId/{userId}")
    public Optional<User> getUserByid(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @DeleteMapping("/userId/{userId}")
    public String deleteUserById(@PathVariable Long userId){
        if(userService.getUserById(userId).isPresent()){
            userService.deleteUserById(userId);
            return "User Deleted Successfully";
        }
        return "User With Id "+userId+" not Found";
    }
}
