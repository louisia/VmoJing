package com.vmojing.feeling;

import java.io.Serializable;

import com.vmojing.base.Weibo;

@SuppressWarnings("serial")
public class Feeling implements Serializable{
	/**����id*/
	private String id;
	/**����΢��*/
	private Weibo weibo;
	/**����״̬��0--�Ѵ������飬1--�Ѻ������飬2--����������*/
	private int status;
	/**ʱ���*/
	private Long timeStamp;
	public String getId() {
		return id;
	}
	public void setId(String string) {
		this.id = string;
	}
	public Weibo getWeibo() {
		return weibo;
	}
	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
}
