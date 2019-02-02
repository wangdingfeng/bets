package com.simple.bets.core.common.exception;
/**
 * @Author wangdingfeng
 * @Description 接口限流异常
 * @Date 10:56 2019/1/11
 **/

public class LimitAccessException extends Exception {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException(String message) {
        super(message);
    }
}