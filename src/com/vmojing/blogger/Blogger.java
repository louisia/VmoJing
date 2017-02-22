package com.vmojing.blogger;

import java.io.Serializable;
import java.util.Date;

import com.vmojing.base.User;

import android.widget.CheckBox;
@SuppressWarnings("serial")
public class Blogger implements Serializable{
	/**博主的id*/
	private String id;
	/**博主的昵称和头像*/
	private User user;
	/**博主的监测时间*/
	private Date monitoredTime;
	/**博主的状态：0--正常博主，1--异常博主*/
	private int status;        
	/**博主的粉丝数*/
	private int followersCount;     
	/**博主的好友数*/
	private int friendsCount;       
	/**博主的微博数*/
	private int weiboCount;
	private int visible=CheckBox.GONE;
	private String check="false";
	
	private Long monitorAt;
	public Long getMonitorAt() {
		return monitorAt;
	}
	public void setMonitorAt(Long monitorAt) {
		this.monitorAt = monitorAt;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String string) {
		this.id = string;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public Date getMonitoredTime() {
		return monitoredTime;
	}
	public void setMonitoredTime(Date monitoredTime) {
		this.monitoredTime = monitoredTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public void setWeiboCount(int weiboCount) {
		this.weiboCount = weiboCount;
	}
	public int getWeiboCount() {
		return weiboCount;
	}
	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}


}
