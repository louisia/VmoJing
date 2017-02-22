package com.vmojing.base;

import org.json.JSONException;
import org.json.JSONObject;

import com.vmojing.comm.HttpComm;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class PostAsyncTask extends AsyncTask<String, Void, StringBuffer>{
	private Context context;
	private String url;
	private String cotent;

	public PostAsyncTask(Context context,String url,String cotent) {
		this.context=context;
		this.url=url;
		this.cotent=cotent;
	}

	@Override
	protected StringBuffer doInBackground(String... params) {
		return HttpComm.postJson(url, cotent);
	}

	@Override
	protected void onPostExecute(StringBuffer result) {
		if(result!=null){
			try {
				JSONObject jsonObject = new JSONObject(result.toString());
				Toast.makeText(context, jsonObject.getString("message"), 
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {

			}
		}else{
			Toast.makeText(context, "²Ù×÷Ê§°Ü£¬ÇëÇó±»¾Ü¾ø", 
					Toast.LENGTH_SHORT).show();
		}
	}
}
