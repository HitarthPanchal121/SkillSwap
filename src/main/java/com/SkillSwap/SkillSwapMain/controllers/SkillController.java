package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.entity.SkillType;
import com.SkillSwap.SkillSwapMain.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    // Add a new skill for a user
    @PostMapping("/add")
    public ResponseEntity<?> addSkill(@RequestBody Skill skill) {
        return ResponseEntity.ok(skillService.addSkill(skill));
    }

    // Get all skills for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Skill>> getSkillsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(skillService.getSkillsByUserId(userId));
    }
    // updating skill ofd a specific user
    @PutMapping("/{skillId}")
    public ResponseEntity<Skill> updateSkill(
            @PathVariable Long skillId,
            @RequestBody Skill skill
    ) {
        Skill updatedSkill = skillService.updateSkill(skillId, skill);
        return ResponseEntity.ok(updatedSkill);
    }
    @DeleteMapping("/skillId/{id}")
    public void deleteSkillById(@PathVariable Long id) {
        skillService.deleteSkillById(id);
    }
    @GetMapping
    public ResponseEntity<List<Skill>> searchSkillsByNameAndType (@RequestParam String skillName, SkillType skillType) {
        return ResponseEntity.ok(skillService.searchSkillsByNameAndType(skillName, skillType));
    }

}