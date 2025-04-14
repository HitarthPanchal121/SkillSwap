package com.SkillSwap.SkillSwapMain.exception.JPAHibernateExceptions;

import com.SkillSwap.SkillSwapMain.exception.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.QueryTimeoutException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class JpaExceptions {
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @ExceptionHandler({
            EntityNotFoundException.class,
            DataIntegrityViolationException.class,
            OptimisticLockingFailureException.class,
            JpaObjectRetrievalFailureException.class,
            QueryTimeoutException.class,
            TransactionSystemException.class
    })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage handleJpaExceptions(Exception ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                request.getDescription(false));
    }
}
