package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.OfficeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.simple.bets.modular.sys.dao
 * @ClassName: OfficeMapper
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/8 12:03
 * @Version: 1.0
 */
@Repository
public interface OfficeMapper extends BaseMapper<OfficeModel> {
    /**
     * 查询所有的数据
     * @return
     */
    List<OfficeModel> findAllList(OfficeModel office);
    /**
     * 查询是否有子节点
     * @param parentId
     * @return
     */
    List<OfficeModel>  findSubOfficeListByPid(@Param("parentId") Long parentId);
}
