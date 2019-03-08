package com.simple.bets.modular.sys.service;

import com.simple.bets.core.base.model.Tree;
import com.simple.bets.core.base.service.IService;
import com.simple.bets.modular.sys.model.OfficeModel;

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
public interface OfficeService extends IService<OfficeModel> {
    /**
     * 查询所有的数据
     * @return
     */
    List<OfficeModel> findAllList(OfficeModel office);

    /**
     * 获取部门的树结构
     * @return
     */
    Tree<OfficeModel> getAllOfficeTree(OfficeModel office);

    /**
     * 保存更新
     * @param office
     */
    void saveOrUpdate(OfficeModel office);
}
