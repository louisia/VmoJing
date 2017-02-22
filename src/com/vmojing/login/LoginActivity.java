package com.vmojing.login;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.comm.HttpComm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity{
	/**用户名*/
	private EditText loginname;  
	/**密码*/
	private EditText password;  
	/**登录按钮*/
	private Button loginBtn;     
	/**保存登录用户配置信息*/
	private SharedPreferences userinfo;
	/**正在登录进度条*/
	private ProgressDialog progressDialog = null;
	/**将用户名保存到全局变量中*/
	private  MyAppplication myAPP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/**将该Activity加入到活动列表中*/
		loginname=(EditText)findViewById(R.id.loginname);
		password=(EditText)findViewById(R.id.password);
		loginBtn=(Button)findViewById(R.id.loginbtn);
		/**从配置文件中读取登录账户信息*/
		userinfo = getSharedPreferences("userinfo", 0);
		myAPP=(MyAppplication)getApplicationContext();
		/**如果用户之前已登录，直接调整*/
		if(userinfo.getBoolean("islogin", false)){
			myAPP.setUsername(userinfo.getString("loginname", ""));
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			finish();
		}
		/**主线程更新*/
		final Handler mHandler = new Handler() {  
			public void handleMessage (Message msg) { 
				progressDialog.dismiss();
				switch(msg.what) {  
				case 0:  
					userinfo = getSharedPreferences("userinfo", 0);
					SharedPreferences.Editor editor = userinfo.edit();
					editor.putString("loginname",loginname.getText().toString());
					editor.putString("password",password.getText().toString());
					editor.putBoolean("islogin", true);
					editor.commit();
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
					break;  
				case 1:  
					Toast.makeText(getApplicationContext(), "用户名或密码输入错误", 
							Toast.LENGTH_SHORT).show();
					break;  
				}  
			}  
		};  
		/**登录按钮单击事件*/
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myAPP.setUsername(loginname.getText().toString());
				progressDialog = ProgressDialog.show(LoginActivity.this, "请稍等...", "登录中...", true);
				/** 向服务器发送用户名密码验证*/
				Thread thread=new Thread(new Runnable() {
					@Override
					public void run() {
						String path="http://10.23.0.102:8080/vmojing-web/login";
						String content="{\"uid\":\""
								+loginname.getText().toString() 
								+"\",\"password\":\""
								+ password.getText().toString()
								+ "\"}";
						if(HttpComm.postJson(path, content).toString().equals("true")){
							mHandler.sendEmptyMessage(0);
						}
						else
							mHandler.sendEmptyMessage(1);
					}
				});
				thread.start();
			}
		});
	}
	
}
