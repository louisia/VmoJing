package com.vmojing.publics;

import com.vmojing.base.Weibo;

public class Publics {
	/**�����ȵ�id*/
	private String id;
	/**�����ȵ�΢��*/
	private Weibo weibo;
	/** ΢��Ԥ��ת����*/
	private int repostCount;
	/**΢��Ԥ��������*/
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
