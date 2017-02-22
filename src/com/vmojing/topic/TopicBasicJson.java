package com.vmojing.topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.os.AsyncTask;

import com.vmojing.comm.HttpComm;
import com.vmojing.ui.DragListView;
public class TopicBasicJson extends AsyncTask<String, Void, List<Topic>>{
	private List<Topic>topics;
	private List<Topic>topicsChange;
	private TopicAdapter adapter;
	private DragListView listView;
	private int index=0;
	private Context context;
	
	public TopicBasicJson(List<Topic>topics,List<Topic>topicsChange,
			TopicAdapter adapter,DragListView listView,Context context) {
		this.topics=topics;
		this.topicsChange=topicsChange;
		this.adapter=adapter;
		this.listView=listView;
		this.context=context;
	}
	@Override
	protected List<Topic> doInBackground(String... params) {
		if(params.length==2)
			index=Integer.valueOf(params[1]);
		List<Topic>temps=new ArrayList<Topic>();
		String str=null;
		try {
			if(HttpComm.isNetworkAvailable(context))
			{
				str=HttpComm.getJson(params[0]);
				if(str!=null&&params.length==1)
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
					Topic topic=new Topic();
					/**话题标题*/
					topic.setId(jsonArray.getJSONObject(i).getString("id"));
					/**话题创建时间*/
					topic.setCreateTime(new Date(jsonArray.getJSONObject(i).getLong("monitorAt")));
					topic.setMonitorAt(jsonArray.getJSONObject(i).getLong("monitorAt"));
					/**话题的今日微博数*/
					topic.setTodayCount(jsonArray.getJSONObject(i).getInt("todayWeiboCount"));
					/**话题的总微博数*/
					topic.setTotalCount(jsonArray.getJSONObject(i).getInt("sum"));
					/**话题的状态*/
					topic.setStatus(jsonArray.getJSONObject(i).getInt("status"));
					/**话题的类型*/
					topic.setType(jsonArray.getJSONObject(i).getInt("type"));
					/**话题名称*/
					topic.setName(jsonArray.getJSONObject(i).getString("topicName"));
					temps.add(topic);
				}
			}
		} catch (Exception e) {
		}
		return temps;
	}
	@Override
	protected void onPostExecute(List<Topic> result) {
		/**更新界面ui*/
		if(index==1)
			listView.onRefreshComplete();
		else if(index==2)
			listView.onLoadMoreComplete(false);
		/**更新界面数据*/
		if(result.size()!=0){
			if(index==0){
				for(int i=0;i<result.size();i++){
					topics.add(result.get(i));
					topicsChange.add(result.get(i));
				}
			}
			if(index==DragListView.LOADMORE_INDEX)
				for(int i=0;i<result.size();i++){
					topics.add(result.get(i));
					topicsChange.add(result.get(i));
				}
			if(index==DragListView.DRAG_INDEX)
				for(int i=0;i<result.size();i++){
				topics.add(0,result.get(i));
				topicsChange.add(0,result.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
}
