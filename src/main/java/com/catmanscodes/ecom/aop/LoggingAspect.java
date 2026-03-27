package com.catmanscodes.ecom.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect //1. this is our aspect
public class LoggingAspect {

    private static  final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    //return type, package, class, method, parameters i.e execution(* com.catmanscodes.ecom.controller.*.*(..))

    //2. @Before is an advice that runs before the matched method execution
    //3. Pointcut - execution(* com.catmanscodes.ecom.controller.*.*(..)) - this matches all methods in any class within the com.catmanscodes.ecom.controller package

    @Before("execution(* com.catmanscodes.ecom.controller.*.*(..)) || execution(* com.catmanscodes.ecom.service.*.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        LOGGER.info("A controller / service method is about to be called."+
                        "Class: " + joinPoint.getSignature().getDeclaringTypeName() +
                        ", Method: " + joinPoint.getSignature().getName()
        );
    }

    @After("execution(* com.catmanscodes.ecom.controller.*.*(..)) || execution(* com.catmanscodes.ecom.service.*.*(..))")
    public void logAfterControllerMethods(JoinPoint joinPoint) {
        LOGGER.info("A controller / service method has been called." +
                "Class: " + joinPoint.getSignature().getDeclaringTypeName() +
                ", Method: " + joinPoint.getSignature().getName()
        );
    }

    @AfterThrowing(pointcut = "execution(* com.catmanscodes.ecom.controller.*.*(..)) || execution(* com.catmanscodes.ecom.service.*.*(..))", throwing = "ex")
    public void logAfterThrowingError(JoinPoint joinPoint, Throwable ex) {
        LOGGER.error("An exception was thrown in method: " + joinPoint.getSignature().getName() + " of class: " + joinPoint.getSignature().getDeclaringTypeName() + ". Exception message: " + ex.getMessage());
    }

    @AfterReturning(pointcut = "execution(* com.catmanscodes.ecom.controller.*.*(..)) || execution(* com.catmanscodes.ecom.service.*.*(..))", returning = "result")
    public void logAfterReturningSuccessMethodCall(JoinPoint joinPoint, Object result) {
        LOGGER.info("Method: " + joinPoint.getSignature().getName() + " of class: " + joinPoint.getSignature().getDeclaringTypeName() + " returned: " + result);
    }

   @Around("execution(* com.catmanscodes.ecom.controller.HomeController.getHome(..))")
    public Object logAroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object obj = joinPoint.proceed(); // Proceed with the method execution


        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        LOGGER.info("Around advice: Before executing method: " +duration +" ms");

        return obj; // Return the result of the method execution

    }



}
