package com.simple.bets.core.model;

import java.util.HashMap;

public class ResponseResult extends HashMap<String, Object> {

	private static final long serialVersionUID = -8713837118340960775L;

	// 成功
	private static final Integer SUCCESS = 0;
	// 警告
	private static final Integer WARN = 1;
	// 异常 失败
	private static final Integer FAIL = 500;

	public ResponseResult(String msg) {
		put("success", true);
		put("code", SUCCESS);
		put("message", msg);
	}
	public ResponseResult() {
	}

	public static ResponseResult error(Object msg) {
		ResponseResult responseBo = new ResponseResult();
		responseBo.put("success", false);
		responseBo.put("code", FAIL);
		responseBo.put("message", msg);
		return responseBo;
	}

	public static ResponseResult warn(Object msg) {
		ResponseResult responseBo = new ResponseResult();
		responseBo.put("success", false);
		responseBo.put("code", WARN);
		responseBo.put("message", msg);
		return responseBo;
	}

	public static ResponseResult ok(Object data) {
		ResponseResult responseBo = new ResponseResult();
		responseBo.put("success", true);
		responseBo.put("code", SUCCESS);
		responseBo.put("message", "获取成功");
		responseBo.put("data", data);
		return responseBo;
	}

	public static ResponseResult ok() {
		return new ResponseResult();
	}
	public static ResponseResult ok(String msg) {
		return new ResponseResult(msg);
	}

	public static ResponseResult error() {
		return ResponseResult.error("");
	}

	@Override
	public ResponseResult put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
