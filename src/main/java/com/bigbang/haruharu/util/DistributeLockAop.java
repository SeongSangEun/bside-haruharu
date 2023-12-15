package com.bigbang.haruharu.util;

import com.bigbang.haruharu.advice.error.DefaultException;
import com.bigbang.haruharu.advice.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {
    private static final String REDISSON_KEY_PREFIX = "RLOCK_";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.bigbang.haruharu.util.DistributeLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);     // (1) @DistributeLock annotation을 가져옴

        String key = REDISSON_KEY_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributeLock.key());    // (2)@DistributeLock에 전달한 key를 가져오기 위해 SpringEL 표현식을 파싱

        RLock rLock = redissonClient.getLock(key);    // (3)Redisson에 해당 락의 RLock 인터페이스를 가져옴

        try {
            boolean available = rLock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(), distributeLock.timeUnit());    // (4)tryLock method를 이용해 Lock 획득을 시도 (획득 실패시 Lock이 해제 될 때까지 subscribe)
            if (!available) {
                throw new DefaultException(ErrorCode.INVALID_PARAMETER);
            }
            log.info(String.valueOf(available));
            log.info("get lock success {}" , key);
            return aopForTransaction.proceed(joinPoint);    // (5)@DistributeLock이 선언된 메소드의 로직 수행(별도 트랜잭션으로 분리)
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
            Thread.currentThread().interrupt();
            throw new InterruptedException(e.getMessage());
        } finally {
            log.info("unlock time {}" , key);
            rLock.unlock();    // (6)종료 혹은 예외 발생시 finally에서 Lock을 해제함
        }
    }
}