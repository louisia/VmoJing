package com.vmojing.clue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vmojing.base.User;
import com.vmojing.comm.HttpComm;

import android.content.Context;
import android.os.AsyncTask;

public class ComRetJson extends AsyncTask<String, Void, List<ComRet>>{
	private List<ComRet>comRets;
	private ComRetAdapter adapter;
	private Context context;

	public ComRetJson(List<ComRet>comRets,ComRetAdapter adapter,Context context) {
		this.comRets=comRets;
		this.adapter=adapter;
		this.context=context;
	}

	@Override
	protected List<ComRet> doInBackground(String... params) {
		List<ComRet>temps=new ArrayList<ComRet>();
		String str=null;
		try {
			if(HttpComm.isNetworkAvailable(context))
				str=HttpComm.getJson(params[0]);
			if(str!=null){
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++){
					ComRet comRet=new ComRet();
					/**��������ת����id*/
					comRet.setId(jsonArray.getJSONObject(i).getString("id"));
					/**��������ת��������*/
					comRet.setContent(jsonArray.getJSONObject(i).getString("text"));
					/**��������ת���Ĵ���ʱ��*/
					comRet.setCreateTime(new Date(jsonArray.getJSONObject(i).getLong("createAt")));
					/**��������ת��������*/
					comRet.setEmotion(jsonArray.getJSONObject(i).getInt("isOpinion"));
					/**����0���ۣ�1ת��*/
					if(jsonArray.getJSONObject(i).get("replyCommentId")!=null)
						comRet.setReplyCommentId(jsonArray.getJSONObject(i).getLong("replyCommentId"));
					/**��������ת�����û�*/
					JSONObject jsonObjectUser=new JSONObject(jsonArray.getJSONObject(i).getString("user"));
					/**����΢������*/
					User user=new User();
					user.setId(jsonObjectUser.getString("id"));
					if(!jsonObjectUser.getString("screenName").equals(null))
						user.setName(jsonObjectUser.getString("screenName"));
					else
						user.setName(jsonObjectUser.getString("name"));
					user.setHeadUrl(jsonObjectUser.getString("profileImageUrl"));
					comRet.setUser(user);
					temps.add(comRet);
				}
				return temps;	
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(List<ComRet> result) {
		if(result.size()!=0){
			comRets.clear();
			for(int i=0;i<result.size();i++){
				comRets.add(result.get(i));
			}
			adapter.notifyDataSetChanged();
		}	
	}
}
