package com.vmojing.topic;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.DragListView;
import com.vmojing.ui.PopUpFilter;
import com.vmojing.ui.MenuTop;

/**
 * 
 * @author clyx
 * 界面数据
 * 话题的id，话题的标题、话题今日微博量、话题微博总量、话题类型、话题种类、话题创建时间
 * 根据话题类型、种类查看话题、批量删除话题、刷新话题类别、查看增加话题
 */
public class TopicBasic extends Fragment implements DragListView.OnRefreshLoadingMoreListener{
	private Context context;
	private View view;
	private DragListView listView;
	private List<Topic>topicsChange=new ArrayList<Topic>();
	private List<Topic>topics=new ArrayList<Topic>();
	private TopicAdapter adapter;
	private PopUpFilter myPopUpWindow;
	private PopupWindow popupWindow;
	private MyAppplication myApp;
	/**
	 * 创建view
	 */
	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		/**初始化*/
		context=this.getActivity();
		view = inflater.inflate(R.layout.fragment_center, null);
		adapter=new TopicAdapter(context, topicsChange);
		listView=(DragListView)view.findViewById(R.id.showlsitview);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		myPopUpWindow=new PopUpFilter(context);
		popupWindow=myPopUpWindow.getPopupWindow();
		myApp=(MyAppplication)context.getApplicationContext();
		registerForContextMenu(listView);
		//TODO 话题初始化
		String initUrl="http://10.23.0.102:8080/vmojing-web/topic/updateList?uid="
				+ myApp.getUsername();
		TopicBasicJson jsonData=new TopicBasicJson(topics,topicsChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**初始化头部菜单*/
		TopicBasic fragment=new TopicBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getCenterBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		
		/**listView点击事件*/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent intent =new Intent();
					intent.setClass(context, TopicAnalyse.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("topic", topicsChange.get(arg2-1));
					intent.putExtras(bundle);
					startActivity(intent);
			}
		});
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("话题操作");
		menu.add(0, Menu.FIRST, 0, "删除该话题");
		menu.add(0, Menu.FIRST+1, 0, "修改话题状态");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=topicsChange.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:删除话题
			String deleteUrl="http://10.23.0.102:8080/vmojing-web/topic/transfer";
			String jsonData="{\"uid\":\""
					+myApp.getUsername()
					+ "\",\"itid\":\""
					+ id
					+ "\",\"os\":"
					+ 0
					+ "}";
			PostAsyncTask asyncTask=new PostAsyncTask(context,deleteUrl,jsonData);
			asyncTask.execute();
			for(int j=0;j<topics.size();j++){
				if(id.equals(topics.get(j).getId())){
					topics.remove(j);
					break;
				}
			}
			topicsChange.remove(position);
			adapter.notifyDataSetChanged();
			break;
		case 2:
			//TODO:修改话题状态
			
			break;
		}
		return super.onContextItemSelected(item);
	}
	private class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			case R.id.center:
				final LinearLayout layout=(LinearLayout)view.findViewById(R.id.menu_add);
				final LinearLayout topicLayout=(LinearLayout)layout.findViewById(R.id.add_topic);
				if(topicLayout.getVisibility()==LinearLayout.VISIBLE)
					topicLayout.setVisibility(LinearLayout.GONE);
				else{
					topicLayout.setVisibility(LinearLayout.VISIBLE);
					Button addBtn=(Button)layout.findViewById(R.id.topic_add);
					addBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//TODO 添加话题
							TextView keywordTextView=(TextView)layout.findViewById(R.id.topic_keyword);
							String createUrl="http://10.23.0.102:8080/vmojing-web/topic/create";
							String createContent="{\"uid\":\""
									+ myApp.getUsername()
									+ "\",\"name\":\""
									+ keywordTextView.getText().toString()
									+ "\"}";
							PostAsyncTask asyncTask=new PostAsyncTask(context,createUrl, createContent);
							asyncTask.execute();
							keywordTextView.setText("");
							topicLayout.setVisibility(LinearLayout.GONE);
						}
					});
				}
				break;
			case R.id.right:
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						topicsChange.clear();
						for(int i=0;i<topics.size();i++)
							topicsChange.add(topics.get(i));
						topicsChange=getTopics(myPopUpWindow.getTypeButton().getText().toString(),myPopUpWindow.getStatusButton().getText().toString());
						adapter.notifyDataSetChanged();	
					}
				});
				if(popupWindow.isShowing())
					popupWindow.dismiss();
				else
					popupWindow.showAsDropDown(v,10,10); 
				break;
			}
		}
	}
	/**
	 * 根据用户设置，有选择的显示话题
	 * @param type:话题的类型，领导、部门
	 * @param status：话题的状态，正常、异常
	 * @return 满足条件设置的话题
	 */
	public List<Topic> getTopics(String type,String status){
		int s=-1,t=-1;
		String statusarr[]={"正常话题","异常话题"};
		String typearr[]={"领导话题","部门话题","其余话题"};
		for(int i=0;i<statusarr.length;i++){
			if(status.equals(statusarr[i]))
				s=i;
		}
		for(int i=0;i<typearr.length;i++){
			if(type.equals(typearr[i]))
				t=i;
		}
		if(type.equals("全部话题")&&!status.equals("全部话题")){
			for(int i=0;i<topics.size();i++)
			{
				if(topics.get(i).getStatus()!=s)
				{
					topics.remove(i);
					i--;
				}
			}
		}
		if(!type.equals("全部话题")&&status.equals("全部话题")){
			for(int i=0;i<topics.size();i++)
			{
				if(topics.get(i).getType()!=t)
				{
					topics.remove(i);
					i--;
				}
			}
		}
		if(!type.equals("全部话题")&&!status.equals("全部话题")){
			for(int i=0;i<topics.size();i++)
			{
				if(topics.get(i).getType()!=t||topics.get(i).getStatus()!=s)
				{
					topics.remove(i);
					i--;
				}
			}
		}
		return topics;
	}

	@Override
	public void onRefresh() {
		//TODO 刷新话题
		String updateUrl="http://10.23.0.102:8080/vmojing-web/topic/updateList?uid="
				+ myApp.getUsername()
				+ "&timestamp="
				+ topics.get(0).getMonitorAt()
				+ "&direction=1";
		String params[]={updateUrl,""+DragListView.DRAG_INDEX};
		TopicBasicJson topicJson=new TopicBasicJson(topics, topicsChange, adapter,listView,context);
		topicJson.execute(params);
	}
	@Override
	public void onLoadMore() {
		//TODO 加载更多话题
		String moreUrl="http://10.23.0.102:8080/vmojing-web/topic/updateList?uid="
				+ myApp.getUsername()
				+ "&timestamp="
				+ topics.get(topics.size()-1).getMonitorAt()
				+ "&direction=-1";
		String params[]={moreUrl,""+DragListView.LOADMORE_INDEX};
		TopicBasicJson topicJson=new TopicBasicJson(topics, topicsChange, adapter,listView,context);
		topicJson.execute(params);
	}
}



