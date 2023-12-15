package com.bigbang.haruharu.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {

    String key(); // (1) 분산락의 락을 설정할 이름입니다.

    TimeUnit timeUnit() default TimeUnit.SECONDS; // (2) 시간 단위(MILLISECONDS, SECONDS, MINUTE..)

    long waitTime() default 5L; // (3) 락을 획득하기 위한 대기 시간

    long leaseTime() default 3L; // (4) 락을 임대하는 시간
}
