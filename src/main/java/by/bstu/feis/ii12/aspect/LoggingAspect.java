package by.bstu.feis.ii12.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LogManager.getLogger("ProjectLogger");

    @Around("execution(* by.bstu.feis.ii12.core.*.isPositive(..))")
    public Object logDetectorCall(ProceedingJoinPoint thisJoinPoint) throws Throwable {

        Object result = thisJoinPoint.proceed();

        LOGGER.info(niceName(thisJoinPoint, result));

        return result;
    }

    private String niceName(JoinPoint joinPoint, Object result) {
        return joinPoint.getTarget().getClass() + "#" + joinPoint.getSignature().getName()
                + "\nargs:" + Arrays.toString(joinPoint.getArgs())
                + "\nresult:" + result;
    }

}