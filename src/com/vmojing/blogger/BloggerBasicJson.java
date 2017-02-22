package com.vmojing.blogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vmojing.base.User;
import com.vmojing.comm.HttpComm;
import com.vmojing.ui.DragListView;

import android.content.Context;
import android.os.AsyncTask;

public class BloggerBasicJson extends AsyncTask<String, Void, List<Blogger>>{
	private List<Blogger> bloggers;
	private List<Blogger> bloggersChange;
	private BloggerAdapter adapter;
	private DragListView listView;
	private int index=0;
	private Context context;

	public BloggerBasicJson(List<Blogger> bloggers,List<Blogger> bloggersChange,
			BloggerAdapter adapter,DragListView listView,Context context) {
		this.bloggers=bloggers;
		this.bloggersChange=bloggersChange;
		this.adapter=adapter;
		this.listView=listView;
		this.context=context;
	}
	@Override
	protected List<Blogger> doInBackground(String... params) {
		if(params.length==2)
			index=Integer.valueOf(params[1]);
		List<Blogger>temps=new ArrayList<Blogger>();
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
			if(str!=null)
			{
				JSONArray jsonArray=new JSONArray(str);
				for(int i=0;i<jsonArray.length();i++)
				{
					Blogger blogger=new Blogger();
					/**������id*/
					blogger.setId(jsonArray.getJSONObject(i).getString("id"));
					User user=new User();
					JSONObject jsonObject=new JSONObject(jsonArray.getJSONObject(i).get("user").toString());
					user.setId(jsonObject.getString("id"));
					/**�������ǳ�*/
					user.setName(jsonObject.getString("name"));
					/**������ͷ��*/
					user.setHeadUrl(jsonObject.getString("profileImageUrl"));
					blogger.setUser(user);
					/**�����ķ�˿��*/
					blogger.setFollowersCount(jsonObject.getInt("followersCount"));
					/**�����ĺ�����*/
					blogger.setFriendsCount(jsonObject.getInt("friendsCount"));
					/**������΢����*/
					blogger.setWeiboCount(jsonObject.getInt("statusesCount"));
					/**�����ļ��ʱ��*/
					blogger.setMonitoredTime(new Date(jsonArray.getJSONObject(i).getLong("monitorAt")));
					blogger.setMonitorAt(jsonArray.getJSONObject(i).getLong("monitorAt"));
					/**������״̬*/
					blogger.setStatus(jsonArray.getJSONObject(i).getInt("status"));
					temps.add(blogger);
				}

			}
		} catch (Exception e) {
		}
		return temps;
	}
	@Override
	protected void onPostExecute(List<Blogger> result) {
		/**���½���ui*/
		if(index==1)
			listView.onRefreshComplete();
		else if(index==2)
			listView.onLoadMoreComplete(false);
		/**���½�������*/
		if(result.size()!=0){
			if(index==0){
				for(int i=0;i<result.size();i++){
					bloggers.add(result.get(i));
					bloggersChange.add(result.get(i));
				}
			}
			if(index==DragListView.LOADMORE_INDEX)
				for(int i=0;i<result.size();i++){
					bloggers.add(result.get(i));
					bloggersChange.add(result.get(i));
				}
			if(index==DragListView.DRAG_INDEX)
				for(int i=0;i<result.size();i++){
					bloggers.add(0,result.get(i));
					bloggersChange.add(0,result.get(i));
				}
		}
		adapter.notifyDataSetChanged();
	}

}
