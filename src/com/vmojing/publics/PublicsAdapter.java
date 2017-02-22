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
		/**Ԥ��������*/
		TextView comment =(TextView)view.findViewById(R.id.public_comment);  
		comment.setText("Ԥ������"+publics.get(position).getCommentCount());
		/**Ԥ��ת����*/
		TextView retweet =(TextView)view.findViewById(R.id.public_retweet);
		retweet.setText("Ԥ��ת��"+publics.get(position).getRepostCount());
		/**΢��*/
		LinearLayout weiboview=(LinearLayout)view.findViewById(R.id.public_weibo);
		WeiboAdapter weiboAdapter=new WeiboAdapter(context);
		weiboAdapter.init(weiboview, (Weibo)publics.get(position).getWeibo());
		return view;
	}
}
