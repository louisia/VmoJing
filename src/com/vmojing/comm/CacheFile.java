package com.vmojing.comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
/**
 * 从SD卡中读取图片或者将图片保存到SD卡中
 * @author clyx
 */
public class CacheFile {
	
	/**
	 * 将图片保存到SD卡中
	 * @param filename 图片名称
	 * @param bitmap 图片对象
	 */
	public static void addBitmapToFileCache(String filename,Bitmap bitmap){
		String storageState=Environment.getExternalStorageState();
		/**判断是否挂载SD卡*/
		if(!storageState.equals(Environment.MEDIA_MOUNTED)){
			Log.v("Test", "CacheFile:保存到SD卡失败，SD卡未挂载");
			return;
		}
		/**创建相应的目录*/
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/clyx");
		if(!file.exists()){
			file.mkdirs();
		}
		/**将Bitmap对象转换为字节流*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		FileOutputStream fos; 
		/**给保存到SD卡上的图片命名*/
		filename=filename.replace("/", "").replace(":", "").replace(".", "");
		if(!filename.endsWith("jpg"))
			filename=filename+".jpg";
		else
			filename.replace("jpg", ".jpg");
		/**将图片保存到SD卡中*/
		try {  
			File f = new File(file,filename);  
			fos = new FileOutputStream(f);  
			fos.write(baos.toByteArray());  
			fos.flush(); 
		} catch (Exception e) {  
			Log.v("Test", "CacheFile:"+e.toString());
		}  
	}
	/**
	 * 根据图片名称从SD卡读取图片
	 * @param filename 图片名称
	 * @return bitmap
	 */
	public static Bitmap getBitmapFromFileCache(String filename) {
		/**获取读取的图片的名称*/
		filename=filename.replace("/", "");
		if(!filename.endsWith(".jpg"))
			filename=filename+".jpg";
		String path=Environment.getExternalStorageDirectory()+"/clyx/"+filename;
		File mFile=new File(path);
		/**判读文件是否存在*/
		if (mFile.exists()) {
			Bitmap bitmap=BitmapFactory.decodeFile(path);
			return bitmap;
		}
		return null;
	}

}
