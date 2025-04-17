package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Match;

import java.util.List;

public interface MatchService {
    Match requestMatch(Long learnerId, Long offererId);
    Match respondToMatch(Long matchId, boolean accepted);
    List<Match> getUserMatches(Long userId);
}
