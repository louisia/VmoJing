package com.vmojing.clue;

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
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class ClueAdapter extends BaseAdapter{
	private Context context;
	private List<Clue> data;
	public ClueAdapter(Context context,List<Clue>data) {
		// TODO Auto-generated constructor stub
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

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layout= LayoutInflater.from (context);
		View view=layout.inflate(R.layout.item_clue, null);
		
        CheckBox checkBox = (CheckBox)view.findViewById(R.id.clue_check);
        checkBox.setVisibility(data.get(position).getVisible());
        if(data.get(position).getCheck().equals("false"))
        	checkBox.setChecked(false);
        else
        	checkBox.setChecked(true);
        /**Î¢²©*/
        LinearLayout weiboview=(LinearLayout)view.findViewById(R.id.clue_weibo);

		WeiboAdapter weiboAdapter=new WeiboAdapter(context);
		weiboAdapter.init(weiboview, (Weibo)data.get(position).getWeibo());
        
		return view;
	}
	


}
