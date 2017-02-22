package com.vmojing.feeling;

import java.io.Serializable;

import com.vmojing.base.Weibo;

@SuppressWarnings("serial")
public class Feeling implements Serializable{
	/**舆情id*/
	private String id;
	/**舆情微博*/
	private Weibo weibo;
	/**舆情状态，0--已处理舆情，1--已忽略舆情，2--待处理舆情*/
	private int status;
	/**时间戳*/
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
