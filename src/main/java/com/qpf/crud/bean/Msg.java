package com.qpf.crud.bean;

import java.util.HashMap;
import java.util.Map;

public class Msg {
	/** 100 - 成功 \n200 - 失败 **/
	private String code;
	/** 描述 ***/
	private String desc;
	/** 绑定数据  **/
	Map<String, Object> data = new HashMap<String, Object>();

	public static Msg success() {
		return new Msg("100", "成功");
	}
	public static Msg fail() {
		return new Msg("200", "失败");
	}
	public Msg desc(String desc) {
		this.desc = desc;
		return this;
	}
	// 链式绑定数据
	public Msg add(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
	public Msg() {}
	public Msg(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
