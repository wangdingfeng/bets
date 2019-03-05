package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.modular.sys.service.DictService;
import com.simple.bets.core.base.service.impl.ServiceImpl;
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

	@Override
	@Transactional
	public void saveOrUpdate(Dict dict) {
		if(null == dict.getDictId()){
			dict.setBaseData(true);
			super.save(dict);
		}else{
			dict.setBaseData(false);
			super.updateNotNull(dict);
		}
	}

	@Override
	@Transactional
	public void deleteDicts(String dictIds) {
		List<String> list = Arrays.asList(dictIds.split(","));
		this.batchDelete(list, "dictId", Dict.class);
	}

}
