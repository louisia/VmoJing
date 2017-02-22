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
 * �ӷ������������ݻ������ϴ���������
 * @author clyx
 *
 */

public class HttpComm {
	/**
	 * ������������ͼƬ
	 * @param path ����ͼƬ��·��
	 * @return ����ͼƬ���ڵ�����
	 */
	public static byte[] getNetworkByte(String path){
		try {
			URL url = new URL(path);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200){
				InputStream inputStream = conn.getInputStream();
				/**��ͼƬ������תΪ�ֽ���*/
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
	 * ����ҳ�ϻ�ȡjson����
	 * @param path json���ݵ�url
	 * @return json���ݵ��ַ���
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
	 * �����������json����
	 * @param path post�����·��
	 * @param content post���������
	 * @return
	 */
	public static StringBuffer postJson(String path,String content) {
		StringBuffer sb=new StringBuffer();
		int code=-1;
		try {
			URL url = new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();  
			conn.setDoOutput(true);             //������������������  
			conn.setDoInput(true);              //������շ���������  
			conn.setRequestMethod("POST");  
			conn.setUseCaches(false);           // Post ������ʹ�û���  
			conn.setConnectTimeout(5000);  
			//���ò������ͣ���������д������  
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
	 * �ӱ��ػ�ȡjson����
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
			BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
			String s = null;
			while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
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
	 * �������л�ȡ��json���ݻ��浽����
	 * @param path
	 * @param content
	 */
	@SuppressWarnings("resource")
	public static void setURLCache(String path,String content){
		String storageState=Environment.getExternalStorageState();
		/**�ж��Ƿ����SD��*/
		if(!storageState.equals(Environment.MEDIA_MOUNTED)){
			return;
		}
		/**������Ӧ��Ŀ¼*/
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/clyx");
		if(!file.exists()){
			file.mkdirs();
		}
		/**�����浽SD���ϵ��ļ�����*/
		path=path.replace("/", "").replace(":", "").replace(".", "").replace("?", "").replace("=", "");;
		path=path+".txt";
		/**�����ֱ��浽SD����*/
		try {  
			File f = new File(file,path); 
			/**�ļ�����ʱɾ�����ؽ�*/
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
	 * �жϵ�ǰ�����Ƿ����
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
            /**�ж��Ƿ���������״̬*/
            if(networkInfo!=null&&networkInfo.isAvailable()
            		&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
            	return true;
        }
        return false;
    }
}
