package com.vmojing.clue;

import java.io.Serializable;

import com.vmojing.base.Weibo;

import android.widget.CheckBox;

@SuppressWarnings("serial")
public class Clue implements Serializable{
	/**线索的id*/
	private String id;
	/**线索的微博内容*/
	private Weibo weibo;   
	/**线索的状态，0--normal,1--abnormal*/
	private int status;
	
	private Long monitorAt;
	
	private int visible=CheckBox.GONE;
	private String check="false";
	
	public void setMonitorAt(Long monitorAt) {
		this.monitorAt = monitorAt;
	}
	public Long getMonitorAt() {
		return monitorAt;
	}
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
