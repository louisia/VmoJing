package com.vmojing.topic;

import java.util.ArrayList;
import java.util.List;















import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
import com.vmojing.base.Weibo;
import com.vmojing.base.WeiboAdapter;
import com.vmojing.base.WeiboJson;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.DragListView;
import com.vmojing.ui.MenuTop;

public class TopicAnalyse extends Activity {
	private DragListView dragListView;
	private ListView listView;
	
	private List<Weibo> weibos=new ArrayList<Weibo>();
	private WeiboAdapter adapter;
	private LinearLayout analyseLayout;
	
	private String analyseUrl;
	private String weiboUrl;
	
	private MyAppplication myApp;
	@SuppressLint({ "InlinedApi", "InflateParams" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_center);
		/**初始化头部菜单*/
		MenuTop menuTop=new MenuTop(this, 
				((Topic)getIntent().getSerializableExtra("topic")).getName());
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageView.GONE);;
		menuTop.getRightBtn().setOnClickListener(new MyClick());
		dragListView=(DragListView)findViewById(R.id.showlsitview);
		dragListView.setVisibility(ListView.GONE);
		/**初始化ListView*/
		listView=(ListView)findViewById(R.id.listview);
		adapter=new WeiboAdapter(getApplicationContext(), weibos);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		
		analyseLayout=(LinearLayout)findViewById(R.id.analyse);
		analyseLayout.setVisibility(LinearLayout.VISIBLE);
		myApp=(MyAppplication)getApplicationContext();
		//TODO 从网络请求话题的正面舆情、负面舆情、中立舆情、水军、博主信息
		analyseUrl="http://10.23.0.102:8080/vmojing-web/topic/statistic?tid="
				+ ((Topic)getIntent().getSerializableExtra("topic")).getId()
				+ "&timestamp="
				+ ((Topic)getIntent().getSerializableExtra("topic")).getMonitorAt();
		TopicAnalyseJson topicAnalyseJson=new TopicAnalyseJson(new TopicAnalyseView(),getApplicationContext());
		topicAnalyseJson.execute(analyseUrl);
		
		//TODO 从网络中请求话题相关微博列表
		weiboUrl="http://10.23.0.102:8080/vmojing-web/topic/weiboList?tid="
				+ ((Topic)getIntent().getSerializableExtra("topic")).getId()
				+ "&timestamp="
				+ ((Topic)getIntent().getSerializableExtra("topic")).getMonitorAt();
		WeiboJson weiboJson=new WeiboJson(weibos, adapter,getApplicationContext());
		weiboJson.execute(weiboUrl);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("微博操作");
		menu.add(0, Menu.FIRST, 0, "设置为线索");
		menu.add(0, Menu.FIRST+1, 0, "加入舆情库");
		menu.add(0, Menu.FIRST+2, 0, "监测该博主");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id =weibos.get(position).getId();
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
					+ "\",\"wid\":\""
					+ id
					+ "\"}";
			PostAsyncTask asyncTaskFeeling=new PostAsyncTask(getApplicationContext(),createFeelingUrl, createFeelingContent);
			asyncTaskFeeling.execute();
			break;
		case 3:
			//TODO 监测该博主
			String createBloggerUrl="http://10.23.0.102:8080/vmojing-web/blogger/create";
			String createBloggerContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"name\":\""
					+ weibos.get(position).getUser().getName()
					+ "\"}";
			PostAsyncTask asyncTask=new PostAsyncTask(getApplicationContext(),createBloggerUrl, createBloggerContent);
			asyncTask.execute();
			break;
		
		}
		return super.onContextItemSelected(item);
	}
	/**
	 * 自定义click类
	 * @author clyx
	 *
	 */
	private class MyClick implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(),MainActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
				finish();
				break;
			case R.id.right:
				//TODO 从网络请求话题的正面舆情、负面舆情、中立舆情、水军、博主信息
				TopicAnalyseJson topicAnalyseJson=new TopicAnalyseJson(new TopicAnalyseView(),getApplicationContext());
				topicAnalyseJson.execute(analyseUrl);
				//TODO 从网络中请求话题相关微博列表
				WeiboJson weiboJson=new WeiboJson(weibos, adapter,getApplicationContext());
				weiboJson.execute(weiboUrl);
				break;
			}
		}
	}
	public class TopicAnalyseView{
		/**正面舆情数*/
		private TextView positiveTextView=(TextView)findViewById(R.id.positive);
		/**中立舆情数*/
		private TextView neutralTextView=(TextView)findViewById(R.id.neutral);
		/**负面舆情数*/
		private TextView negativeTextView=(TextView)findViewById(R.id.negative);
		/**水军数*/
		private TextView zombieTextView=(TextView)findViewById(R.id.zombie);
		/**博主数*/
		private TextView bloggerTextView=(TextView)findViewById(R.id.blogger);
		public TopicAnalyseView(){
			positiveTextView.setText("正面:");
			negativeTextView.setText("负面:");
			neutralTextView.setText("中立:");
			zombieTextView.setText("水军:");
			bloggerTextView.setText("博主:");
		}
		public TextView getBloggerTextView() {
			return bloggerTextView;
		}
		public TextView getNegativeTextView() {
			return negativeTextView;
		}
		public TextView getNeutralTextView() {
			return neutralTextView;
		}
		public TextView getPositiveTextView() {
			return positiveTextView;
		}
		public TextView getZombieTextView() {
			return zombieTextView;
		}
	}
}
