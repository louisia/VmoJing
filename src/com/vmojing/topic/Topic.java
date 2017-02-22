package com.vmojing.topic;

import java.io.Serializable;
import java.util.Date;

import android.widget.CheckBox;
@SuppressWarnings("serial")
public class Topic implements Serializable{
	/**�����id*/
	private String id;
	/**��������*/
	private String name;
	/**��������0--�쵼����,1--���Ż���,2--��������*/
	private int type;//
	/**����״̬*/
	private int status;//0--��������,1--�쳣����
	/**�������΢����*/
	private int todayCount;
	/**����΢������*/
	private int totalCount;
	/**���ⴴ��ʱ��*/
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
