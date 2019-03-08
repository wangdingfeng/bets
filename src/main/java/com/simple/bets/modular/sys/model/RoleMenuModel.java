package com.simple.bets.modular.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色菜单表
 */
@Table(name = "sys_role_menu")
public class RoleMenuModel implements Serializable {
	
	private static final long serialVersionUID = -7573904024872252113L;

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long id;

	@Column(name = "role_id")
    private Long roleId;

    @Column(name = "menu_id")
    private Long menuId;

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
     * @return MENU_ID
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * @param menuId
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}