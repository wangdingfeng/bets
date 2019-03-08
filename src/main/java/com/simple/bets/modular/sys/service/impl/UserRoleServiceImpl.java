package com.simple.bets.modular.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.common.util.ToolUtils;
import com.simple.bets.modular.sys.dao.UserRoleMapper;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserRoleModel;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * 用户角色表
 */
@Service("userRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleModel> implements UserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Override
	public Page<UserModel> queryPage(Page<UserModel> page, UserModel user, Boolean exist) {
		PageHelper.startPage(page.getPageNo(),page.getPageSize());
		PageHelper.orderBy(page.getOrderBy());
		user = ToolUtils.setNullValue(user);
		List<UserModel> entityList = null;
		if(exist){
			//查询存在的用户
			entityList = userRoleMapper.findUsersByRole(user);
		}else{
			//查询不存在的用户
			entityList = userRoleMapper.findNotUsersByRole(user);
		}
		PageInfo<UserModel> pageInfo = new PageInfo<>(entityList);
		page.setCount(pageInfo.getTotal());
		page.setList(pageInfo.getList());
		return page;
	}

	@Override
	public void assignRoleUser(RoleModel role) {
		List<Long> userIds = role.getUserIds();
		userIds.forEach(aLong -> {
			UserRoleModel userRole = new UserRoleModel();
			userRole.setRoleId(role.getRoleId());
			userRole.setUserId(aLong);
			super.save(userRole);
		});
	}

	@Override
	@Transactional
	public void deleteAllUserRolesByRoleId(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		this.batchDelete(list, "roleId", UserRoleModel.class);
	}
	@Override
	@Transactional
	public void deleteUserRolesByRoleId(RoleModel role) {
		Example example = new Example(UserRoleModel.class);
		example.createCriteria().andEqualTo("roleId",role.getRoleId()).andIn("userId", role.getUserIds());
		userRoleMapper.deleteByExample(example);
	}

	@Override
	@Transactional
	public void deleteUserRolesByUserId(String userIds) {
		List<String> list = Arrays.asList(userIds.split(","));
		this.batchDelete(list, "userId", UserRoleModel.class);
	}

}
