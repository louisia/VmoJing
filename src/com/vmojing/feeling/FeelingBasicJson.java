package com.vmojing.feeling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vmojing.base.User;
import com.vmojing.base.Weibo;
import com.vmojing.comm.HttpComm;
import com.vmojing.ui.DragListView;

import android.content.Context;
import android.os.AsyncTask;

public class FeelingBasicJson extends AsyncTask<String, Void, List<Feeling>>{
	private List<Feeling>feelings;
	private List<Feeling>feelingsChange;
	private FeelingAdapter adapter;
	private DragListView listView;
	private int index=0;
	private Context context;

	public FeelingBasicJson(List<Feeling>feelings,List<Feeling>feelingsChange
			,FeelingAdapter adapter,DragListView listView,Context context) {
		this.feelings=feelings;
		this.feelingsChange=feelingsChange;
		this.adapter =adapter;
		this.listView=listView;
		this.context=context;
	}

	@Override
	protected List<Feeling> doInBackground(String... params) {
		if(params.length==2)
			index=Integer.valueOf(params[1]);
		List<Feeling>temps=new ArrayList<Feeling>();
		String str=null;
		try {
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
					Feeling feeling=new Feeling();
					feeling.setId(jsonArray.getJSONObject(i).getString("id"));
					JSONObject jsonObjectWeibo=new JSONObject(jsonArray.getJSONObject(i).getString("weibo"));
					Weibo weibo =new Weibo();
					weibo.setId(jsonObjectWeibo.getString("id"));
					/**设置微博博主*/
					User user=new User();
					JSONObject jsonObjectUser=new JSONObject(jsonObjectWeibo.getString("user"));
					user.setId(jsonObjectUser.getString("id"));
					if(!jsonObjectUser.getString("screenName").equals(null))
						user.setName(jsonObjectUser.getString("screenName"));
					else
						user.setName(jsonObjectUser.getString("name"));
					user.setHeadUrl(jsonObjectUser.getString("profileImageUrl"));
					weibo.setUser(user);
					/**设置博主微博的内容*/
					weibo.setContent(jsonObjectWeibo.getString("text"));
					/**设置博主微博中的图片*/
					if(!jsonObjectWeibo.getString("thumbnailPic").equals(null))
						weibo.setContenturl(jsonObjectWeibo.getString("thumbnailPic").split(","));
					/**设置微博的评论数*/
					weibo.setComment(jsonObjectWeibo.getInt("commentsCount"));
					/**设置微博的赞数*/
					weibo.setAttitude(jsonObjectWeibo.getInt("attitudeCount"));
					/**设置微博的转发数*/
					weibo.setRetweet(jsonObjectWeibo.getInt("repostsCount"));
					/**设置微博创建时间*/
					weibo.setTime(new Date(jsonObjectWeibo.getLong("createAt")));
					feeling.setWeibo(weibo);
					feeling.setStatus(jsonArray.getJSONObject(i).getInt("operateStatus"));
					temps.add(feeling);
				}
			}
		} catch (Exception e) {
		}
		return temps;
	}
	@Override
	protected void onPostExecute(List<Feeling> result) {
		/**更新界面ui*/
		if(index==1)
			listView.onRefreshComplete();
		else
			listView.onLoadMoreComplete(false);
		/**更新界面数据*/
		if(result.size()!=0){
			feelings.clear();
			feelingsChange.clear();
			for(int i=0;i<result.size();i++){
				feelings.add(result.get(i));
				feelingsChange.add(result.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
}
