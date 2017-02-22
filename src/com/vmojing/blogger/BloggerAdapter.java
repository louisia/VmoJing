package com.vmojing.blogger;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.comm.CacheMemory;
import com.vmojing.comm.ImageAsync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class BloggerAdapter extends BaseAdapter{
	private Context context;
    private List<Blogger>data;
	public BloggerAdapter(Context context,List<Blogger>data) {
		this.context=context;
		this.data=data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@SuppressLint({ "ViewHolder", "InflateParams", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layout= LayoutInflater.from (context);
		View view=layout.inflate(R.layout.item_blogger, null);
		/**�û��ǳ�*/
		TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(data.get(position).getUser().getName().toString());
		/**�û�ͷ��*/
		ImageView userhead=(ImageView)view.findViewById(R.id.head);
		String url=data.get(position).getUser().getHeadUrl();
		CacheMemory mc=new CacheMemory(((MyAppplication)context.
				getApplicationContext()).getmLruCache());
		Bitmap bitmap=mc.getBitmapFromMemoryCache(url);
		//�ж�ͷ���Ƿ��Ѿ�����
		if(bitmap!=null)
			userhead.setImageBitmap(bitmap);
		else
		{
			ImageAsync imageAsync=new ImageAsync(context, userhead);
			imageAsync.execute(url);
		}
		
        /**�û����ʱ��*/
        TextView monitoredTime=(TextView)view.findViewById(R.id.time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        monitoredTime.setText(sdf.format(data.get(position).getMonitoredTime()));
        /**�û���״̬*/
        ImageView status=(ImageView)view.findViewById(R.id.status);
        if(data.get(position).getStatus()==0)
        	status.setImageResource(R.drawable.user_normal);
        else
        	status.setImageResource(R.drawable.user_abnormal);
        /**�û�΢����*/
        TextView weiboCount = (TextView)view.findViewById(R.id.weibocount);
        weiboCount.setText("΢����"+data.get(position).getWeiboCount());
        /**�û���˿��*/
        TextView followerCount=(TextView)view.findViewById(R.id.followercount);
        followerCount.setText("��˿��"+data.get(position).getFollowersCount());
        /**�û�������*/
        TextView friendCount=(TextView)view.findViewById(R.id.friendcount);
        friendCount.setText("������"+data.get(position).getFriendsCount());
        CheckBox check = (CheckBox)view.findViewById(R.id.blogger_check);
        check.setVisibility(data.get(position).getVisible());
        if(data.get(position).getCheck().equals("false"))
        	check.setChecked(false);
        else
        	check.setChecked(true);
		return view;
	}
	

}
