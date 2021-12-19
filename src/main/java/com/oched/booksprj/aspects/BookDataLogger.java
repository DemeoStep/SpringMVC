package com.oched.booksprj.aspects;

import com.oched.booksprj.responses.BookInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
@Slf4j
public class BookDataLogger {

    @Before("execution(* com.oched.booksprj.services.BookService.*(..))")
    public void logBeforeBookService(JoinPoint joinPoint) {
        log.info("<===================/ Before BookService / ===================>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(joinPoint.getKind());
        log.info("<=============================================================>");
    }

    @After("execution(* com.oched.booksprj.services.BookService.*(..))")
    public void logAfterBookService(JoinPoint joinPoint) {
        log.info("<=================== / After BookService / ===================>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(joinPoint.getKind());
        log.info("<=============================================================>");
    }

    @AfterReturning(value = "execution(* com.oched.booksprj.services.BookService.getAll(..))", returning = "response")
    public void logAfterReturningBookService(JoinPoint joinPoint, List<BookInfoResponse> response) {
        log.info("<=============== / After Returning BookService / ===============>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info("Entities: ");
        for (BookInfoResponse book: response) {
            log.info("\t" + book.toString());
        }
        log.info("<=============================================================>");
    }

    @AfterReturning(value = "execution(* com.oched.booksprj.services.BookService.getById(..))", returning = "response")
    public void logAfterReturningBookService(JoinPoint joinPoint, BookInfoResponse response) {
        log.info("<=============== / After Returning BookService / ===============>");
        log.info(joinPoint.getSignature().getName());
        log.info(Arrays.toString(Arrays.stream(joinPoint.getArgs()).toArray()));
        log.info(joinPoint.getSignature().toString());
        log.info(response.toString());
        log.info("<=============================================================>");
    }


    @AfterThrowing(value = "execution(* com.oched.booksprj.services.BookService.*(..))", throwing = "exception")
    public void logAfterReturningBookService(Exception exception) {
        log.info("<=============== / After Throwing BookService / ===============>");
        log.info(exception.getMessage());
        log.info("<=============================================================>");
    }
}
