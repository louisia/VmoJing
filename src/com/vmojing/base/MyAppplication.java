package com.vmojing.base;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.support.v4.util.LruCache;

import com.vmojing.comm.BitmapStatus;

public class MyAppplication extends Application{
	/**�û���¼������*/
	private String username;
	/**activity�б�*/
	private List<Activity> mList = new LinkedList<Activity>();
	/**ͼƬ�������*/
	private LruCache<String,BitmapStatus>mLruCache;
	/**����ʱ��*/
	
	public LruCache<String, BitmapStatus> getmLruCache() {
		return mLruCache;
	}
	public void setmLruCache(LruCache<String, BitmapStatus> mLruCache) {
		this.mLruCache = mLruCache;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public void onCreate() {
		int cacheSize = (int) Runtime.getRuntime().maxMemory() / 10;
		/**��д�����ж���Ĵ�С*/
		mLruCache = new LruCache<String, BitmapStatus>(cacheSize) {  
			@Override  
			protected int sizeOf(String key, BitmapStatus bitmapStatus) {
				if(bitmapStatus.getBitmap()!=null)
					return bitmapStatus.getBitmap().getByteCount();
				return 0;
			}  
		}; 
		super.onCreate();
	}
	/**
	 * ���һ��activity
	 * @param activity
	 */
	public void addActivity(Activity activity) {   
		mList.add(activity);   
	}   
	/**
	 * �ر�����activity  
	 */
	public void exit() {   
		try {   
			for (Activity activity:mList) {   
				if (activity != null)   
					activity.finish();   
			}   
		} catch (Exception e) {   
			e.printStackTrace();   
		} finally {   
			System.exit(0);   
		}   
	}   
}
