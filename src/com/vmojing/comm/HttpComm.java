package com.vmojing.comm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
/**
 * 从服务器下载数据或将数据上传到服务器
 * @author clyx
 *
 */

public class HttpComm {
	/**
	 * 从网络中下载图片
	 * @param path 网络图片的路径
	 * @return 网络图片对于的数组
	 */
	public static byte[] getNetworkByte(String path){
		try {
			URL url = new URL(path);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200){
				InputStream inputStream = conn.getInputStream();
				/**将图片输入流转为字节流*/
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len=inputStream.read(buffer)) != -1){
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inputStream.close();
				return outStream.toByteArray();
			}
		} catch (IOException e) {
		}
		return null;
	}
	/**
	 * 从网页上获取json数据
	 * @param path json数据的url
	 * @return json数据的字符串
	 * @throws Exception
	 */
	public static String getJson(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			connection.connect();  
			if(connection.getResponseCode()==200){
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(connection.getInputStream(),"UTF-8"));  
				String str = null;  
				StringBuffer sb = new StringBuffer();  
				while ((str = bufferedReader.readLine()) != null) {  
					sb.append(str+"\r\n");  
				}  
				setURLCache(path, sb.toString());
				connection.disconnect(); 
				return sb.toString();
			}
		}catch (Exception e) {
		}  
		return null;
	}
	/**
	 * 向服务器发送json数据
	 * @param path post请求的路径
	 * @param content post请求的内容
	 * @return
	 */
	public static StringBuffer postJson(String path,String content) {
		StringBuffer sb=new StringBuffer();
		int code=-1;
		try {
			URL url = new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();  
			conn.setDoOutput(true);             //允许向服务器输出数据  
			conn.setDoInput(true);              //允许接收服务器数据  
			conn.setRequestMethod("POST");  
			conn.setUseCaches(false);           // Post 请求不能使用缓存  
			conn.setConnectTimeout(5000);  
			//设置参数类型，并将数据写入流中  
			conn.setRequestProperty("Content-Type","application/json"); 
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.connect();  
			conn.getOutputStream().write(content.getBytes());
			code=conn.getResponseCode();
			if(code==200){
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(),"UTF-8"));  
				String str = null;  
				while ((str = bufferedReader.readLine()) != null) {  
					sb.append(str);  
				} 
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if(code==200){
			return sb;
		}else{
			return null;
		}
	}
	/***
	 * 从本地获取json数据
	 * @param path
	 * @return
	 */
	public static String getUrlCache(String path){
		path=path.replace("/", "").replace(":", "").replace(".", "").replace("?", "").replace("=", "");
		path=path+".txt";
		path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/clyx/"+path;
		File file = new File(path);
		String result ="";
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				result +=s;
				result=result+"\r\n";
			}
			br.close();    
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/***
	 * 将网络中获取的json数据缓存到本地
	 * @param path
	 * @param content
	 */
	@SuppressWarnings("resource")
	public static void setURLCache(String path,String content){
		String storageState=Environment.getExternalStorageState();
		/**判断是否挂载SD卡*/
		if(!storageState.equals(Environment.MEDIA_MOUNTED)){
			return;
		}
		/**创建相应的目录*/
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/clyx");
		if(!file.exists()){
			file.mkdirs();
		}
		/**给保存到SD卡上的文件命名*/
		path=path.replace("/", "").replace(":", "").replace(".", "").replace("?", "").replace("=", "");;
		path=path+".txt";
		/**将文字保存到SD卡中*/
		try {  
			File f = new File(file,path); 
			/**文件存在时删除并重建*/
			if(f.exists())
				f.delete();
			f.createNewFile();
			FileWriter fw = new FileWriter(f); 
			fw.write(content);
			fw.flush();
		} catch (Exception e) {  
		}  
	}
	/**
	 * 判断当前网络是否可用
	 * @param activity
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        else
        {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            /**判断是否处于无线网状态*/
            if(networkInfo!=null&&networkInfo.isAvailable()
            		&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
            	return true;
        }
        return false;
    }
}
