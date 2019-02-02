package com.simple.bets.core.beetl.fn;

import com.simple.bets.core.common.lang.ObjectUtils;
import com.simple.bets.core.common.util.CookieUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookie implements Function {

	@Override
	public Object call(Object[] paras, Context ctx) {
		String key = ObjectUtils.toString(paras.length >= 1 ? paras[0] : null);
		boolean isRemove = paras.length >= 2 ? ObjectUtils.toBoolean(paras[1]) : false;
		String defValue = ObjectUtils.toString(paras.length >= 3 ? paras[2] : null);

		HttpServletRequest request = (HttpServletRequest) ctx.getGlobal("request");
		HttpServletResponse response = (HttpServletResponse) ctx.getGlobal("response");
		
		String cookie = CookieUtils.getCookie(request, response, key, isRemove);
		return cookie != null ? cookie : defValue;
	}

}
