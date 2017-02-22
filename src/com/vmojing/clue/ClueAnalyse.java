package com.vmojing.clue;

import java.util.ArrayList;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class ClueAnalyse extends Activity {
	private DragListView dragListView;
	private ListView listView;
	private List<ComRet> comRets=new ArrayList<ComRet>();
	private ComRetAdapter adapter;	
	private String analyseUrl;
	private String comRetUrl;
	private Clue clue;
	private MyAppplication myApp;

	@SuppressLint({ "InlinedApi", "InflateParams" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_center);
		/**初始化头部菜单*/
		clue=(Clue)getIntent().getSerializableExtra("clue");
		MenuTop menuTop=new MenuTop(this,clue.getWeibo().getContent());
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageView.GONE);;
		menuTop.getRightBtn().setOnClickListener(new MyClick());
		dragListView=(DragListView)findViewById(R.id.showlsitview);
		dragListView.setVisibility(ListView.GONE);
		myApp=(MyAppplication)getApplicationContext();
		/**初始化ListView*/
		listView=(ListView)findViewById(R.id.listview);
		adapter=new ComRetAdapter(getApplicationContext(), comRets);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		//TODO 获取微博线索分析数据
		analyseUrl="http://10.23.0.102:8080/vmojing-web/clue/statistic?cid="
				+ clue.getId()
				+ "&timestamp="
				+ clue.getMonitorAt();
		ClueAnalyseJson clueAnalyseJson=new ClueAnalyseJson(new ClueAnalyseView(),getApplicationContext());
		clueAnalyseJson.execute(analyseUrl);
		//TODO 获取微博线索评论转发列表
		comRetUrl="http://10.23.0.102:8080/vmojing-web/clue/detailList?uid="
				+ myApp.getUsername()
				+ "&cid="
				+ clue.getId()
				+ "&timestamp="
				+ clue.getMonitorAt();
		ComRetJson comRetJson=new ComRetJson(comRets, adapter,getApplicationContext());
		comRetJson.execute(comRetUrl);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("转发评论操作");
		menu.add(0,Menu.FIRST,0,"设置为线索");
		menu.add(0, Menu.FIRST+1, 0, "监测该用户");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		//TODO:设置为微博线索
		case 1:
			if(comRets.get(position).getReplyCommentId()!=null){
				String createClueUrl="http://10.23.0.102:8080/vmojing-web/clue/create";
				String createClueContent="{\"uid\":\""
						+ myApp.getUsername()
						+ "\",\"itid\":\""
						+ comRets.get(position).getReplyCommentId()
						+ "\"}";
				PostAsyncTask asyncTaskClue=new PostAsyncTask(getApplicationContext(),createClueUrl, createClueContent);
				asyncTaskClue.execute();
			}
			else{
				Toast.makeText(getApplicationContext(), "这是评论，不可设置为线索", Toast.LENGTH_SHORT).show();
			}
			break;
		case 2:
			//TODO 监测该用户
			String createBloggerUrl="http://10.23.0.102:8080/vmojing-web/blogger/create";
			String createBloggerContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"name\":\""
					+ comRets.get(position).getUser().getName()
					+ "\"}";
			PostAsyncTask asyncTask=new PostAsyncTask(getApplicationContext(),createBloggerUrl, createBloggerContent);
			asyncTask.execute();
			break;
		}
		return super.onContextItemSelected(item);
	}
	private class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(),MainActivity.class);
				intent.putExtra("type", "3");
				startActivity(intent);
				finish();
				break;
			case R.id.right:
				ClueAnalyseJson clueAnalyseJson=new ClueAnalyseJson(new ClueAnalyseView(),getApplicationContext());
				clueAnalyseJson.execute(analyseUrl);
				ComRetJson comRetJson=new ComRetJson(comRets, adapter,getApplicationContext());
				comRetJson.execute(comRetUrl);
				break;
			}
		}
	}

	public class ClueAnalyseView{
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
		public ClueAnalyseView() {
			positiveTextView.setText("正面舆情:");
			negativeTextView.setText("负面舆情:");
			neutralTextView.setText("中立舆情:");
			zombieTextView.setText("水军数:");
			bloggerTextView.setText("博主数:");
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
