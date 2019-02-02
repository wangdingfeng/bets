package com.simple.bets.core.mapper;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * @Author wangdingfeng
 * @Description 基本mapper
 * @Date 11:04 2019/1/11
 **/

@Repository
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
	
}