package com.simple.bets.core.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.core.base.service.IService;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.common.util.ToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 通用Service接口
 *
 * @param <T>
 * @author wangdingfeng
 * @Date 2019-01-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class ServiceImpl<T> implements IService<T> {

    protected Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    protected BaseMapper<T> mapper;

    public Mapper<T> getMapper() {
        return mapper;
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public T selectByKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int save(T entity) {
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public int delete(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int batchDelete(List<String> list, String property, Class<T> clazz) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, list);
        return this.mapper.deleteByExample(example);
    }

    @Override
    @Transactional
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    /**
     * 查询单个对象：如果多条记录则会抛出异
     * @param entity
     * @return
     */
    @Override
    public T findByObject(T entity) {
        try {
            return this.mapper.selectOne(entity);
        } catch (Exception e) {
            logger.error("错误的查询,检查是否返回多个结果集!", e);
        }
        return null;
    }

    @Override
    public T findById(Object id){
        return this.mapper.selectByPrimaryKey(id);
    }


    public List<T> queryObjectForList(String order) {
        PageHelper.orderBy(order);
        return this.mapper.selectAll();
    }

    /**
     * 带条件查询所有
     *
     * @param entity
     * @return
     */
    public List<T> queryObjectForList(T entity) {
        return this.mapper.select(entity);
    }

    public Page<T> queryPage(Page<T> page, T entity){
        PageHelper.startPage(page.getPageNo(),page.getPageSize());
        PageHelper.orderBy(page.getOrderBy());
        entity = ToolUtils.setNullValue(entity);
        List<T> entityList = this.mapper.select(entity);
        PageInfo<T> pageInfo = new PageInfo<>(entityList);
        page.setCount(pageInfo.getTotal());
        page.setList(pageInfo.getList());
        return page;
    }
}
