package com.vmojing.clue;

import java.io.Serializable;
import java.util.Date;

import com.vmojing.base.User;

@SuppressWarnings("serial")
public class ComRet implements Serializable{
	/**����ת����id*/
	private String id;
	/** ����ת�����û� */
	private User user;
	/** ����ת�������� */
	private String content;
	/** ����ת����ʱ��*/
	private Date createTime;
	/**���ۻ�ת��,����0��ת��1*/
	private Long replyCommentId;
	/**���ۻ�ת������,����0������1������2*/
	private int emotion;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getEmotion() {
		return emotion;
	}
	public void setEmotion(int emotion) {
		this.emotion = emotion;
	}
	public void setReplyCommentId(Long replyCommentId) {
		this.replyCommentId = replyCommentId;
	}
	public Long getReplyCommentId() {
		return replyCommentId;
	}
}
