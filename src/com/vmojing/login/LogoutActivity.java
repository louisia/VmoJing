package com.vmojing.login;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LogoutActivity extends Activity implements OnClickListener{
	/**切换账号按钮*/
	private Button changeBtn;
	/**退出应用按钮*/
	private Button exitBtn;
	/**取消按钮*/
	private Button cancleBtn;
	private MyAppplication mydata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);

		mydata=(MyAppplication)getApplication();
		mydata.addActivity(this);

		changeBtn = (Button) this.findViewById(R.id.btn_change);
		exitBtn = (Button) this.findViewById(R.id.btn_exit);
		cancleBtn = (Button) this.findViewById(R.id.btn_cancel);

		/**添加按钮监听*/
		changeBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
	}

	/**实现onTouchEvent触屏函数但点击屏幕时销毁本Activity*/
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		/**切换账号*/
		case R.id.btn_change:
			/**将用户登录的配置信息设为空*/
			SharedPreferences userinfo=getSharedPreferences("userinfo", 0);
			SharedPreferences.Editor editor=userinfo.edit();
			editor.putString("loginname", "");
			editor.putString("password", "");
			editor.putBoolean("islogin", false);
			editor.commit();
			Intent intent = getBaseContext().getPackageManager() 
					.getLaunchIntentForPackage(getBaseContext().getPackageName()); 
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivity(intent);
			mydata.exit();
			break;
		case R.id.btn_exit:	
			mydata.exit();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}
}
