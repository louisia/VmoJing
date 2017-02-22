package com.vmojing.clue;

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
import android.util.Log;

public class ClueBasicJson extends AsyncTask<String, Void, List<Clue>>{
	private List<Clue>clues;
	private List<Clue>cluesChange;
	private ClueAdapter adapter;
	private Context context;
	private DragListView listView;
	private int index=0;
	public ClueBasicJson(List<Clue>clues,List<Clue>cluesChange,
			ClueAdapter adapter,DragListView listView,Context context) {
		this.clues=clues;
		this.cluesChange=cluesChange;
		this.adapter=adapter;
		this.listView=listView;
		this.context=context;
	}

	@Override
	protected List<Clue> doInBackground(String... params) {
		Log.v("Test", params[0]);
		if(params.length==2)
			index=Integer.valueOf(params[1]);
		List<Clue>temps=new ArrayList<Clue>();
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
				Log.v("Test", str);
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++)
				{
					Clue clue=new Clue();
					clue.setId(jsonArray.getJSONObject(i).getString("id"));
					JSONObject jsonObject=new JSONObject(jsonArray.getJSONObject(i).getString("weibo"));
					Weibo weibo =new Weibo();
					weibo.setId(jsonObject.getString("id"));
					JSONObject jsonObjectUser=new JSONObject(jsonObject.getString("user"));
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
					weibo.setContent(jsonObject.getString("text"));
					/**����΢����������*/
					weibo.setComment(jsonObject.getInt("commentsCount"));
					/**����΢��������*/
					weibo.setAttitude(jsonObject.getInt("attitudeCount"));
					/**����΢����ת����*/
					weibo.setRetweet(jsonObject.getInt("repostsCount"));
					/**����΢������ʱ��*/
					weibo.setTime(new Date(jsonObject.getLong("createAt")));
					clue.setMonitorAt(jsonArray.getJSONObject(i).getLong("monitorAt"));
					clue.setWeibo(weibo);
					temps.add(clue);
				}
			}
		} catch (Exception e) {
			Log.v("Test", e.toString());
		}
		
		return temps;
	}
	@Override
	protected void onPostExecute(List<Clue> result) {
		/**���½���ui*/
		if(index==1)
			listView.onRefreshComplete();
		else if(index==2)
			listView.onLoadMoreComplete(false);
		/**���½�������*/
		if(result.size()!=0){
			if(index==0){
				for(int i=0;i<result.size();i++){
					clues.add(result.get(i));
					cluesChange.add(result.get(i));
				}
			}
			if(index==DragListView.LOADMORE_INDEX)
				for(int i=0;i<result.size();i++){
					clues.add(result.get(i));
					cluesChange.add(result.get(i));
				}
			if(index==DragListView.DRAG_INDEX)
				for(int i=0;i<result.size();i++){
				clues.add(0,result.get(i));
				cluesChange.add(0,result.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
}
