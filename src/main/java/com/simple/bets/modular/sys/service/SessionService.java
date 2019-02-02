package com.simple.bets.modular.sys.service;


import com.simple.bets.modular.sys.model.UserOnline;

import java.util.List;

/**
 * session 管理
 * @Author wangdingfeng
 * @Description //TODO
 * @Date 14:40 2019/2/2
 **/

public interface SessionService {
	/**
	 * 获取在线用户
	 * @return
	 */
	List<UserOnline> list();

	/**
	 * 提出用户
	 * @param sessionId
	 * @return
	 */
	boolean forceLogout(String sessionId);
}
