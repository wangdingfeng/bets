package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.core.service.IService;

public interface DictService extends IService<Dict> {

    void addDict(Dict dict);

    void deleteDicts(String dictIds);

    void updateDict(Dict dicdt);
}
