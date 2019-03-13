package com.simple.bets.modular.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志表
 */
@Table(name = "sys_log")
public class LogModel implements Serializable {

	private static final long serialVersionUID = -8878596941954995444L;

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "operation")
	private String operation;

	@Column(name = "time")
	private Long time;

	@Column(name = "method")
	private String method;

	@Column(name = "params")
	private String params;

	@Column(name = "ip")
	private String ip;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "location")
	private String location;

	// 用于搜索条件中的时间字段
	@Transient
	private String timeField;

	@JsonIgnore
	@Transient
	protected Object example;

	/**
	 * @return ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return USERNAME
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	/**
	 * @return OPERATION
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation == null ? null : operation.trim();
	}

	/**
	 * @return TIME
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * @return METHOD
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method == null ? null : method.trim();
	}

	/**
	 * @return PARAMS
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 */
	public void setParams(String params) {
		this.params = params == null ? null : params.trim();
	}

	/**
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	/**
	 * @return CREATE_TIME
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTimeField() {
		return timeField;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}
}