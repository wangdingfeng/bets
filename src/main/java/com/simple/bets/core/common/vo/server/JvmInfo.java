package com.simple.bets.core.common.vo.server;

import com.simple.bets.core.common.util.ArithUtil;
import com.simple.bets.core.common.util.DateUtil;

import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.common.vo.service
 * @ClassName: JvmInfo
 * @Author: wangdingfeng
 * @Description: 虚拟机相关信息
 * @Date: 2019/3/9 10:30
 * @Version: 1.0
 */
public class JvmInfo {

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        return ArithUtil.div(total, (1024 * 1024), 2);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMax() {
        return ArithUtil.div(max, (1024 * 1024), 2);
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getFree() {
        return ArithUtil.div(free, (1024 * 1024), 2);
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        return ArithUtil.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage() {
        return ArithUtil.mul(ArithUtil.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        return DateUtil.date2Str(DateUtil.getServerStartDate(), DateUtil.DATE_PATTERN_DETAIL);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        return DateUtil.getDatePoor(new Date(), DateUtil.getServerStartDate());
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
