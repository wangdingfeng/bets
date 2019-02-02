package com.simple.bets.modular.sys.service;

import com.simple.bets.core.model.Tree;
import com.simple.bets.core.service.IService;
import com.simple.bets.modular.sys.model.Office;

import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.simple.bets.modular.sys.service
 * @ClassName: OfficeService
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/8 14:35
 * @Version: 1.0
 */
public interface OfficeService extends IService<Office> {
    /**
     * 查询所有的数据
     * @return
     */
    List<Office> findAllList(Office office);

    /**
     * 获取部门的树结构
     * @return
     */
    Tree<Office> getAllOfficeTree(Office office);
}
