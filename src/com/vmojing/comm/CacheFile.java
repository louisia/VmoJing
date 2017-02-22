package com.vmojing.comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
/**
 * ��SD���ж�ȡͼƬ���߽�ͼƬ���浽SD����
 * @author clyx
 */
public class CacheFile {
	
	/**
	 * ��ͼƬ���浽SD����
	 * @param filename ͼƬ����
	 * @param bitmap ͼƬ����
	 */
	public static void addBitmapToFileCache(String filename,Bitmap bitmap){
		String storageState=Environment.getExternalStorageState();
		/**�ж��Ƿ����SD��*/
		if(!storageState.equals(Environment.MEDIA_MOUNTED)){
			Log.v("Test", "CacheFile:���浽SD��ʧ�ܣ�SD��δ����");
			return;
		}
		/**������Ӧ��Ŀ¼*/
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/clyx");
		if(!file.exists()){
			file.mkdirs();
		}
		/**��Bitmap����ת��Ϊ�ֽ���*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		FileOutputStream fos; 
		/**�����浽SD���ϵ�ͼƬ����*/
		filename=filename.replace("/", "").replace(":", "").replace(".", "");
		if(!filename.endsWith("jpg"))
			filename=filename+".jpg";
		else
			filename.replace("jpg", ".jpg");
		/**��ͼƬ���浽SD����*/
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
	 * ����ͼƬ���ƴ�SD����ȡͼƬ
	 * @param filename ͼƬ����
	 * @return bitmap
	 */
	public static Bitmap getBitmapFromFileCache(String filename) {
		/**��ȡ��ȡ��ͼƬ������*/
		filename=filename.replace("/", "");
		if(!filename.endsWith(".jpg"))
			filename=filename+".jpg";
		String path=Environment.getExternalStorageDirectory()+"/clyx/"+filename;
		File mFile=new File(path);
		/**�ж��ļ��Ƿ����*/
		if (mFile.exists()) {
			Bitmap bitmap=BitmapFactory.decodeFile(path);
			return bitmap;
		}
		return null;
	}

}
