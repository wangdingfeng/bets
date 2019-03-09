package com.simple.bets.core.common.vo.server;

import com.simple.bets.core.common.util.ArithUtil;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.common.vo.service
 * @ClassName: MemInfo
 * @Author: wangdingfeng
 * @Description: 服务器内存信息
 * @Date: 2019/3/9 10:31
 * @Version: 1.0
 */
public class MemInfo {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal() {
        return ArithUtil.div(total, (1024 * 1024 * 1024), 2);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getUsed() {
        return ArithUtil.div(used, (1024 * 1024 * 1024), 2);
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public double getFree() {
        return ArithUtil.div(free, (1024 * 1024 * 1024), 2);
    }

    public void setFree(long free) {
        this.free = free;
    }

    public double getUsage() {
        return ArithUtil.mul(ArithUtil.div(used, total, 4), 100);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public void setFree(double free) {
        this.free = free;
    }
}
