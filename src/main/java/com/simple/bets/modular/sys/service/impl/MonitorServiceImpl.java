package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.AddressUtils;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserOnlineModel;
import com.simple.bets.modular.sys.service.MonitorService;
import com.simple.bets.core.common.vo.server.ServerInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author wangdingfeng
 * @Description shiro 对象管理
 * @Date 14:42 2019/2/2
 **/
@Service("monitorServiceImpl")
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public List<UserOnlineModel> list() {
        List<UserOnlineModel> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnlineModel userOnline = new UserOnlineModel();
            UserModel user;
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (UserModel) principalCollection.getPrimaryPrincipal();
                userOnline.setUserName(user.getUsername());
                userOnline.setUserId(user.getUserId().toString());
            }
            userOnline.setId((String) session.getId());
            userOnline.setHost(session.getHost());
            userOnline.setStartTimestamp(session.getStartTimestamp());
            userOnline.setLastAccessTime(session.getLastAccessTime());
            long timeout = session.getTimeout();
            userOnline.setStatus(timeout == 0L ? "0" : "1");
            String address = AddressUtils.getCityInfo(userOnline.getHost());
            userOnline.setLocation(address);
            userOnline.setTimeout(timeout);
            list.add(userOnline);
        }
        return list;
    }

    @Override
    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        session.stop();
        sessionDAO.delete(session);
        return true;
    }

    @Override
    public ServerInfo getServerInfo() {
        ServerInfo serverInfo =new ServerInfo();
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            serverInfo.setCpuInfo(hal.getProcessor());
            serverInfo.setMemInfo(hal.getMemory());
            serverInfo.setSysInfo();
            serverInfo.setJvmInfo();
            serverInfo.setSysFiles(si.getOperatingSystem());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serverInfo;
    }


}
