package com.vmojing.clue;

import java.io.Serializable;
import java.util.Date;

import com.vmojing.base.User;

@SuppressWarnings("serial")
public class ComRet implements Serializable{
	/**评论转发的id*/
	private String id;
	/** 评论转发的用户 */
	private User user;
	/** 评论转发的内容 */
	private String content;
	/** 评论转发的时间*/
	private Date createTime;
	/**评论或转发,评论0、转发1*/
	private Long replyCommentId;
	/**评论或转发舆情,负面0、中立1、正面2*/
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
