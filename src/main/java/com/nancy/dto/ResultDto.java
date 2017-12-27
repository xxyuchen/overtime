package com.nancy.dto;

import java.io.Serializable;
import java.util.Map;

public class ResultDto implements Serializable {
	public static final int NO_LOGIN = 401;    //未登录
	public static final int CODE_FAIL = 500;  // 操作失败
	public static final int CODE_SUCC = 200; //操作成功
	public static final String MESS_SUCC = "操作成功";
	public static final String MESS_FAIL= "操作失败";

	//结果码
	private int code=CODE_SUCC;
	//结果信息
	private	String msg;
	private Map<String, Object>  data;
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
