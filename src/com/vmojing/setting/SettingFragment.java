package com.vmojing.setting;

import com.vmojing.R;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.MenuTop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint("InflateParams")
public class SettingFragment extends Fragment {
	private View view;
	private RelativeLayout setTopic;      //话题阈值设置
	private RelativeLayout setBlogger;    //博主阈值设置
	private RelativeLayout setClue;       //线索阈值设置
	private EditText setMail;             //通知邮箱设置     
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_setting, null);
		/*阈值设置按钮*/
		setTopic=(RelativeLayout)view.findViewById(R.id.settopic);
		setBlogger=(RelativeLayout)view.findViewById(R.id.setblogger);
		setClue=(RelativeLayout)view.findViewById(R.id.setclue);
		/*通知邮箱*/
		setMail=(EditText)view.findViewById(R.id.setmail);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//初始化头部菜单
		SettingFragment fragment=new SettingFragment();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getCenterBtn().setVisibility(ImageView.GONE);
		topMenu.getRightBtn().setVisibility(ImageView.GONE);
		/*阈值设置点击事件*/
		setTopic.setOnClickListener(new MyClick());
		setBlogger.setOnClickListener(new MyClick());
		setClue.setOnClickListener(new MyClick());
		/*利用SharePreference保存数据*/
		SharedPreferences mailInfo = getActivity().getSharedPreferences("setmail", 0);
		String mail = mailInfo.getString("mail", "GourmetLu@163.com");
		setMail.setText(mail);
		setMail.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				SharedPreferences mailInfo = getActivity().getSharedPreferences("setmail", 0);
				mailInfo.edit().putString("mail", setMail.getText().toString()).commit();
				// TODO:需要保存到数据库中
			}
		});
	}
	/**
	 * 
	 * @author clyx
	 * 自定义按钮事件
	 *
	 */
	private class MyClick implements OnClickListener{
		Intent intent=new Intent();
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.settopic:
				intent.putExtra("title", "异常话题设置");
				intent.setClass(getActivity(), SettingActivity.class);
				startActivity(intent);
				break;
			case R.id.setblogger:
				intent.putExtra("title", "异常博主设置");
				intent.setClass(getActivity(), SettingActivity.class);
				startActivity(intent);
				break;
			case R.id.setclue:
				intent.putExtra("title", "异常线索设置");
				intent.setClass(getActivity(), SettingActivity.class);
				startActivity(intent);
				break;
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			}
		}
	}
}
