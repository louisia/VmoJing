package com.vmojing.clue;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.comm.CacheMemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComRetAdapter extends BaseAdapter {
	private Context context;
	private List<ComRet>data;
	public ComRetAdapter(Context context,List<ComRet>data) {
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

	@SuppressLint({ "SimpleDateFormat", "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layout= LayoutInflater.from (context);
		View view=layout.inflate(R.layout.item_comret, null);
		ImageView comment_head=(ImageView)view.findViewById(R.id.comment_head);
		TextView comment_name=(TextView)view.findViewById(R.id.comment_name);
		TextView comment_content=(TextView)view.findViewById(R.id.comment_content);
		TextView comment_time=(TextView)view.findViewById(R.id.comment_time);
		
		String url=data.get(position).getUser().getHeadUrl();
		/**从缓存中读取bitmap显示*/
		CacheMemory mc=new CacheMemory(((MyAppplication)context.
				getApplicationContext()).getmLruCache());
		Bitmap bitmap=mc.getBitmapFromMemoryCache(url);
		if(bitmap!=null)
			comment_head.setImageBitmap(bitmap);
		comment_name.setText(data.get(position).getUser().getName());
		comment_content.setText(data.get(position).getContent());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
		comment_time.setText(sdf.format(data.get(position).getCreateTime()));
		return view;
	}

}
