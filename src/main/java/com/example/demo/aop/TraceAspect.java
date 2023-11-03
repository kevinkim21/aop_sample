package com.example.demo.aop;

import com.example.demo.common.Utils;
import com.example.demo.dto.TraceDto;
import com.example.demo.service.TraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TraceAspect {
    private final TraceService traceService;
    private final String TRACE_USER_ID = "userId";
    private final String TRACE_ORDER_KEY = "orderKey";
    private final String TRACE_PAYMENT_ID = "paymentId";

    @Pointcut("@annotation(com.example.demo.annotation.Trace)")
    public void annotatedWithTrace() {}

    // Around advice를 사용하여 메서드 실행 전후로 로깅합니다.
    @Around("annotatedWithTrace()")
    public Object traceAround(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();

        String userId = Utils.getFieldValue(args, TRACE_USER_ID);
        String orderKey = Utils.getFieldValue(args, TRACE_ORDER_KEY);
        String paymentId = Utils.getFieldValue(args, TRACE_PAYMENT_ID);

        log.info("==== 메서드 실행 전 ====");
        String beforePayload = Utils.convertArgsToJson(args);
        saveTrace(userId, orderKey, paymentId, beforePayload);
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Exception in method: {}", methodName, throwable);
        }
        log.info("==== 메서드 실행 후 ===="); 
        String afterPayload = Utils.convertObjectToJson(result);
        saveTrace(userId, orderKey, paymentId, afterPayload);
        return result;
    }

    // 예외 발생 시 로깅을 수행합니다.
    @AfterThrowing(pointcut = "annotatedWithTrace()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        try {
            // 필드 값 가져오기
            String userId = Utils.getFieldValue(args, TRACE_USER_ID);
            String orderKey = Utils.getFieldValue(args, TRACE_ORDER_KEY);
            String paymentId = Utils.getFieldValue(args, TRACE_PAYMENT_ID);
            log.info("==== THROWING ====");
            saveTrace(userId, orderKey, paymentId, e.getMessage());
        } catch (Exception ex) {
            log.error("Error logging after throwing", ex);
        }
    }

    public void saveTrace(String userId, String orderKey, String paymentId, String payload) {
        TraceDto beforeTrace = new TraceDto(
                orderKey,
                userId,
                paymentId,
                payload
        );
        traceService.save(beforeTrace);
    }
    
}
