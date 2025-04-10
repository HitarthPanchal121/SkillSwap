package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Session;
import com.SkillSwap.SkillSwapMain.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // Schedule a session for a match
    @PostMapping
    public ResponseEntity<Session> scheduleSession(@RequestBody Session session) {
        return ResponseEntity.ok(sessionService.scheduleSession(session));
    }

    // Submit feedback for a session
    @PutMapping("/{sessionId}/feedback")
    public ResponseEntity<Session> submitFeedback(
            @PathVariable Long sessionId,
            @RequestParam String feedbackUser1,
            @RequestParam Integer ratingUser1,
            @RequestParam String feedbackUser2,
            @RequestParam Integer ratingUser2) {
        return ResponseEntity.ok(sessionService.submitFeedback(sessionId, feedbackUser1, ratingUser1, feedbackUser2, ratingUser2));
    }
}