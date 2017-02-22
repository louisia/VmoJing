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
		/**��������*/
		TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(data.get(position).getName());
        /**���ⴴ��ʱ��*/
        TextView createTime=(TextView)view.findViewById(R.id.createtime);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createTime.setText(sdf.format(data.get(position).getCreateTime()));
        /**��������*/
        ImageView type=(ImageView)view.findViewById(R.id.type);
        if(data.get(position).getType()==0){//�쵼
        	type.setImageResource(R.drawable.leader);
        }else if(data.get(position).getType()==1){//����
        	type.setImageResource(R.drawable.department);
        }else if(data.get(position).getType()==2){//����
        	type.setImageResource(R.drawable.other);
        }
        /**����״̬*/
        ImageView status=(ImageView)view.findViewById(R.id.status);
        if(data.get(position).getStatus()==0){//��������
        	status.setImageResource(R.drawable.user_normal);
        }else if(data.get(position).getStatus()==1){//�쳣����
        	status.setImageResource(R.drawable.user_abnormal);
        }
        /**�������΢����*/	
        TextView todayCount = (TextView)view.findViewById(R.id.todaycount);
        todayCount.setText("����΢����:"+data.get(position).getTodayCount());
        /**������΢����*/	
        TextView totalCount=(TextView)view.findViewById(R.id.totalcount);
        totalCount.setText("΢������:"+data.get(position).getTotalCount());
        
        CheckBox check = (CheckBox)view.findViewById(R.id.check);
        check.setVisibility(data.get(position).getVisible());
        if(data.get(position).getCheck().equals("false"))
        	check.setChecked(false);
        else
        	check.setChecked(true);
		return view;
	}

}

