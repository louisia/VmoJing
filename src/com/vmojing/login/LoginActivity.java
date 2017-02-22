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
	/**�û���*/
	private EditText loginname;  
	/**����*/
	private EditText password;  
	/**��¼��ť*/
	private Button loginBtn;     
	/**�����¼�û�������Ϣ*/
	private SharedPreferences userinfo;
	/**���ڵ�¼������*/
	private ProgressDialog progressDialog = null;
	/**���û������浽ȫ�ֱ�����*/
	private  MyAppplication myAPP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/**����Activity���뵽��б���*/
		loginname=(EditText)findViewById(R.id.loginname);
		password=(EditText)findViewById(R.id.password);
		loginBtn=(Button)findViewById(R.id.loginbtn);
		/**�������ļ��ж�ȡ��¼�˻���Ϣ*/
		userinfo = getSharedPreferences("userinfo", 0);
		myAPP=(MyAppplication)getApplicationContext();
		/**����û�֮ǰ�ѵ�¼��ֱ�ӵ���*/
		if(userinfo.getBoolean("islogin", false)){
			myAPP.setUsername(userinfo.getString("loginname", ""));
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			finish();
		}
		/**���̸߳���*/
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
					Toast.makeText(getApplicationContext(), "�û����������������", 
							Toast.LENGTH_SHORT).show();
					break;  
				}  
			}  
		};  
		/**��¼��ť�����¼�*/
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myAPP.setUsername(loginname.getText().toString());
				progressDialog = ProgressDialog.show(LoginActivity.this, "���Ե�...", "��¼��...", true);
				/** ������������û���������֤*/
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
