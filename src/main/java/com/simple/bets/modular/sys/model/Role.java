package com.simple.bets.modular.sys.model;


import com.simple.bets.core.base.model.BaseModel;

import javax.persistence.*;
import java.util.List;

@Table(name = "t_role")
public class Role extends BaseModel {

	private static final long serialVersionUID = -1714476694755654924L;

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "dept_id")
	private Long deptId;

	@Column(name = "dept_name")
	private String deptName;

	/**
	 * 角色拥有的菜单
	 */
	@Transient
	private String menuIds;
	/**
	 * 角色下对应的用户
	 */
	@Transient
	private List<Long> userIds;

	/**
	 * @return ROLE_ID
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return ROLE_NAME
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}