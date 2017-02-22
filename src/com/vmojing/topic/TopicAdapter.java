package com.vmojing.topic;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vmojing.R;

public class TopicAdapter extends BaseAdapter{
	private Context context;
    private List<Topic>data;
	public TopicAdapter(Context context,List<Topic>data) {
		this.context=context;
		this.data=data;
	}
	@Override
	public int getCount() {
		return data.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams", "SimpleDateFormat" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater layout= LayoutInflater.from (context);
		View view=layout.inflate(R.layout.item_topic, null);
		/**话题名称*/
		TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(data.get(position).getName());
        /**话题创建时间*/
        TextView createTime=(TextView)view.findViewById(R.id.createtime);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createTime.setText(sdf.format(data.get(position).getCreateTime()));
        /**话题类型*/
        ImageView type=(ImageView)view.findViewById(R.id.type);
        if(data.get(position).getType()==0){//领导
        	type.setImageResource(R.drawable.leader);
        }else if(data.get(position).getType()==1){//部门
        	type.setImageResource(R.drawable.department);
        }else if(data.get(position).getType()==2){//其他
        	type.setImageResource(R.drawable.other);
        }
        /**话题状态*/
        ImageView status=(ImageView)view.findViewById(R.id.status);
        if(data.get(position).getStatus()==0){//正常话题
        	status.setImageResource(R.drawable.user_normal);
        }else if(data.get(position).getStatus()==1){//异常话题
        	status.setImageResource(R.drawable.user_abnormal);
        }
        /**话题今日微博量*/	
        TextView todayCount = (TextView)view.findViewById(R.id.todaycount);
        todayCount.setText("今日微博量:"+data.get(position).getTodayCount());
        /**话题总微博量*/	
        TextView totalCount=(TextView)view.findViewById(R.id.totalcount);
        totalCount.setText("微博总量:"+data.get(position).getTotalCount());
        
        CheckBox check = (CheckBox)view.findViewById(R.id.check);
        check.setVisibility(data.get(position).getVisible());
        if(data.get(position).getCheck().equals("false"))
        	check.setChecked(false);
        else
        	check.setChecked(true);
		return view;
	}

}

