package com.simple.bets.core.beetl.ext;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置项目中常用到的路径
 */
public class GlobalExt implements WebRenderExt {
	
	@Override
	public void modify(Template template, GroupTemplate arg1, HttpServletRequest request, HttpServletResponse response) {
		String ctxPath = request.getContextPath();
		template.binding("ctxPath", ctxPath);
		//静态文件路径
		template.binding("ctxStatic", (ctxPath + "/static"));
	}

}
