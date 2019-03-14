package com.simple.bets.core.base.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.core.base.service.IService;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.common.util.ReflectUtil;
import com.simple.bets.core.common.util.ToolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Id;
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
    /**
     * 基础类 填充方法名
     */
    protected static final String SET_BASE_DATA = "setBaseData";

    /**
     * 自定义sql 查询属性名
     */
    protected static final String SQL_EXAMPLE = "example";

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
    public int save(T query) {
        //保存公共字段信息
        ReflectUtil.invokeMethod(query, SET_BASE_DATA, true);
        return mapper.insert(query);
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
    public int updateAll(T query) {
        //保存公共字段信息
        ReflectUtil.invokeMethod(query, SET_BASE_DATA, false);
        return mapper.updateByPrimaryKey(query);
    }

    @Override
    @Transactional
    public int update(T query) {
        //保存公共字段信息
        ReflectUtil.invokeMethod(query, SET_BASE_DATA, false);
        return mapper.updateByPrimaryKeySelective(query);
    }

    @Override
    @Transactional
    public int merge(T query){
        Object object = ReflectUtil.getAnnotationValue(query, Id.class);
        if(null == object){
            return this.save(query);
        }else{
            return this.update(query);
        }
    }

    @Override
    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    /**
     * 查询单个对象：如果多条记录则会抛出异
     *
     * @param query
     * @return
     */
    @Override
    public T findByModel(T query) {
        try {
            return this.mapper.selectOne(query);
        } catch (Exception e) {
            logger.error("错误的查询,检查是否返回多个结果集!", e);
            return null;
        }
    }

    @Override
    public T findById(Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> queryObjectForList(String order) {
        PageHelper.orderBy(order);
        return this.mapper.selectAll();
    }

    /**
     * 带条件查询所有
     *
     * @param query
     * @return
     */
    @Override
    public List<T> finList(T query) {
        Example example = installExample(query);
        return this.mapper.selectByExample(example);
    }

    @Override
    public Page<T> queryPage(Page<T> page, T query) {
        PageInfo<T> pageInfo = queryForPage(query, page.getPageNo(), page.getPageSize(), page.getOrderBy());
        page.setCount(pageInfo.getTotal());
        page.setList(pageInfo.getList());
        return page;
    }

    /**
     * 列表查询，根据指定的字段倒序排序
     *
     * @param query      查询对象
     * @param orderField 排序字段
     * @return 分页对象
     */
    @Override
    public PageInfo<T> queryForPage(T query, int pageNum, int pageSize, String orderField) {
        query = ToolUtils.setNullValue(query);
        Example example = installExample(query);
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy(orderField);
        List<T> result = mapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<T>(result, pageNum);
        return pageInfo;
    }

    /**
     * 封装Example 数据
     *
     * @param query
     * @return
     */
    protected Example installExample(T query) {
        Example example = (Example) ReflectUtil.getFieldValue(query, SQL_EXAMPLE);
        if (null == example) example = new Example(query.getClass());
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtil.isNotEmpty(example.getOredCriteria())) {
            criteria = example.getOredCriteria().get(0);
        }
        criteria.andEqualTo(query);
        return example;
    }

}
