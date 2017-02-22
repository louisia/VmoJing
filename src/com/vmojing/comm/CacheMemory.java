package com.vmojing.comm;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
/**
 * 从内存中读取图片或者将图片保存到内存
 * @author clyx
 *
 */
public class CacheMemory {
	/**用Lru算法保存图片*/
	private LruCache<String, BitmapStatus> lruCache;
	public CacheMemory(LruCache<String, BitmapStatus> lruCache) {
		this.lruCache=lruCache;
	}
	/**
	 * 将图片保存到内存中
	 * @param key 图片的url
	 * @param bitmap 图片对象
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		/**判断这个url对应的图片缓存是否已经存在*/
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
	 * 从内存中读取图片
	 * @param key 图片的url
	 * @return
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		if(lruCache.get(key)!=null&&lruCache.get(key).getBitmap()!=null)
			return lruCache.get(key).getBitmap();  
		else 
			return null;
	} 

}
