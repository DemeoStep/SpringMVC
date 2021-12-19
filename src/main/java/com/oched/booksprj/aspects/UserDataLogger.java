package com.oched.booksprj.aspects;

import com.oched.booksprj.responses.BookInfoResponse;
import com.oched.booksprj.responses.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Slf4j
public class UserDataLogger {

    @Before("execution(* com.oched.booksprj.services.UserService.*(..))")
    public void logBeforeUserService(JoinPoint joinPoint) {
        log.info("<===================/ Before UserService / ===================>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(joinPoint.getKind());
        log.info("<=============================================================>");
    }

    @After("execution(* com.oched.booksprj.services.UserService.*(..))")
    public void logAfterUserService(JoinPoint joinPoint) {
        log.info("<=================== / After UserService / ===================>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(joinPoint.getKind());
        log.info("<=============================================================>");
    }

    @AfterReturning(value = "execution(* com.oched.booksprj.services.UserService.getUsersList(..))", returning = "response")
    public void logAfterReturningUserService(JoinPoint joinPoint, List<UserInfoResponse> response) {
        log.info("<=============== / After Returning UserService / ===============>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info("Entities: ");
        for (UserInfoResponse user: response) {
            log.info("\t" + user.toString());
        }
        log.info("<=============================================================>");
    }

    @AfterReturning(value = "execution(* com.oched.booksprj.services.UserService.getById(..))", returning = "response")
    public void logAfterReturningUserService(JoinPoint joinPoint, UserInfoResponse response) {
        log.info("<=============== / After Returning UserService / ===============>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(response.toString());
        log.info("<=============================================================>");
    }



    @AfterThrowing(value = "execution(* com.oched.booksprj.services.UserService.*(..))", throwing = "exception")
    public void logAfterReturningUserService(Exception exception) {
        log.info("<=============== / After Throwing UserService / ===============>");
        log.info(exception.getMessage());
        log.info("<=============================================================>");
    }
}
