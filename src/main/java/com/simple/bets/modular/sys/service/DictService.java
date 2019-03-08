package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.DictModel;
import com.simple.bets.core.base.service.IService;

public interface DictService extends IService<DictModel> {
    /**
     * 保存or更新字典
     * @param dict
     */
    void saveOrUpdate(DictModel dict);
}
