package com.vmojing.base;

import java.io.Serializable;


@SuppressWarnings("serial")
public class User implements Serializable{
	/**用户id*/
	private String id;
	/**用户昵称*/
	private String name;
	//TODO:
	/**用户头像*/
	private String headUrl;
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
