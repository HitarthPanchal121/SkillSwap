package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Session;
import com.SkillSwap.SkillSwapMain.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    // Schedule a session for a match
    public Session scheduleSession(Session session) {
        return sessionRepository.save(session);
    }

    // Submit feedback for a session
    public Session submitFeedback(Long sessionId, String feedbackUser1, Integer ratingUser1, String feedbackUser2, Integer ratingUser2) {
        return sessionRepository.findById(sessionId).map(session -> {
            session.setFeedbackUser1(feedbackUser1);
            session.setRatingUser1(ratingUser1);
            session.setFeedbackUser2(feedbackUser2);
            session.setRatingUser2(ratingUser2);
            session.setStatus(Session.SessionStatus.COMPLETED); // Corrected reference
            return sessionRepository.save(session);
        }).orElseThrow(() -> new RuntimeException("Session not found"));
    }
}