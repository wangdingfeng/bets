package com.simple.bets.core.beetl.fn;

import com.simple.bets.core.common.lang.ObjectUtils;
import com.simple.bets.core.common.lang.StringUtils;
import com.simple.bets.modular.sys.utils.UserUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * @Author wangdingfeng
 * @Description  自定义beetl shiro权限判断方法
 * @Date 15:34 2019/3/9
 **/

public class HasPermi implements Function {

	@Override
	public Object call(Object[] paras, Context ctx) {
		// TODO Auto-generated method stub
		String permissions = ObjectUtils.toString(paras[0]);
		if (StringUtils.isBlank(permissions)) {
			return false;
		} else {
			boolean option = true;// || true;   &&false
			if(paras.length >= 2) {
				option = (Boolean) paras[1];
			}
			boolean[] bArr = UserUtils.getSubject().isPermitted(StringUtils.split(permissions, ","));
			return option?BooleanUtils.or(bArr):Boolean.valueOf(BooleanUtils.and(bArr));
		}
	}

}
