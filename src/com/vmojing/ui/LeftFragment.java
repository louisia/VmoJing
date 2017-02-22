package com.vmojing.ui;

import com.vmojing.R;
import com.vmojing.blogger.BloggerBasic;
import com.vmojing.clue.ClueBasic;
import com.vmojing.feeling.FeelingBasic;
import com.vmojing.login.MainActivity;
import com.vmojing.publics.PublicsBasic;
import com.vmojing.setting.SettingFragment;
import com.vmojing.topic.TopicBasic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class LeftFragment extends Fragment {
	private TextView topicMenu;
	private TextView bloggerMenu;
	private TextView clueMenu;
	private TextView publicsMenu;
	private TextView processMenu;
	private TextView settingMenu;
	private View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_left, null);
		topicMenu = (TextView) view.findViewById(R.id.topicmenu);
		bloggerMenu=(TextView) view.findViewById(R.id.bloggermenu);
		clueMenu=(TextView) view.findViewById(R.id.cluemenu);
		publicsMenu=(TextView) view.findViewById(R.id.publicmenu);
		processMenu=(TextView) view.findViewById(R.id.processmenu);
		settingMenu=(TextView) view.findViewById(R.id.settingmenu);
		//…Ë÷√º‡Ã˝∆˜
		topicMenu.setOnClickListener(new MyTextViewListener());
		bloggerMenu.setOnClickListener(new MyTextViewListener());
		clueMenu.setOnClickListener(new MyTextViewListener());
		publicsMenu.setOnClickListener(new MyTextViewListener());
		processMenu.setOnClickListener(new MyTextViewListener());
		settingMenu.setOnClickListener(new MyTextViewListener());
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	class MyTextViewListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Fragment fragment =null;
			//∂‡Ã¨
			switch(v.getId()){
			case R.id.topicmenu:
				fragment = new TopicBasic();
				break;
			case R.id.bloggermenu:
				fragment = new BloggerBasic();
				break;
			case R.id.cluemenu:
				fragment = new ClueBasic();
				break;
			case R.id.publicmenu:
				fragment = new PublicsBasic();
				break;
			case R.id.processmenu:
				fragment = new FeelingBasic();
				break;
			case R.id.settingmenu:
				fragment = new SettingFragment();
				break;
			}
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.center_frame, fragment);
			ft.commit();
			((MainActivity) getActivity()).showLeft();
		}

	}
}
