package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    // Add a new skill for a user
    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    // Get all skills for a specific user
    public List<Skill> getSkillsByUserId(Long userId) {
        return skillRepository.findByUserId(userId);
    }
}