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
		/**获取Intent传递的对象*/
		blogger=(Blogger)getIntent().getSerializableExtra("blogger");
		/**初始化头部菜单*/
		MenuTop menuTop=new MenuTop(this,blogger.getUser().getName());
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageView.GONE);
		menuTop.getRightBtn().setOnClickListener(new MyClick());
		/**初始化ListView*/
		dragListView=(DragListView)findViewById(R.id.showlsitview);
		dragListView.setVisibility(ListView.GONE);
		listView=(ListView)findViewById(R.id.listview);
		adapter=new WeiboAdapter(getApplicationContext(), weibos);
		listView.setAdapter(adapter);
		/**对ListView添加菜单监听器*/
		registerForContextMenu(listView);
		/**初始化分析数据*/
		analyseLayout=(LinearLayout)findViewById(R.id.analyse);
		analyseLayout.setVisibility(LinearLayout.VISIBLE);
		//TODO 获取转发@关系
		analyseUrl="http://10.23.0.102:8080/vmojing-web/blogger/relation?bid="
				+ blogger.getId()
				+ "&timestamp="
				+ blogger.getMonitorAt();
		BloggerAnalyseJson bloggerAnalyseJson=new BloggerAnalyseJson(new BloggerAnalyseView(),getApplicationContext());
		bloggerAnalyseJson.execute(analyseUrl);
		//TODO 获取博主微博列表
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
		menu.setHeaderTitle("微博操作");
		menu.add(0, Menu.FIRST, 0, "设置为线索");
		menu.add(0, Menu.FIRST+1, 0, "加入舆情库");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=weibos.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:设置为线索
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
			//TODO:加入舆情库
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
				//TODO:刷新
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
		/**微博数*/
		private TextView weiboCountTextView=(TextView)findViewById(R.id.positive);
		/**粉丝数*/
		private TextView followCountTextView=(TextView)findViewById(R.id.neutral);
		/**好友数*/
		private TextView friendCountTextView=(TextView)findViewById(R.id.negative);
		/**转发关系人数*/
		private TextView retweetCountTextView=(TextView)findViewById(R.id.zombie);
		/**@关系人数*/
		private TextView notifyTextView=(TextView)findViewById(R.id.blogger);
		public BloggerAnalyseView() {
			weiboCountTextView.setText("微博数"+blogger.getWeiboCount());
			followCountTextView.setText("粉丝数"+blogger.getFollowersCount());
			friendCountTextView.setText("好友数"+blogger.getFriendsCount());
			retweetCountTextView.setText("转发微博:"+"");
			notifyTextView.setText("@微博："+"");
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

