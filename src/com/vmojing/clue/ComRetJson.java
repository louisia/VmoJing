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
					/**设置评论转发的id*/
					comRet.setId(jsonArray.getJSONObject(i).getString("id"));
					/**设置评论转发的内容*/
					comRet.setContent(jsonArray.getJSONObject(i).getString("text"));
					/**设置评论转发的创建时间*/
					comRet.setCreateTime(new Date(jsonArray.getJSONObject(i).getLong("createAt")));
					/**设置评论转发的舆情*/
					comRet.setEmotion(jsonArray.getJSONObject(i).getInt("isOpinion"));
					/**设置0评论，1转发*/
					if(jsonArray.getJSONObject(i).get("replyCommentId")!=null)
						comRet.setReplyCommentId(jsonArray.getJSONObject(i).getLong("replyCommentId"));
					/**设置评论转发的用户*/
					JSONObject jsonObjectUser=new JSONObject(jsonArray.getJSONObject(i).getString("user"));
					/**设置微博博主*/
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
