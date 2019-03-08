package com.simple.bets.modular.sys.model;

import com.simple.bets.core.base.model.TreeModel;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;

/**
 * 机构model
 ** @author wangdingfeng
 * @version 2018-11-15
 */
@Table(name = "sys_office")
public class OfficeModel extends TreeModel {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;	// 名称
	@Column(name = "code")
	private String code; 	// 机构编码
	@Column(name = "type")
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	@Column(name = "address")
	private String address; // 联系地址
	@Column(name = "master")
	private String master; 	// 负责人
	@Column(name = "phone")
	private String phone; 	// 电话
	@Column(name = "email")
	private String email; 	// 邮箱

	public OfficeModel(Long id){
		setId(id);
	}
	public OfficeModel(){
		setId(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}