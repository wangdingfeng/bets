package com.simple.bets.core.common.vo.server;

import com.simple.bets.core.common.util.ArithUtil;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.common.vo.service
 * @ClassName: CpuInfo
 * @Author: wangdingfeng
 * @Description: CPU相关信息
 * @Date: 2019/3/9 10:28
 * @Version: 1.0
 */
public class CpuInfo {

    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;


    public double getTotal() {
        return ArithUtil.round(ArithUtil.mul(total, 100), 2);
    }


    public double getSys() {
        return ArithUtil.round(ArithUtil.mul(sys / total, 100), 2);
    }


    public double getUsed() {
        return ArithUtil.round(ArithUtil.mul(used / total, 100), 2);
    }


    public double getWait() {
        return ArithUtil.round(ArithUtil.mul(wait / total, 100), 2);
    }


    public double getFree() {
        return ArithUtil.round(ArithUtil.mul(free / total, 100), 2);
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }

    public void setFree(double free) {
        this.free = free;
    }
}
