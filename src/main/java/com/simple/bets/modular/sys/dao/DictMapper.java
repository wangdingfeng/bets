package com.simple.bets.modular.sys.dao;


import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.DictModel;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description 字典管理 dao
 * @Date 15:57 2019/1/14
 **/

public interface DictMapper extends BaseMapper<DictModel> {

    /**
     * 查询所有子字典项
     * @return
     */
   List<DictModel> selectAllChildrenDict();
}