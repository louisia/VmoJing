package com.vmojing.base;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Weibo implements Serializable{
	/**΢��id*/
	private String id;
	/**΢������*/
	private String content;
	/**΢��ʱ��*/
	private Date time;
	/**΢����ͼƬ*/
	String []contenturl;
	/**΢����ת����*/
	private int retweet;
	/**΢����������*/
	private int comment;
	/**΢������*/
	private int attitude;
	/**ת����΢��*/
	private Weibo weibo;
	/**΢������ �û���Ϣ*/
	private User user;
	
	public void setContenturl(String[] contenturl) {
		this.contenturl = contenturl;
	}
	public String[] getContenturl() {
		return contenturl;
	}

	public int getAttitude() {
		return attitude;
	}
	public void setAttitude(int attitude) {
		this.attitude = attitude;
	}
	public int getRetweet() {
		return retweet;
	}
	public void setRetweet(int retweet) {
		this.retweet = retweet;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Weibo getWeibo() {
		return weibo;
	}
	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
