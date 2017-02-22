package com.vmojing.base;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Weibo implements Serializable{
	/**微博id*/
	private String id;
	/**微博内容*/
	private String content;
	/**微博时间*/
	private Date time;
	/**微博中图片*/
	String []contenturl;
	/**微博的转发数*/
	private int retweet;
	/**微博的评论数*/
	private int comment;
	/**微博赞数*/
	private int attitude;
	/**转发子微博*/
	private Weibo weibo;
	/**微博作者 用户信息*/
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
