package com.SkillSwap.SkillSwapMain.dto.request;

import lombok.NonNull;

public record LoginRequest(String email,
                           String password,
                           String mobileNumber) {


}

