package com.vmojing.publics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vmojing.base.User;
import com.vmojing.base.Weibo;
import com.vmojing.comm.HttpComm;

import android.content.Context;
import android.os.AsyncTask;

public class PublicsBasicJson extends AsyncTask<String, Void, List<Publics>>{
	private List<Publics>publics;
	private PublicsAdapter adapter;
	private Context context;
	
	public PublicsBasicJson(List<Publics> publics, PublicsAdapter adapter,Context context) {
		this.publics=publics;
		this.adapter=adapter;
		this.context=context;
	}

	@Override
	protected List<Publics> doInBackground(String... params) {
		List<Publics>temps=new ArrayList<Publics>();
		String str=null;
		try {
			/**WIfi����,�������ȡ����*/
			if(HttpComm.isNetworkAvailable(context))
			{
				str=HttpComm.getJson(params[0]);
				if(str!=null)
					HttpComm.setURLCache(params[0], str);
			}
			else if(str==null){
				/**Wifi������,�ӻ����ȡ����*/
				str=HttpComm.getUrlCache(params[0]);
			}
			if(str!=null){
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++)
				{
					Publics publics=new Publics();
					publics.setId(jsonArray.getJSONObject(i).getString("id"));
					JSONObject jsonObjectUser=new JSONObject(jsonArray.getJSONObject(i).getString("user"));
					/**����΢������*/
					User user=new User();
					user.setId(jsonObjectUser.getString("id"));
					if(!jsonObjectUser.getString("screenName").equals(null))
						user.setName(jsonObjectUser.getString("screenName"));
					else
						user.setName(jsonObjectUser.getString("name"));
					user.setHeadUrl(jsonObjectUser.getString("profileImageUrl"));
					Weibo weibo=new Weibo();
					weibo.setUser(user);
					/**���ò���΢��������*/
					weibo.setContent(jsonArray.getJSONObject(i).getString("text"));
					/**���ò���΢���е�ͼƬ*/
					if(!jsonArray.getJSONObject(i).getString("thumbnailPic").equals(null))
						weibo.setContenturl(jsonArray.getJSONObject(i).getString("thumbnailPic").split(","));
					/**����΢����������*/
					weibo.setComment(jsonArray.getJSONObject(i).getInt("commentsCount"));
					/**����΢��������*/
					weibo.setAttitude(jsonArray.getJSONObject(i).getInt("attitudeCount"));
					/**����΢����ת����*/
					weibo.setRetweet(jsonArray.getJSONObject(i).getInt("repostsCount"));
					/**����΢������ʱ��*/
					weibo.setTime(new Date(jsonArray.getJSONObject(i).getLong("createAt")));
					publics.setWeibo(weibo);
					publics.setCommentCount(jsonArray.getJSONObject(i).getInt("commentPredictCount"));
					publics.setRepostCount(jsonArray.getJSONObject(i).getInt("retweetPredictCount"));
					temps.add(publics);
				}
			}
		} catch (Exception e) {
		}
		return temps;
	}

	@Override
	protected void onPostExecute(List<Publics> result) {
		/**���½�������*/
		if(result.size()!=0){
				for(int i=0;i<result.size();i++){
					publics.add(result.get(i));
				}
		}
		adapter.notifyDataSetChanged();
	}
}
