package com.vmojing.blogger;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vmojing.blogger.BloggerAnalyse.BloggerAnalyseView;
import com.vmojing.comm.HttpComm;

import android.content.Context;
import android.os.AsyncTask;

public class BloggerAnalyseJson extends AsyncTask<String, Void, HashMap<String,Integer>>{
	private BloggerAnalyseView bloggerAnalyseView;
	private Context context;
	public BloggerAnalyseJson(BloggerAnalyseView bloggerAnalyseView,
			Context context) {
		this.bloggerAnalyseView=bloggerAnalyseView;
		this.context=context;
	}
	@Override
	protected HashMap<String,Integer> doInBackground(String... params) {
		HashMap<String,Integer> item=null;
		String str=null;
		try {
			int retweet=0,at=0;
			if(HttpComm.isNetworkAvailable(context)){
				str=HttpComm.getJson(params[0]);
			}
			if(str!=null){
				item=new HashMap<String, Integer>();
				JSONObject jsonObject=new JSONObject(str);
				JSONArray jsonArray=new JSONArray(jsonObject.getString("retweetBlogger"));
				for(int i=0;i<jsonArray.length();i++){
					retweet+=jsonArray.getJSONObject(i).getInt("count");
				}
				item.put("retweet", retweet);
				jsonArray=new JSONArray(jsonObject.getString("commentBlogger"));
				for(int i=0;i<jsonArray.length();i++){
					at+=jsonArray.getJSONObject(i).getInt("count");
				}
				item.put("at", at);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	@Override
	protected void onPostExecute(HashMap<String, Integer> result) {
		if(result!=null)
		{		
			bloggerAnalyseView.getRetweetCountTextView().setText("×ª·¢Î¢²©:"+result.get("retweet"));
			bloggerAnalyseView.getNotifyTextView().setText("@Î¢²©:"+result.get("at"));
		}
	}
}
