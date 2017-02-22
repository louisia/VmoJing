package com.vmojing.comm;

import com.vmojing.base.MyAppplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ImageAsync extends AsyncTask<String, Void, Bitmap>{
	/**�����Ķ���*/
	private Context context;
	/**ͼƬ��ʾ�ؼ�*/
	private ImageView imageView;   

	public ImageAsync(Context context, ImageView imageView) {
		this.context=context;
		this.imageView=imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		LruCache<String, BitmapStatus> lruCache;//ͼƬ�ڴ滺��
		lruCache=((MyAppplication)context.getApplicationContext()).getmLruCache();
		/**��SD����ȡͼƬ*/
		Bitmap bitmap=CacheFile.getBitmapFromFileCache(params[0]);
		CacheMemory mc=new CacheMemory(lruCache);
		/**����SD����ȡ��ͼƬ���浽�ڴ���*/
		if(bitmap!=null)
			mc.addBitmapToMemoryCache(params[0], bitmap);
		else{
			/**�������ȡͼƬ*/
			byte[] bytes=HttpComm.getNetworkByte(params[0]);
			/**�ж��Ƿ�ɹ��������ȡͼƬ*/
			if(bytes!=null){
				bitmap=ImageUtil.compressBitmap(bytes, 180,180);
				/**��ȡ��ͼƬ��ͼƬ���浽SD�����ڴ���*/
				if(bitmap!=null){
					mc.addBitmapToMemoryCache(params[0], bitmap);
					CacheFile.addBitmapToFileCache(params[0], bitmap);
				}
			}
			else{/**��ȡ����ͼƬʱ,�ӻ�����ɾ�����ͼƬ��Ӧ��url*/
				lruCache.remove(params[0]);
			}
		}
		return bitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		/**��ImageView�ؼ�����ʾͼƬ*/
		if(result!=null)
			imageView.setImageBitmap(result);
	}

}
