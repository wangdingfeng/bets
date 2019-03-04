package com.simple.bets.core.base.model;

/**
 * 接口限流
 */
public enum LimitType {
    // 传统类型
    CUSTOMER,
    // 根据 IP 限制
    IP;
}
