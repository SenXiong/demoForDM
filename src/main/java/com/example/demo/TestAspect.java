package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect {
    @Pointcut("execution(public * com.example.demo.PerconController.testaop*(..))&& @annotation(com.example.demo.MyAnnotation)")
    /*@Pointcut("execution(public * com.example.demo.PerconController.testaop*(..))")*/
    public void addAdvice(){}
    @Around("addAdvice()")
    public Object Interceptor(ProceedingJoinPoint pjp){
        Object result = null;
        Object[] args = pjp.getArgs();
        if(args != null && args.length >0) {
            String id = (String) args[0];
            if(!"xj".equals(id)) {
                return "sorry,"+id+" have no anthorization";
            }
        }
        try {
            result =pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
