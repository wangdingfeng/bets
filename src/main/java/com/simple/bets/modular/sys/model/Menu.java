package com.simple.bets.modular.sys.model;


import com.simple.bets.core.annotation.ExportConfig;
import com.simple.bets.core.model.TreeModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_menu")
public class Menu extends TreeModel {

	private static final long serialVersionUID = 7187628714679791771L;

	public static final String TYPE_MENU = "0";

	public static final String TYPE_BUTTON = "1";

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@ExportConfig(value = "编号")
	private Long id;

	@Column(name = "menu_name")
	@ExportConfig(value = "名称")
	private String menuName;

	@Column(name = "url")
	@ExportConfig(value = "地址")
	private String url;

	@Column(name = "perms")
	@ExportConfig(value = "权限标识")
	private String perms;

	@Column(name = "icon")
	@ExportConfig(value = "图标")
	private String icon;

	@Column(name = "type")
	@ExportConfig(value = "类型", convert = "s:0=菜单,1=按钮")
	private String type;


	public Menu(){

	};

	public Menu(Long menuId){
		this.setId(menuId);
	};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return MENU_NAME
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName == null ? "" : menuName.trim();
	}

	/**
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url == null ? "" : url.trim();
	}

	/**
	 * @return PERMS
	 */
	public String getPerms() {
		return perms;
	}

	/**
	 * @param perms
	 */
	public void setPerms(String perms) {
		this.perms = perms == null ? "" : perms.trim();
	}

	/**
	 * @return ICON
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon == null ? "" : icon.trim();
	}

	/**
	 * @return TYPE
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type == null ? "" : type.trim();
	}
}