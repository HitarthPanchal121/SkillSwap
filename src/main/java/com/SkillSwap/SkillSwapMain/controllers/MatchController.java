package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Match;
import com.SkillSwap.SkillSwapMain.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // Create a match between two users
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    // Get all matches for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Match>> getMatchesForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(matchService.getMatchesForUser(userId));
    }
}