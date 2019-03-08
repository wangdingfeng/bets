package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.MenuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends BaseMapper<MenuModel> {
	
	List<MenuModel> findUserPermissions(String userName);
	
	List<MenuModel> findUserMenus(String userName);
	
	// 删除父节点，子节点变成顶级节点（根据实际业务调整）
	void changeToTop(List<String> menuIds);

	/**
	 * 查询是否有子节点
	 * @param parentId
	 * @return
	 */
	List<MenuModel> findSubMenuListByPid(@Param("parentId") Long parentId);

	/**
	 * 保存排序
	 * @param menu
	 */
	void updateSort(MenuModel menu);
}