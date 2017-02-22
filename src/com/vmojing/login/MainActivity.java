package com.vmojing.login;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.blogger.BloggerBasic;
import com.vmojing.clue.ClueBasic;
import com.vmojing.feeling.FeelingBasic;
import com.vmojing.publics.PublicsBasic;
import com.vmojing.setting.SettingFragment;
import com.vmojing.topic.TopicBasic;
import com.vmojing.ui.LeftFragment;
import com.vmojing.ui.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MainActivity extends FragmentActivity {
	private SlidingMenu mSlidingMenu;// 侧边栏的view
	private LeftFragment leftFragment; // 左侧边栏view
	private Fragment centerFragment;// 中间内容view
	private FragmentTransaction ft; // 
	private long firstTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frame_main);
		MyAppplication mydata=(MyAppplication)getApplication();
		mydata.addActivity(this);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.frame_left, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.frame_center, null));

		ft = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		if(getIntent().getStringExtra("type")==null)
			centerFragment = new PublicsBasic();
		else
		{
			String type=getIntent().getStringExtra("type");
			switch(Integer.parseInt(type)){
			case 1:
				centerFragment=new TopicBasic();
				break;
			case 2:
				centerFragment=new BloggerBasic();
				break;
			case 3:
				centerFragment=new ClueBasic();
				break;
			case 4:
				break;
			case 5:
				centerFragment=new SettingFragment();
				break;
			case 6:
				centerFragment=new FeelingBasic();
				break;
			}
		}
		ft.replace(R.id.left_frame, leftFragment);
		ft.replace(R.id.center_frame, centerFragment);
		ft.commit();
		firstTime=0;
	}
	public void showLeft() {
		mSlidingMenu.showLeftView();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime=System.currentTimeMillis();
			if((secondTime-firstTime)>5000){
				firstTime=System.currentTimeMillis();
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			}else{
				System.exit(0);
			}
			break;
		case KeyEvent.KEYCODE_MENU:
			startActivity(new Intent(MainActivity.this,LogoutActivity.class));
			break;
		default:
			break;
		}
		return true;
	}
}
