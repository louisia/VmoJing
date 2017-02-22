package com.vmojing.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 图片质量压缩，比例压缩
 * @author clyx
 *
 */
public class ImageUtil {
	/**
	 * 质量压缩
	 * @param pathName 图片在sd卡中的路径
	 * @param quality  压缩图片的质量
	 * @param format   压缩图片的格式Bitmap.CompressFormat.PNG或Bitmap.CompressFormat.JPG
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
	 * 比例压缩
	 * @param pathName 图片路径
	 * @param height 压缩后图片的高度
	 * @param width  压缩后图片的宽度
	 * @return
	 */
	public static Bitmap compressBitmap(byte[] data,int height,int width){
		if(data!=null){
			BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inJustDecodeBounds = true; 
			Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length,options);
			options.inSampleSize=1;
			/**计算压缩比例*/
			while(options.outHeight>height||options.outWidth>width){
				options.inSampleSize*=2;
				options.outHeight/=2;
				options.outWidth/=2;
			}
			/**载入压缩后的图片*/
			options.inJustDecodeBounds = false;  
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options); 
			return bitmap;  
		}
		return null;
	}
	
}
