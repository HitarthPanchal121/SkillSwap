package com.SkillSwap.SkillSwapMain.exception.SpringSecurityExceptions;

import com.SkillSwap.SkillSwapMain.exception.ErrorMessage;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.auth.login.AccountException;
import java.nio.file.AccessDeniedException;
import java.util.Date;

@RestControllerAdvice
public class SecurityExceptions {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @ExceptionHandler({
            AuthenticationException.class,
            AccountException.class,
            BadCredentialsException.class,
            DisabledException.class,
            LockedException.class,
            UsernameNotFoundException.class,
            InsufficientAuthenticationException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleSpringSecurityExceptions(Exception ex, WebRequest request) {
        HttpStatus status = ex instanceof AccessDeniedException ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return new ErrorMessage(
                status.value(),
                new Date(),
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                request.getDescription(false));
    }

}
