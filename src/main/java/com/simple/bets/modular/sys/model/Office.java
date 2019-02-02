package com.simple.bets.modular.sys.model;

import com.simple.bets.core.model.TreeModel;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import java.util.List;

/**
 * 机构model
 ** @author wangdingfeng
 * @version 2018-11-15
 */
@Table(name = "sys_office")
public class Office extends TreeModel {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	protected String name;	// 名称
	@Column(name = "code")
	private String code; 	// 机构编码
	@Column(name = "type")
	private String type; 	// 机构类型（1：公司；2：部门；3：小组）
	@Column(name = "grade")
	private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	@Column(name = "address")
	private String address; // 联系地址
	@Column(name = "zip_code")
	private String zipCode; // 邮政编码
	@Column(name = "master")
	private String master; 	// 负责人
	@Column(name = "phone")
	private String phone; 	// 电话
	@Column(name = "fax")
	private String fax; 	// 传真
	@Column(name = "email")
	private String email; 	// 邮箱
	@Column(name = "PRIMARY_PERSON")
	private String primaryPerson;//主负责人
	@Column(name = "DEPUTY_PERSON")
	private String deputyPerson;//副负责人
	@Column(name = "area_id")
	private String areaId;
	@Transient
	private String areaName;		//区域名称

	@Transient
	private List<String> childDeptList;//快速添加子部门

	public Office(Long id){
		setId(id);
	}
	public Office(){
		setId(id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(String primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public String getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(String deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=1)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}