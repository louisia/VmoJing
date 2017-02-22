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
		/**��ʼ��ͷ���˵�*/
		clue=(Clue)getIntent().getSerializableExtra("clue");
		MenuTop menuTop=new MenuTop(this,clue.getWeibo().getContent());
		menuTop.getLeftBtn().setOnClickListener(new MyClick());
		menuTop.getCenterBtn().setVisibility(ImageView.GONE);;
		menuTop.getRightBtn().setOnClickListener(new MyClick());
		dragListView=(DragListView)findViewById(R.id.showlsitview);
		dragListView.setVisibility(ListView.GONE);
		myApp=(MyAppplication)getApplicationContext();
		/**��ʼ��ListView*/
		listView=(ListView)findViewById(R.id.listview);
		adapter=new ComRetAdapter(getApplicationContext(), comRets);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		//TODO ��ȡ΢��������������
		analyseUrl="http://10.23.0.102:8080/vmojing-web/clue/statistic?cid="
				+ clue.getId()
				+ "&timestamp="
				+ clue.getMonitorAt();
		ClueAnalyseJson clueAnalyseJson=new ClueAnalyseJson(new ClueAnalyseView(),getApplicationContext());
		clueAnalyseJson.execute(analyseUrl);
		//TODO ��ȡ΢����������ת���б�
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
		menu.setHeaderTitle("ת�����۲���");
		menu.add(0,Menu.FIRST,0,"����Ϊ����");
		menu.add(0, Menu.FIRST+1, 0, "�����û�");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		//TODO:����Ϊ΢������
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
				Toast.makeText(getApplicationContext(), "�������ۣ���������Ϊ����", Toast.LENGTH_SHORT).show();
			}
			break;
		case 2:
			//TODO �����û�
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
		/**����������*/
		private TextView positiveTextView=(TextView)findViewById(R.id.positive);
		/**����������*/
		private TextView neutralTextView=(TextView)findViewById(R.id.neutral);
		/**����������*/
		private TextView negativeTextView=(TextView)findViewById(R.id.negative);
		/**ˮ����*/
		private TextView zombieTextView=(TextView)findViewById(R.id.zombie);
		/**������*/
		private TextView bloggerTextView=(TextView)findViewById(R.id.blogger);
		public ClueAnalyseView() {
			positiveTextView.setText("��������:");
			negativeTextView.setText("��������:");
			neutralTextView.setText("��������:");
			zombieTextView.setText("ˮ����:");
			bloggerTextView.setText("������:");
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
