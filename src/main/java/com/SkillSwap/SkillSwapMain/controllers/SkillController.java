package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SkillController {

    @Autowired
    private SkillService skillService;

    // Add a new skill for a user
    @PostMapping("/skills")
    public ResponseEntity<?> addSkill(@RequestBody Skill skill) {
        return ResponseEntity.ok(skillService.addSkill(skill));

    }

    // Get all skills for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Skill>> getSkillsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(skillService.getSkillsByUserId(userId));
    }
}