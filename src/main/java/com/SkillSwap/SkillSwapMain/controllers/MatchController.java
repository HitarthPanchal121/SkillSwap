package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Match;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/request")
    public ResponseEntity<BaseResponse<?>> requestMatch(@RequestParam Long learnerId, @RequestParam Long offererId) {
        Match match = matchService.requestMatch(learnerId, offererId);
        BaseResponse<Match> response = new BaseResponse<>(200, "Match request sent successfully", match);
        return ResponseEntity.status(response.resultCode()).body(response);
    }

    @PostMapping("/respond")
    public ResponseEntity<BaseResponse<?>> respondToMatch(@RequestParam Long matchId, @RequestParam boolean accepted) {
        Match updatedMatch = matchService.respondToMatch(matchId, accepted);
        String message = accepted ? "Match accepted successfully" : "Match rejected successfully";
        BaseResponse<Match> response = new BaseResponse<>(200, message, updatedMatch);
        return ResponseEntity.status(response.resultCode()).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<?>> getUserMatches(@PathVariable Long userId) {
        List<Match> matches = matchService.getUserMatches(userId);
        BaseResponse<List<Match>> response = new BaseResponse<>(200, "User matches retrieved", matches);
        return ResponseEntity.status(response.resultCode()).body(response);
    }
}