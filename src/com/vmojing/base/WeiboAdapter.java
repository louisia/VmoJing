package com.vmojing.base;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vmojing.R;
import com.vmojing.comm.CacheMemory;
import com.vmojing.comm.ImageAsync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeiboAdapter extends BaseAdapter {
	private Context context;
	private List<Weibo> weibos;

	public WeiboAdapter(Context context) {
		this.context=context;
	}
	public WeiboAdapter(Context context,List<Weibo>weibos) {
		this.context=context;
		this.weibos=weibos;
	}
	@Override
	public int getCount() {
		return weibos.size();
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
		init(view,weibos.get(position));
		return view;
	}

	@SuppressLint("SimpleDateFormat")
	public  void init(View view,Weibo weibo){
		/**�û�ͷ��*/
		ImageView weibo_head=(ImageView)view.findViewById(R.id.weibo_head);
		/**�ӻ����ж�ȡbitmap��ʾ*/
		CacheMemory mc=new CacheMemory(((MyAppplication)context.
				getApplicationContext()).getmLruCache());
		
		Bitmap bitmap=mc.getBitmapFromMemoryCache(weibo.getUser().getHeadUrl());
		if(bitmap!=null)
			weibo_head.setImageBitmap(bitmap);
		else{/**���������SD������ͼƬ*/
			ImageAsync imageAsync=new ImageAsync(context,weibo_head);
			imageAsync.execute(weibo.getUser().getHeadUrl());
		}
		/**�û��ǳ�*/
		TextView weibo_name = (TextView)view.findViewById(R.id.weibo_name);
		weibo_name.setText(weibo.getUser().getName());
		/**΢������ʱ��*/
		TextView weibo_time =(TextView)view.findViewById(R.id.weibo_time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		weibo_time.setText(sdf.format(weibo.getTime()));
		LinearLayout weibo_layout=(LinearLayout)view.findViewById(R.id.weibo_layout);
		/**΢������*/
		TextView weibo_content = (TextView)weibo_layout.findViewById(R.id.weibo_content); 
		weibo_content.setText(weibo.getContent());
		/**΢��ͼƬ*/
		int id[]={R.id.weibo_contentpic,R.id.weibo_contentpic1,R.id.weibo_contentpic2,
				R.id.weibo_contentpic3,R.id.weibo_contentpic4,R.id.weibo_contentpic5,
				R.id.weibo_contentpic6,R.id.weibo_contentpic7,R.id.weibo_contentpic8};
		if(weibo.getContenturl()!=null)
		{
			for(int i=0;i<weibo.getContenturl().length;i++){
				ImageView weibo_contentpic=(ImageView)weibo_layout.findViewById(id[i]);
				weibo_contentpic.setVisibility(ImageView.VISIBLE);
				bitmap=mc.getBitmapFromMemoryCache(weibo.getContenturl()[i]);
				if(bitmap!=null)
					weibo_contentpic.setImageBitmap(bitmap);
				else{
					ImageAsync imageAsync=new ImageAsync(context,weibo_contentpic);
					imageAsync.execute(weibo.getContenturl()[i]);
				}
			}

		}
		/**�ж�΢���Ƿ�����΢��*/
		if(weibo.getWeibo()!=null)
		{
			LinearLayout weibo_sublayout=(LinearLayout)view.findViewById(R.id.weibo_sublayout);
			weibo_sublayout.setVisibility(View.VISIBLE);
			/**��΢������*/
			TextView weibo_subcontent = (TextView)weibo_sublayout.findViewById(R.id.weibo_content); 
			weibo_subcontent.setText("@"+weibo.getWeibo().getUser().getName()+":"+weibo.getWeibo().getContent());
			/**��΢��ͼƬ*/
			if(weibo.getWeibo().getContenturl()!=null)
			{
				for(int i=0;i<weibo.getWeibo().getContenturl().length;i++){
					ImageView weibo_contentpic=(ImageView)weibo_layout.findViewById(id[i]);
					weibo_contentpic.setVisibility(ImageView.VISIBLE);
					bitmap=mc.getBitmapFromMemoryCache(weibo.getContenturl()[i]);
					if(bitmap!=null)
						weibo_contentpic.setImageBitmap(bitmap);
					else{
						ImageAsync imageAsync=new ImageAsync(context,weibo_contentpic);
						imageAsync.execute(weibo.getWeibo().getContenturl()[i]);
					}
				}
			}
		}
		/**΢��������*/
		TextView weibo_comment =(TextView)view.findViewById(R.id.weibo_comment);  
		weibo_comment.setText(weibo.getComment()+"");
		/**΢��ת����*/
		TextView weibo_retweet =(TextView)view.findViewById(R.id.weibo_retweet);
		weibo_retweet.setText(weibo.getRetweet()+"");
		/**΢������*/
		TextView weibo_like=(TextView)view.findViewById(R.id.weibo_like);
		weibo_like.setText(weibo.getAttitude()+"");
	}

}
