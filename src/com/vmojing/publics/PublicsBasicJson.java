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
			/**WIfi可用,从网络获取数据*/
			if(HttpComm.isNetworkAvailable(context))
			{
				str=HttpComm.getJson(params[0]);
				if(str!=null)
					HttpComm.setURLCache(params[0], str);
			}
			else if(str==null){
				/**Wifi不可用,从缓存获取数据*/
				str=HttpComm.getUrlCache(params[0]);
			}
			if(str!=null){
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++)
				{
					Publics publics=new Publics();
					publics.setId(jsonArray.getJSONObject(i).getString("id"));
					JSONObject jsonObjectUser=new JSONObject(jsonArray.getJSONObject(i).getString("user"));
					/**设置微博博主*/
					User user=new User();
					user.setId(jsonObjectUser.getString("id"));
					if(!jsonObjectUser.getString("screenName").equals(null))
						user.setName(jsonObjectUser.getString("screenName"));
					else
						user.setName(jsonObjectUser.getString("name"));
					user.setHeadUrl(jsonObjectUser.getString("profileImageUrl"));
					Weibo weibo=new Weibo();
					weibo.setUser(user);
					/**设置博主微博的内容*/
					weibo.setContent(jsonArray.getJSONObject(i).getString("text"));
					/**设置博主微博中的图片*/
					if(!jsonArray.getJSONObject(i).getString("thumbnailPic").equals(null))
						weibo.setContenturl(jsonArray.getJSONObject(i).getString("thumbnailPic").split(","));
					/**设置微博的评论数*/
					weibo.setComment(jsonArray.getJSONObject(i).getInt("commentsCount"));
					/**设置微博的赞数*/
					weibo.setAttitude(jsonArray.getJSONObject(i).getInt("attitudeCount"));
					/**设置微博的转发数*/
					weibo.setRetweet(jsonArray.getJSONObject(i).getInt("repostsCount"));
					/**设置微博创建时间*/
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
		/**更新界面数据*/
		if(result.size()!=0){
				for(int i=0;i<result.size();i++){
					publics.add(result.get(i));
				}
		}
		adapter.notifyDataSetChanged();
	}
}
