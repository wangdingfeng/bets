package com.simple.bets.modular.sys.service;


import com.simple.bets.core.common.vo.server.ServerInfo;
import com.simple.bets.modular.sys.model.UserOnlineModel;

import java.util.List;

/**
 * 系统监控
 * @Author wangdingfeng
 * @Description //TODO
 * @Date 14:40 2019/2/2
 **/

public interface MonitorService {
	/**
	 * 获取在线用户
	 * @return
	 */
	List<UserOnlineModel> list();

	/**
	 * 提出用户
	 * @param sessionId
	 * @return
	 */
	boolean forceLogout(String sessionId);

	/**
	 * 获取服务器信息
	 * @return
	 */
	ServerInfo getServerInfo();
}
