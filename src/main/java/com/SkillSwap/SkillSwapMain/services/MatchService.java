package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Match;
import com.SkillSwap.SkillSwapMain.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    // Create a match between two users
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    // Get all matches for a specific user
    public List<Match> getMatchesForUser(Long userId) {
        return matchRepository.findByUser1IdOrUser2Id(userId, userId);
    }
}