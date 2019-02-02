package com.simple.bets.core.service;

import com.simple.bets.core.common.util.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用Service封装
 *
 * @param <T>
 * @author wangdingfeng
 * @Date 2019-01-07
 */
public interface IService<T> {
    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> selectAll();


    /**
     * 通过主键查询
     *
     * @param key 主键
     * @return
     */
    T selectByKey(Object key);

    /**
     * 保存数据
     *
     * @param entity
     * @return
     */
    int save(T entity);

    /**
     * 通过主键删除
     *
     * @param key 主键
     * @return
     */
    int delete(Object key);

    /**
     * 批量删除
     *
     * @param list     参数集合
     * @param property 指定列
     * @param clazz    删除的class
     * @return
     */
    int batchDelete(List<String> list, String property, Class<T> clazz);

    /**
     * 更新所有的数据
     *
     * @param entity
     * @return
     */
    int updateAll(T entity);

    /**
     * 更新非空数据
     *
     * @param entity
     * @return
     */
    int updateNotNull(T entity);

    /**
     * Example 查询
     *
     * @param example
     * @return
     */
    List<T> selectByExample(Object example);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    T findById(Object id);

    /**
     * 查询单个对象：如果多条记录则会抛出异常
     *
     * @param entity
     * @return
     */
    T findByObject(T entity);

    /**
     * 排序查询
     *
     * @param order 排序字段
     * @return
     */
    List<T> queryObjectForList(String order);

    /**
     * 带条件查询所有数据
     * @param entity 条件
     * @return
     */
    List<T> queryObjectForList(T entity);

    /**
     * 分页查询
     * @param page
     * @param entity
     * @return
     */
    Page<T> queryPage(Page<T> page, T entity);
}