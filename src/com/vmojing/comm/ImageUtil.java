package com.vmojing.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * ͼƬ����ѹ��������ѹ��
 * @author clyx
 *
 */
public class ImageUtil {
	/**
	 * ����ѹ��
	 * @param pathName ͼƬ��sd���е�·��
	 * @param quality  ѹ��ͼƬ������
	 * @param format   ѹ��ͼƬ�ĸ�ʽBitmap.CompressFormat.PNG��Bitmap.CompressFormat.JPG
	 * @return
	 */
	public static Bitmap compressBitmap(String pathName,int quality,Bitmap.CompressFormat format){
		Bitmap bitmap=BitmapFactory.decodeFile(pathName);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bitmap.compress(format, quality, baos);
		ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
		return BitmapFactory.decodeStream(bais);
	}

	/**
	 * ����ѹ��
	 * @param pathName ͼƬ·��
	 * @param height ѹ����ͼƬ�ĸ߶�
	 * @param width  ѹ����ͼƬ�Ŀ��
	 * @return
	 */
	public static Bitmap compressBitmap(byte[] data,int height,int width){
		if(data!=null){
			BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inJustDecodeBounds = true; 
			Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length,options);
			options.inSampleSize=1;
			/**����ѹ������*/
			while(options.outHeight>height||options.outWidth>width){
				options.inSampleSize*=2;
				options.outHeight/=2;
				options.outWidth/=2;
			}
			/**����ѹ�����ͼƬ*/
			options.inJustDecodeBounds = false;  
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options); 
			return bitmap;  
		}
		return null;
	}
	
}
