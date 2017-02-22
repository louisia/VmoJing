package com.vmojing.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vmojing.R;
import com.vmojing.blogger.BloggerBasic;
import com.vmojing.clue.ClueBasic;
import com.vmojing.feeling.FeelingBasic;
import com.vmojing.publics.PublicsBasic;
import com.vmojing.setting.SettingFragment;
import com.vmojing.topic.TopicBasic;

public class MenuTop {
	private ImageView leftBtn;
	private ImageView centerBtn;
	private ImageView rightBtn;

	public MenuTop(Fragment fragment,View view) {
		// TODO Auto-generated constructor stub
		RelativeLayout menu_top=(RelativeLayout)view.findViewById(R.id.menu_top);
		TextView titleTextView=(TextView)menu_top.findViewById(R.id.title);
		centerBtn=(ImageView)menu_top.findViewById(R.id.center);
		rightBtn=(ImageView)menu_top.findViewById(R.id.right);
		leftBtn=(ImageView)menu_top.findViewById(R.id.left);
		if(fragment instanceof TopicBasic){
			titleTextView.setText("话题监测");
		}
		if(fragment instanceof BloggerBasic){
			titleTextView.setText("博主关注");
		}
		if(fragment instanceof ClueBasic){
			titleTextView.setText("微博线索");
			centerBtn.setVisibility(ImageButton.GONE);
		}
		if(fragment instanceof PublicsBasic){
			titleTextView.setText("公共热点");
			centerBtn.setVisibility(ImageButton.GONE);
		}
		if(fragment instanceof SettingFragment){
			titleTextView.setText("阈值设置");
			centerBtn.setVisibility(ImageButton.GONE);
			rightBtn.setVisibility(ImageButton.GONE);
		}
		if(fragment instanceof FeelingBasic){
			titleTextView.setText("舆情处理");
			centerBtn.setVisibility(ImageButton.GONE);
		}
	}
	public MenuTop(Activity activity,String title) {
		RelativeLayout menu_top=(RelativeLayout)activity.findViewById(R.id.menu_top);
		TextView titleTextView=(TextView)menu_top.findViewById(R.id.title);
		titleTextView.setText(title);
		centerBtn=(ImageView)menu_top.findViewById(R.id.center);
		rightBtn=(ImageView)menu_top.findViewById(R.id.right);
		rightBtn.setImageResource(R.drawable.refresh);
		leftBtn=(ImageView)menu_top.findViewById(R.id.left);
		leftBtn.setImageResource(R.drawable.back);
	}

	public ImageView getRightBtn() {
		return rightBtn;
	}
	public ImageView getCenterBtn() {
		return centerBtn;
	}
	public ImageView getLeftBtn() {
		return leftBtn;
	}
}
