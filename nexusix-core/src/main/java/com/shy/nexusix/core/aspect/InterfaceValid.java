package com.shy.nexusix.core.aspect;

import com.shy.nexusix.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class InterfaceValid {

    private static final String POINTCUT_BUSINESS = "execution(* com.shy.nexusix..service.impl..*.*(..))";

    @Pointcut(POINTCUT_BUSINESS)
    public void businessPointcut() {
    }

    @Around("businessPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;

        try {
            // TODO Sa-Token 获取 token 以下大多校验存redis

            // 是否登录

            // 关于权限、角色编码解决方案：考虑特定前缀 + 根据方法名分割

            // 校验租户状态

            // 校验数据权限范围？

            // 权限校验

            // 角色校验

            // 通用业务规则校验 [ 1. 敏感数据仅超级管理员可见(如逻辑删除、编码、用户信息是否脱敏) ]

            log.info("环绕通知执行完成");

            // 执行目标方法
            result = joinPoint.proceed();
        } catch (BusinessException e) {
            // 业务异常：原样抛出，交给 GlobalExceptionHandler 统一返回业务码和消息
            throw e;
        } catch (Exception e) {
            // 非业务异常：记录日志后继续抛出，交给全局异常兜底处理
            log.error("接口校验切面捕获系统异常, method={}, message={}",
                    joinPoint.getSignature().toShortString(), e.getMessage(), e);
            throw e;
        } finally {

        }
        return result;
    }

}
