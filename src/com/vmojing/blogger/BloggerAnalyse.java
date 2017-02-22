package com.vmojing.blogger;

import java.util.ArrayList;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
import com.vmojing.base.Weibo;
import com.vmojing.base.WeiboAdapter;
import com.vmojing.base.WeiboJson;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.DragListView;
import com.vmojing.ui.MenuTop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BloggerAnalyse extends Activity {

	private DragListView dragListView;
	private ListView listView;

	private List<Weibo> weibos=new ArrayList<Weibo>();
	private WeiboAdapter adapter;

	private LinearLayout analyseLayout;
	private String analyseUrl;
	private String weiboUrl;
	private Blogger blogger;
	private MyAppplication myApp;

	@SuppressLint({ "InlinedApi", "InflateParams" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_center);
		myApp=(MyAppplication)getApplicationContext();
		/**��ȡIntent���ݵĶ���*/
		blogger=(Blogger)getIntent().getSerializableExtra("blogger");
		/**��ʼ��ͷ���˵�*/
		MenuTop menuTop=new MenuTop(this,blogger.getUser().getName());
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageView.GONE);
		menuTop.getRightBtn().setOnClickListener(new MyClick());
		/**��ʼ��ListView*/
		dragListView=(DragListView)findViewById(R.id.showlsitview);
		dragListView.setVisibility(ListView.GONE);
		listView=(ListView)findViewById(R.id.listview);
		adapter=new WeiboAdapter(getApplicationContext(), weibos);
		listView.setAdapter(adapter);
		/**��ListView��Ӳ˵�������*/
		registerForContextMenu(listView);
		/**��ʼ����������*/
		analyseLayout=(LinearLayout)findViewById(R.id.analyse);
		analyseLayout.setVisibility(LinearLayout.VISIBLE);
		//TODO ��ȡת��@��ϵ
		analyseUrl="http://10.23.0.102:8080/vmojing-web/blogger/relation?bid="
				+ blogger.getId()
				+ "&timestamp="
				+ blogger.getMonitorAt();
		BloggerAnalyseJson bloggerAnalyseJson=new BloggerAnalyseJson(new BloggerAnalyseView(),getApplicationContext());
		bloggerAnalyseJson.execute(analyseUrl);
		//TODO ��ȡ����΢���б�
		weiboUrl="http://10.23.0.102:8080/vmojing-web/blogger/weiboList?bid="
				+ blogger.getId()
				+ "&timestamp="
				+ blogger.getMonitorAt();
		WeiboJson weiboJson=new WeiboJson(weibos, adapter,getApplicationContext());
		weiboJson.execute(weiboUrl);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("΢������");
		menu.add(0, Menu.FIRST, 0, "����Ϊ����");
		menu.add(0, Menu.FIRST+1, 0, "���������");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=weibos.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:����Ϊ����
			String createClueUrl="http://10.23.0.102:8080/vmojing-web/clue/create";
			String createClueContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ id
					+ "\"}";
			PostAsyncTask asyncTaskClue=new PostAsyncTask(getApplicationContext(),createClueUrl, createClueContent);
			asyncTaskClue.execute();
			break;
		case 2:
			//TODO:���������
			String createFeelingUrl="http://10.23.0.102:8080/vmojing-web/opinion/create";
			String createFeelingContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ id
					+ "\"}";
			PostAsyncTask asyncTaskFeeling=new PostAsyncTask(getApplicationContext(),createFeelingUrl, createFeelingContent);
			asyncTaskFeeling.execute();
			break;
		}
		return super.onContextItemSelected(item);
	}
	private class MyClick implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(),MainActivity.class);
				intent.putExtra("type", "2");
				startActivity(intent);
				finish();
				break;
				//TODO:ˢ��
			case R.id.right:
				BloggerAnalyseJson bloggerAnalyseJson=new BloggerAnalyseJson(new BloggerAnalyseView(),getApplicationContext());
				bloggerAnalyseJson.execute(analyseUrl);
				WeiboJson weiboJson=new WeiboJson(weibos, adapter,getApplicationContext());
				weiboJson.execute(weiboUrl);
				break;
			}
		}
	}
	public class BloggerAnalyseView{
		/**΢����*/
		private TextView weiboCountTextView=(TextView)findViewById(R.id.positive);
		/**��˿��*/
		private TextView followCountTextView=(TextView)findViewById(R.id.neutral);
		/**������*/
		private TextView friendCountTextView=(TextView)findViewById(R.id.negative);
		/**ת����ϵ����*/
		private TextView retweetCountTextView=(TextView)findViewById(R.id.zombie);
		/**@��ϵ����*/
		private TextView notifyTextView=(TextView)findViewById(R.id.blogger);
		public BloggerAnalyseView() {
			weiboCountTextView.setText("΢����"+blogger.getWeiboCount());
			followCountTextView.setText("��˿��"+blogger.getFollowersCount());
			friendCountTextView.setText("������"+blogger.getFriendsCount());
			retweetCountTextView.setText("ת��΢��:"+"");
			notifyTextView.setText("@΢����"+"");
		}
		public TextView getFollowCountTextView() {
			return followCountTextView;
		}
		public TextView getFriendCountTextView() {
			return friendCountTextView;
		}
		public TextView getWeiboCountTextView() {
			return weiboCountTextView;
		}
		public TextView getNotifyTextView() {
			return notifyTextView;
		}
		public TextView getRetweetCountTextView() {
			return retweetCountTextView;
		}
	}
}

