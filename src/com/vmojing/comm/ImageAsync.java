package com.vmojing.comm;

import com.vmojing.base.MyAppplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageAsync extends AsyncTask<String, Void, Bitmap>{
	/**上下文对象*/
	private Context context;
	/**图片显示控件*/
	private ImageView imageView;   

	public ImageAsync(Context context, ImageView imageView) {
		this.context=context;
		this.imageView=imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		LruCache<String, BitmapStatus> lruCache;//图片内存缓存
		lruCache=((MyAppplication)context.getApplicationContext()).getmLruCache();
		/**从SD卡读取图片*/
		Bitmap bitmap=CacheFile.getBitmapFromFileCache(params[0]);
		CacheMemory mc=new CacheMemory(lruCache);
		/**将从SD卡读取的图片保存到内存中*/
		if(bitmap!=null)
			mc.addBitmapToMemoryCache(params[0], bitmap);
		else{
			/**从网络获取图片*/
			byte[] bytes=HttpComm.getNetworkByte(params[0]);
			/**判断是否成功从网络获取图片*/
			if(bytes!=null){
				bitmap=ImageUtil.compressBitmap(bytes, 180,180);
				/**获取到图片后将图片保存到SD卡和内存中*/
				if(bitmap!=null){
					mc.addBitmapToMemoryCache(params[0], bitmap);
					CacheFile.addBitmapToFileCache(params[0], bitmap);
				}
			}
			else{/**获取不到图片时,从缓存中删除这个图片对应的url*/
				lruCache.remove(params[0]);
			}
		}
		return bitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		/**在ImageView控件上显示图片*/
		if(result!=null)
			imageView.setImageBitmap(result);
	}

}
