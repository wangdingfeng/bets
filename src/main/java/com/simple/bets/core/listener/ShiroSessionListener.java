package com.simple.bets.core.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author wangdingfeng
 * @Description //shiro Session监听
 * @Date 11:04 2019/1/11
 **/

public class ShiroSessionListener implements SessionListener{

	private final AtomicInteger sessionCount = new AtomicInteger(0);
	
	@Override
	public void onStart(Session session) {
		sessionCount.incrementAndGet();
	}

	@Override
	public void onStop(Session session) {
		sessionCount.decrementAndGet();
		
	}

	@Override
	public void onExpiration(Session session) {
		sessionCount.decrementAndGet();
	}
}
