package com.SkillSwap.SkillSwapMain.services.Impl;


import com.SkillSwap.SkillSwapMain.entity.Match;
import com.SkillSwap.SkillSwapMain.entity.MatchStatus;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.MatchRepository;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import com.SkillSwap.SkillSwapMain.services.MatchService;
import com.SkillSwap.SkillSwapMain.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public Match requestMatch(Long learnerId, Long offererId) {
        User learner = userRepository.findById(learnerId).orElseThrow();
        User offerer = userRepository.findById(offererId).orElseThrow();

        Match match = new Match();
        match.setUser1(learner);
        match.setUser2(offerer);
        match.setStatus(MatchStatus.PENDING);

        matchRepository.save(match);

        notificationService.sendNotification(offererId, "You have a new match request from " + learner.getName());
        return match;
    }

    @Override
    public Match respondToMatch(Long matchId, boolean accepted) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        match.setStatus(accepted ? MatchStatus.ACCEPTED : MatchStatus.REJECTED);
        matchRepository.save(match);

        Long learnerId = match.getUser1().getId();
        String message = accepted ? "Your match request has been accepted!" : "Your match request was rejected.";
        notificationService.sendNotification(learnerId, message);

        return match;
    }

    @Override
    public List<Match> getUserMatches(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return matchRepository.findByUser1OrUser2(user, user);
    }
}
