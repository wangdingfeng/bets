package com.simple.bets.modular.sys.model;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_role_menu")
public class RoleMenu implements Serializable {
	
	private static final long serialVersionUID = -7573904024872252113L;

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
}