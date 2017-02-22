package com.vmojing.comm;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
/**
 * ���ڴ��ж�ȡͼƬ���߽�ͼƬ���浽�ڴ�
 * @author clyx
 *
 */
public class CacheMemory {
	/**��Lru�㷨����ͼƬ*/
	private LruCache<String, BitmapStatus> lruCache;
	public CacheMemory(LruCache<String, BitmapStatus> lruCache) {
		this.lruCache=lruCache;
	}
	/**
	 * ��ͼƬ���浽�ڴ���
	 * @param key ͼƬ��url
	 * @param bitmap ͼƬ����
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		/**�ж����url��Ӧ��ͼƬ�����Ƿ��Ѿ�����*/
		if(lruCache.get(key)==null){
			BitmapStatus bitmapStatus=new BitmapStatus();
			bitmapStatus.setBitmap(bitmap);
			lruCache.put(key, bitmapStatus);
		}
		else if(lruCache.get(key)!=null&&lruCache.get(key).getBitmap()==null){
			BitmapStatus bitmapStatus=lruCache.get(key);
			bitmapStatus.setBitmap(bitmap);
		}  
	}  
	/**
	 * ���ڴ��ж�ȡͼƬ
	 * @param key ͼƬ��url
	 * @return
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		if(lruCache.get(key)!=null&&lruCache.get(key).getBitmap()!=null)
			return lruCache.get(key).getBitmap();  
		else 
			return null;
	} 

}
