package com.vmojing.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vmojing.comm.HttpComm;

import android.content.Context;
import android.os.AsyncTask;

public class WeiboJson extends AsyncTask<String, Void, List<Weibo>>{
	private List<Weibo> weibos;
	private WeiboAdapter adapter;
	private Context context;
	public WeiboJson(List<Weibo> weibos,WeiboAdapter adapter,Context context) {
		this.weibos=weibos;
		this.adapter=adapter;
		this.context=context;
	}
	@Override
	protected List<Weibo> doInBackground(String... params) {
		List<Weibo>temps=new ArrayList<Weibo>();
		String str=null;
		try {
			if(HttpComm.isNetworkAvailable(context))
				str = HttpComm.getJson(params[0]);
			if(str!=null){
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++)
				{
					Weibo weibo =new Weibo();
					weibo.setId(jsonArray.getJSONObject(i).getString("id"));
					JSONObject jsonObjectUser=new JSONObject(jsonArray.getJSONObject(i).getString("user"));
					/**����΢������*/
					User user=new User();
					user.setId(jsonObjectUser.getString("id"));
					if(!jsonObjectUser.getString("screenName").equals(null))
						user.setName(jsonObjectUser.getString("screenName"));
					else
						user.setName(jsonObjectUser.getString("name"));
					user.setHeadUrl(jsonObjectUser.getString("profileImageUrl"));
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
					/**����ת��΢������*/
					if(!jsonArray.getJSONObject(i).getString("retweetWeibo").equals(null)){
						JSONObject subWeibo=new JSONObject(jsonArray.getJSONObject(i).getString("retweetWeibo"));
						JSONObject jsonObjectSubUser=new JSONObject(subWeibo.getString("user"));
						User subUser=new User();
						subUser.setName(jsonObjectSubUser.getString("screenName"));
						Weibo retweetWeibo=new Weibo();
						retweetWeibo.setUser(subUser);
						retweetWeibo.setContent(subWeibo.getString("text"));
						if(!subWeibo.getString("thumbnailPic").equals(null))
							retweetWeibo.setContenturl(subWeibo.getString("thumbnailPic").split(","));
						weibo.setWeibo(retweetWeibo);
					}
					temps.add(weibo);
				}
				return temps;
			}
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	protected void onPostExecute(List<Weibo> result) {
		/**ˢ�½�����ʾ����*/
		if(result.size()!=0){
			weibos.clear();
			for(int i=0;i<result.size();i++){
				weibos.add(result.get(i));
			}
			adapter.notifyDataSetChanged();
		}
	}
}
