package com.vmojing.setting;

import com.vmojing.R;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.MenuTop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity {
	private RelativeLayout item1;
	private RelativeLayout item2;
	private RelativeLayout item3;
	private RelativeLayout item4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		item1=(RelativeLayout)findViewById(R.id.setvalue1);
		item2=(RelativeLayout)findViewById(R.id.setvalue2);
		item3=(RelativeLayout)findViewById(R.id.setvalue3);
		item4=(RelativeLayout)findViewById(R.id.setvalue4);
		initTopMenu();
	}

	private void initTopMenu(){
		//初始化头部菜单
		String title=getIntent().getSerializableExtra("title").toString();
		MenuTop menuTop=new MenuTop(this, title);
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageButton.GONE);;
		menuTop.getRightBtn().setVisibility(ImageButton.GONE);
		//初始化设置控件
		if(title.equals("异常话题设置")){
			initTopicValue();
		}
		else if(title.equals("异常博主设置")){
			initBloggerValue();
		}
		else{
			initClueValue();
		}
	}
	private void initClueValue() {
		// TODO Auto-generated method stub
		TextView tv1=(TextView)item1.findViewById(R.id.tv1);
		tv1.setText("转发增长速度：");
		TextView tv2=(TextView)item2.findViewById(R.id.tv1);
		tv2.setText("评论增长速度：");
		TextView tv3=(TextView)item3.findViewById(R.id.tv1);
		tv3.setText("水军参与比例：");
		item4.setVisibility(RelativeLayout.GONE);
		SharedPreferences clueInfo = getSharedPreferences("clue", 0);
		String retweet = clueInfo.getString("retweet", "50");
		String comment = clueInfo.getString("comment", "100");
		String navy=clueInfo.getString("navy", "100");
		EditText et1=(EditText)item1.findViewById(R.id.et1);
		EditText et2=(EditText)item2.findViewById(R.id.et1);
		EditText et3=(EditText)item3.findViewById(R.id.et1);
		et1.setText(retweet);
		et2.setText(comment);
		et3.setText(navy);
	}

	private void initBloggerValue() {
		// TODO Auto-generated method stub
		TextView tv1=(TextView)item1.findViewById(R.id.tv1);
		tv1.setText("微博增长速度：");
		TextView tv2=(TextView)item2.findViewById(R.id.tv1);
		tv2.setText("粉丝增长速度：");
		TextView tv3=(TextView)item3.findViewById(R.id.tv1);
		tv3.setText("大号增长速度：");
		item4.setVisibility(RelativeLayout.GONE);
		SharedPreferences bloggerInfo = getSharedPreferences("blogger", 0);
		String weibo = bloggerInfo.getString("weibo", "100");
		String fans = bloggerInfo.getString("fans", "100");
		String tuba=bloggerInfo.getString("tuba", "100");
		EditText et1=(EditText)item1.findViewById(R.id.et1);
		EditText et2=(EditText)item2.findViewById(R.id.et1);
		EditText et3=(EditText)item3.findViewById(R.id.et1);
		et1.setText(weibo);
		et2.setText(fans);
		et3.setText(tuba);
	}

	private void initTopicValue() {
		// TODO Auto-generated method stub
		TextView tv1=(TextView)item1.findViewById(R.id.tv1);
		tv1.setText("微博增长速度：");
		TextView tv2=(TextView)item2.findViewById(R.id.tv1);
		tv2.setText("博主增长速度：");
		TextView tv3=(TextView)item3.findViewById(R.id.tv1);
		tv3.setText("舆情增长速度：");
		TextView tv4=(TextView)item4.findViewById(R.id.tv1);
		tv4.setText("水军参与比例：");
		SharedPreferences topicInfo = getSharedPreferences("topic", 0);
		String weibo = topicInfo.getString("weibo", "100");
		String blogger = topicInfo.getString("blogger", "100");
		String feeling=topicInfo.getString("feeling", "100");
		String navy=topicInfo.getString("navy", "100");
		EditText et1=(EditText)item1.findViewById(R.id.et1);
		EditText et2=(EditText)item2.findViewById(R.id.et1);
		EditText et3=(EditText)item3.findViewById(R.id.et1);
		EditText et4=(EditText)item4.findViewById(R.id.et1);
		et1.setText(weibo);
		et2.setText(blogger);
		et3.setText(feeling);
		et4.setText(navy);
	}
	/**
	 * 保存用户更新后的阈值
	 */
	private void saveData(){
		String title=getIntent().getStringExtra("title");
		EditText et1=(EditText)item1.findViewById(R.id.et1);
		EditText et2=(EditText)item2.findViewById(R.id.et1);
		EditText et3=(EditText)item3.findViewById(R.id.et1);
		EditText et4=(EditText)item4.findViewById(R.id.et1);
		if(title.equals("异常话题设置")){
			SharedPreferences topicInfo = getSharedPreferences("topic", 0);
			topicInfo.edit().putString("weibo", et1.getText().toString()).commit();
			topicInfo.edit().putString("blogger", et2.getText().toString()).commit();	
			topicInfo.edit().putString("feeling", et3.getText().toString()).commit();
			topicInfo.edit().putString("navy", et4.getText().toString()).commit();
		}
		else if(title.equals("异常博主设置")){
			SharedPreferences bloggerInfo = getSharedPreferences("blogger", 0);
			bloggerInfo.edit().putString("weibo", et1.getText().toString()).commit();
			bloggerInfo.edit().putString("fans", et2.getText().toString()).commit();	
			bloggerInfo.edit().putString("tuba", et3.getText().toString()).commit();
		}
		else{
			SharedPreferences clueInfo = getSharedPreferences("clue", 0);
			clueInfo.edit().putString("retweet", et1.getText().toString()).commit();
			clueInfo.edit().putString("comment", et2.getText().toString()).commit();	
			clueInfo.edit().putString("navy", et3.getText().toString()).commit();
		}
	}
	private class MyClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.left:
				saveData();
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				intent.putExtra("type", "5");
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
	}
}
