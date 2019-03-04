package com.simple.bets.modular.sys.dao;


import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.core.base.BaseMapper;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description 字典管理 dao
 * @Date 15:57 2019/1/14
 **/

public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 查询所有子字典项
     * @return
     */
   List<Dict> selectAllChildrenDict();
}