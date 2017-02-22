package com.vmojing.publics;

import com.vmojing.base.Weibo;

public class Publics {
	/**公共热点id*/
	private String id;
	/**公共热点微博*/
	private Weibo weibo;
	/** 微博预测转发数*/
	private int repostCount;
	/**微博预测评论数*/
	private int commentCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Weibo getWeibo() {
		return weibo;
	}
	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}
	public int getRepostCount() {
		return repostCount;
	}
	public void setRepostCount(int repostCount) {
		this.repostCount = repostCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
