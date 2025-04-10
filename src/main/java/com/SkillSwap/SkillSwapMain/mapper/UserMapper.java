package com.SkillSwap.SkillSwapMain.mapper;

import com.SkillSwap.SkillSwapMain.dto.request.RegisterRequest;
import com.SkillSwap.SkillSwapMain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sessionId", ignore = true)
    @Mapping(target = "jwtToken", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract User toEntity(RegisterRequest registerRequest);

    @Named("encodePassword")
    protected String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}