package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.Office;
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
public interface OfficeMapper extends BaseMapper<Office> {
    /**
     * 查询所有的数据
     * @return
     */
    List<Office> findAllList(Office office);
    /**
     * 查询是否有子节点
     * @param parentId
     * @return
     */
    List<Office>  findSubOfficeListByPid(@Param("parentId") Long parentId);
}
