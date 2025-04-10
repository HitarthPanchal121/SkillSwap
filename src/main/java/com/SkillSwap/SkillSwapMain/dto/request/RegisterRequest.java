package com.SkillSwap.SkillSwapMain.dto.request;

public record RegisterRequest(String name,
                              String email,
                              String password,
                              String profilePictureUrl,
                              String bio,
                              String location) {
}
