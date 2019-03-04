package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.modular.sys.service.DictService;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Author wangdingfeng
 * @Description 字典信息
 * @Date 17:39 2019/1/14
 **/

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<Dict> implements DictService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional
	public void addDict(Dict dict) {
		this.save(dict);
	}

	@Override
	@Transactional
	public void deleteDicts(String dictIds) {
		List<String> list = Arrays.asList(dictIds.split(","));
		this.batchDelete(list, "dictId", Dict.class);
	}

	@Override
	@Transactional
	public void updateDict(Dict dict) {
		this.updateNotNull(dict);
	}

}
