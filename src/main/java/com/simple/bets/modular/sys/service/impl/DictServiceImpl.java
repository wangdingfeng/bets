package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.model.DictModel;
import com.simple.bets.modular.sys.service.DictService;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author wangdingfeng
 * @Description 字典信息
 * @Date 17:39 2019/1/14
 **/

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictModel> implements DictService {

	@Override
	@Transactional
	public void saveOrUpdate(DictModel dict) {
		super.merge(dict);
		//清除字典缓存
		DictUtils.clearCacheDict();
	}

}
