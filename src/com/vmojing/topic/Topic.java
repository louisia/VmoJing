package com.vmojing.topic;

import java.io.Serializable;
import java.util.Date;

import android.widget.CheckBox;
@SuppressWarnings("serial")
public class Topic implements Serializable{
	/**话题的id*/
	private String id;
	/**话题名称*/
	private String name;
	/**话题类型0--领导话题,1--部门话题,2--其他话题*/
	private int type;//
	/**话题状态*/
	private int status;//0--正常话题,1--异常话题
	/**话题今日微博数*/
	private int todayCount;
	/**话题微博总数*/
	private int totalCount;
	/**话题创建时间*/
	private Date createTime;
	
	private Long monitorAt;
	
	
	
	private int visible=CheckBox.GONE;
	private String check="false";

	public Topic() {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(int todayCount) {
		this.todayCount = todayCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	public void setMonitorAt(Long monitorAt) {
		this.monitorAt = monitorAt;
	}
	public Long getMonitorAt() {
		return monitorAt;
	}

}
