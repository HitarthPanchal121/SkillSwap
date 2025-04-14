package com.SkillSwap.SkillSwapMain.repositories;
import com.SkillSwap.SkillSwapMain.entity.Skill;
import com.SkillSwap.SkillSwapMain.entity.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUserId(Long userId);
    List<Skill> findBySkillNameContainingIgnoreCaseAndSkillType(String skillName, SkillType skillType);
}
