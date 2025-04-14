package com.SkillSwap.SkillSwapMain.services.Impl;

import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.entity.SkillType;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.SkillRepository;
import com.SkillSwap.SkillSwapMain.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public List<Skill> getSkillsByUserId(Long userId) {
      return skillRepository.findByUserId(userId);
    }

    @Override
    public Skill updateSkill(Long id, Skill updatedSkill) {
        return skillRepository.findById(id).map(skill ->  {
            // Only update fields that are non-null in the input
            if (updatedSkill.getSkillName()!= null) skill.setSkillName(updatedSkill.getSkillName());
            if (updatedSkill.getSkillType()!= null) skill.setSkillType(updatedSkill.getSkillType());
            if (updatedSkill.getProficiencyLevel() != null) skill.setProficiencyLevel(updatedSkill.getProficiencyLevel());
            return skillRepository.save(skill);
        }).orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    @Override
    public void deleteSkillById(Long id) {
        Optional<Skill> optionalSkill = skillRepository.findById(id);

        if (optionalSkill.isPresent()) {
            skillRepository.deleteById(id);
        }
    }


    @Override
    public List<Skill> searchSkillsByNameAndType(String skillName, SkillType skillType) {
        // Validate input
        if (skillName == null || skillType == null) {
            throw new IllegalArgumentException("Both skillName and skillType must be provided.");
        }
        // Use a custom query to find skills by name and type
        return skillRepository.findBySkillNameContainingIgnoreCaseAndSkillType(skillName, skillType);
    }
}
