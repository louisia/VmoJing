package com.vmojing.clue;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.vmojing.clue.ClueAnalyse.ClueAnalyseView;
import com.vmojing.comm.HttpComm;

public class ClueAnalyseJson extends AsyncTask<String, Void, HashMap<String, Integer>>{
	private ClueAnalyseView clueAnalyseView;
	private Context context;
	public ClueAnalyseJson(ClueAnalyseView clueAnalyseView,
			Context context) {
		this.clueAnalyseView=clueAnalyseView;
		this.context=context;
	}

	@Override
	protected HashMap<String, Integer> doInBackground(String... params) {
		HashMap<String, Integer>analysedate=null;
		String str=null;
		try {
			if(HttpComm.isNetworkAvailable(context))
				str=HttpComm.getJson(params[0]);
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
			clueAnalyseView.getBloggerTextView().setText("博主数:"+result.get("bloggerCount"));
			clueAnalyseView.getZombieTextView().setText("水军数:"+result.get("zombieCount"));
			clueAnalyseView.getNegativeTextView().setText("负面舆情:"+result.get("negativeWeibo"));
			clueAnalyseView.getNeutralTextView().setText("中立舆情:"+result.get("middleWeibo"));
			clueAnalyseView.getPositiveTextView().setText("正面舆情:"+result.get("positiveWeibo"));
		}
	}
}