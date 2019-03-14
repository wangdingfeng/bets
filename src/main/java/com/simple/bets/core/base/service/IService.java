package com.simple.bets.core.base.service;

import com.github.pagehelper.PageInfo;
import com.simple.bets.core.common.util.Page;

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
     * @param query
     * @return
     */
    int save(T query);

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
     * @param query
     * @return
     */
    int updateAll(T query);

    /**
     * 更新非空数据
     *
     * @param query
     * @return
     */
    int update(T query);

    /**
     * 保存更新实体
     * @param query
     * @return
     */
    int merge(T query);

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
     * @param query
     * @return
     */
    T findByModel(T query);

    /**
     * 排序查询
     *
     * @param order 排序字段
     * @return
     */
    List<T> queryObjectForList(String order);

    /**
     * 带条件查询所有数据
     * @param query 条件
     * @return
     */
    List<T> finList(T query);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    Page<T> queryPage(Page<T> page, T query);

    /**
     * 分页插件分页
     * @param query 对象
     * @param pageNum 当前页
     * @param pageSize 数量
     * @param orderField 排序字段
     * @return
     */
    PageInfo<T> queryForPage(T query, int pageNum, int pageSize, String orderField);
}