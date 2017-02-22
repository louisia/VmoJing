package com.vmojing.topic;

import java.util.HashMap;

import org.json.JSONObject;

import com.vmojing.comm.HttpComm;
import com.vmojing.topic.TopicAnalyse.TopicAnalyseView;

import android.content.Context;
import android.os.AsyncTask;

public class TopicAnalyseJson extends AsyncTask<String, Void, HashMap<String, Integer>>{
	private TopicAnalyseView topicAnalyseView;
	private Context context;
	public TopicAnalyseJson(TopicAnalyseView topicAnalyseView,
			Context context) {
		this.topicAnalyseView=topicAnalyseView;
		this.context=context;
	}

	@Override
	protected HashMap<String, Integer> doInBackground(String... params) {
		HashMap<String, Integer>analysedate=null;
		String str=null;
		try {
			if(HttpComm.isNetworkAvailable(context)){
				str=HttpComm.getJson(params[0]);
			}
			if(str!=null){
				analysedate=new HashMap<String,Integer>();
				JSONObject jsonObject=new JSONObject(str);
				JSONObject jsonObjectAnalyse=new JSONObject(jsonObject.getString("basic"));
				analysedate.put("positiveWeibo", jsonObjectAnalyse.getInt("posNum"));
				analysedate.put("middleWeibo", jsonObjectAnalyse.getInt("midNum"));
				analysedate.put("negativeWeibo", jsonObjectAnalyse.getInt("negNum"));
				analysedate.put("bloggerCount", jsonObjectAnalyse.getInt("bloggerNum"));
				analysedate.put("zombieCount", jsonObjectAnalyse.getInt("zomNum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return analysedate;
	}
	@Override
	protected void onPostExecute(HashMap<String, Integer> result) {
		if(result!=null){
			topicAnalyseView.getBloggerTextView().setText("����:"+result.get("bloggerCount"));
			topicAnalyseView.getZombieTextView().setText("ˮ��:"+result.get("zombieCount"));
			topicAnalyseView.getNegativeTextView().setText("����:"+result.get("negativeWeibo"));
			topicAnalyseView.getNeutralTextView().setText("����:"+result.get("middleWeibo"));
			topicAnalyseView.getPositiveTextView().setText("����:"+result.get("positiveWeibo"));
		}
	}
}
