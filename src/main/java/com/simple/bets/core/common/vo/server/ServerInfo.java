package com.simple.bets.core.common.vo.server;

import com.simple.bets.core.common.util.ArithUtil;
import com.simple.bets.core.common.util.IPUtils;
import com.simple.bets.core.common.util.ToolUtils;
import com.simple.bets.core.common.vo.server.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.sys.service
 * @ClassName: ServerInfoService
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/3/8 20:40
 * @Version: 1.0
 */
public class ServerInfo {

    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private CpuInfo cpu = new CpuInfo();

    /**
     * 內存相关信息
     */
    private MemInfo mem = new MemInfo();

    /**
     * JVM相关信息
     */
    private JvmInfo jvm = new JvmInfo();

    /**
     * 服务器相关信息
     */
    private SysInfo sys = new SysInfo();

    /**
     * 磁盘相关信息
     */
    private List<SysFileInfo> sysFiles = new LinkedList<>();


    /**
     * 设置CPU信息
     */
    public void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    public void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
     */
    public void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IPUtils.getHostName());
        sys.setComputerIp(IPUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    public void setJvmInfo() throws UnknownHostException {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
     */
    public void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFileInfo sysFile = new SysFileInfo();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(ToolUtils.convertFileSize(total));
            sysFile.setFree(ToolUtils.convertFileSize(free));
            sysFile.setUsed(ToolUtils.convertFileSize(used));
            sysFile.setUsage(ArithUtil.mul(ArithUtil.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    public CpuInfo getCpu() {
        return cpu;
    }

    public void setCpu(CpuInfo cpu) {
        this.cpu = cpu;
    }

    public MemInfo getMem() {
        return mem;
    }

    public void setMem(MemInfo mem) {
        this.mem = mem;
    }

    public JvmInfo getJvm() {
        return jvm;
    }

    public void setJvm(JvmInfo jvm) {
        this.jvm = jvm;
    }

    public SysInfo getSys() {
        return sys;
    }

    public void setSys(SysInfo sys) {
        this.sys = sys;
    }

    public List<SysFileInfo> getSysFiles() {
        return sysFiles;
    }

    public void setSysFiles(List<SysFileInfo> sysFiles) {
        this.sysFiles = sysFiles;
    }
}
