package com.vmojing.feeling;

import java.util.List;

import com.vmojing.R;
import com.vmojing.base.Weibo;
import com.vmojing.base.WeiboAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FeelingAdapter extends BaseAdapter{
	private Context context;
	private List<Feeling> data;
	public FeelingAdapter(Context context,List<Feeling>data) {
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

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layout= LayoutInflater.from (context);
		View view=layout.inflate(R.layout.item_weibo, null);

		WeiboAdapter weiboAdapter=new WeiboAdapter(context);
		weiboAdapter.init(view, (Weibo)data.get(position).getWeibo());
		return view;
	}

}
