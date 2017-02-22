package com.vmojing.publics;

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
import android.widget.LinearLayout;
import android.widget.TextView;

public class PublicsAdapter extends BaseAdapter{
	private Context context;
	private List<Publics> publics;
	public PublicsAdapter(Context context,List<Publics>publics) {
		this.context=context;
		this.publics=publics;
	}

	@Override
	public int getCount() {
		return publics.size();
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
		View view=layout.inflate(R.layout.item_public, null);
		/**预测评论数*/
		TextView comment =(TextView)view.findViewById(R.id.public_comment);  
		comment.setText("预测评论"+publics.get(position).getCommentCount());
		/**预测转发数*/
		TextView retweet =(TextView)view.findViewById(R.id.public_retweet);
		retweet.setText("预测转发"+publics.get(position).getRepostCount());
		/**微博*/
		LinearLayout weiboview=(LinearLayout)view.findViewById(R.id.public_weibo);
		WeiboAdapter weiboAdapter=new WeiboAdapter(context);
		weiboAdapter.init(weiboview, (Weibo)publics.get(position).getWeibo());
		return view;
	}
}
