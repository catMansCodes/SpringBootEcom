package com.catmanscodes.ecom.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

    @Around("execution(* com.catmanscodes.ecom.controller.ProductControler.getProductById(..)) && args(productId)")
    public Object validateProductId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Integer productId = (Integer) args[0];

        if (productId <= 0) {
            LOGGER.warn("Invalid product ID: {}. Product ID must be a positive integer.", productId);
            throw new IllegalArgumentException("Product ID must be a positive integer.");
        }

        return joinPoint.proceed(); // Proceed with the method execution if validation passes
    }

}
