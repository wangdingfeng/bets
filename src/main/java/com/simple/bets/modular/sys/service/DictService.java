package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.core.base.service.IService;

public interface DictService extends IService<Dict> {
    /**
     * 保存or更新字典
     * @param dict
     */
    void saveOrUpdate(Dict dict);
}
