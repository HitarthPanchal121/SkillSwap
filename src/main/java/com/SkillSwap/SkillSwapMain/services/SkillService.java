package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.entity.SkillType;
import com.SkillSwap.SkillSwapMain.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SkillService {
    // Add a new skill for a user
    public Skill addSkill(Skill skill) ;
//    {
//        return skillRepository.save(skill);
//    }

    // Get all skills for a specific user
    public List<Skill> getSkillsByUserId(Long userId) ;
//    {
//        return skillRepository.findByUserId(userId);
//    }
    public Skill updateSkill(Long id,Skill updatedSkill) ;
    public void deleteSkillById(Long id) ;
    public List<Skill> searchSkillsByNameAndType(String skillName, SkillType skillType) ;
}