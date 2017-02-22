package com.vmojing.publics;


import java.util.ArrayList;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.DragListView;
import com.vmojing.ui.MenuTop;
import com.vmojing.ui.PopUpFilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.PopupWindow.OnDismissListener;
/**
 * 全网热点：
 * 		用于显示网络中的热点微博,这些微博都是原创微博
 * 		原始数据：博主头像、博主昵称、微博创建时间、微博内容、微博插图、转发数、评论数、点赞数
 * 		分析数据：预测转发、预测评论、预测点赞
 * 		操作：将某条热点微博设置为微博线索、加入舆情库、监测某个博主、上拉刷新、下拉加载更多
 * @author clyx
 *
 */
@SuppressLint({ "InflateParams", "NewApi" })
public class PublicsBasic  extends Fragment {
	private View view;            //页面布局
	private Context context;      //上下文控件
	private List<Publics>publics=new ArrayList<Publics>();//热点列表
	private ListView listView; //自定义ListView控件
	private PublicsAdapter adapter;    //数据适配器
	private MyAppplication myApp;      //全局对象
	private PopUpFilter myPopUpWindow;    //弹出式菜单
	private PopupWindow popupWindow;
	/**
	 * 初始化成员变量
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_center, null);
		context=this.getActivity();
		myApp=(MyAppplication)context.getApplicationContext();
		adapter=new PublicsAdapter(context, publics);
		listView=(DragListView)view.findViewById(R.id.showlsitview);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		myPopUpWindow=new PopUpFilter(context);
		popupWindow=myPopUpWindow.getPopupWindow();
		//TODO 初始化
		String initUrl="http://10.23.0.102:8080/vmojing-web/hot/weibo?"
				+ "timeOpt=1";
		PublicsBasicJson jsonData=new PublicsBasicJson(publics, adapter, context);
		jsonData.execute(initUrl);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**初始化头部菜单*/
		PublicsBasic fragment=new PublicsBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
	}
	/**
	 * 设置弹出ContextMenu的内容
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("热点操作");
		menu.add(0,Menu.FIRST,0,"设置为线索");
		menu.add(0,Menu.FIRST+1,0,"加入舆情库");
		menu.add(0,Menu.FIRST+2,0,"监测该博主");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	/**
	 * 设置ContextMenu中item点击事件
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		case 1:
			//TODO:设置为线索
			String createClueUrl="http://10.23.0.102:8080/vmojing-web/clue/create";
			String createClueContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ publics.get(position).getWeibo().getId()
					+ "\"}";
			PostAsyncTask asyncTaskClue=new PostAsyncTask(context,createClueUrl, createClueContent);
			asyncTaskClue.execute();
			break;
		case 2:
			//TODO:加入舆情库
			String createFeelingUrl="http://10.23.0.102:8080/vmojing-web/opinion/create";
			String createFeelingContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ publics.get(position).getWeibo().getId()
					+ "\"}";
			PostAsyncTask asyncTaskFeeling=new PostAsyncTask(context,createFeelingUrl, createFeelingContent);
			asyncTaskFeeling.execute();
			break;
		case 3:
			//TODO 监测该博主
			String createBloggerUrl="http://10.23.0.102:8080/vmojing-web/blogger/create";
			String createBloggerContent="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"name\":\""
					+ publics.get(position).getWeibo().getUser().getName()
					+ "\"}";
			PostAsyncTask asyncTask=new PostAsyncTask(context,createBloggerUrl, createBloggerContent);
			asyncTask.execute();
			break;
		}
		return super.onContextItemSelected(item);
	}
	/**
	 * 自定义类捕捉控件点击事件
	 * @author clyx
	 *
	 */
	private class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			case R.id.right:
				myPopUpWindow.getTypeGroup().setVisibility(RadioGroup.GONE);
				myPopUpWindow.getType().setVisibility(TextView.GONE);
				myPopUpWindow.getStatus().setText("时间节点");
				myPopUpWindow.getStatusAllButton().setText("0——3h");
				myPopUpWindow.getStatusnormalButton().setText("3——6h");
				myPopUpWindow.getStatusabnormalButton().setText("6——9h");
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						//TODO 按设置筛选热点微博
						publics.clear();
						int timeOpt=0;
						if(myPopUpWindow.getStatusButton().getText().toString().equals("0——3h"))
							timeOpt=1;
						else if(myPopUpWindow.getStatusButton().getText().toString().equals("3——6h"))
							timeOpt=2;
						else 
							timeOpt=3;
						String updateUrl="http://10.23.0.102:8080/vmojing-web/hot/weibo?"
								+ "&timeOpt="
								+ timeOpt;
						PublicsBasicJson task=new PublicsBasicJson(publics, adapter, context);
						task.execute(updateUrl);
					}
				});
				if(popupWindow.isShowing())
					popupWindow.dismiss();
				else
					popupWindow.showAsDropDown(v,10,10); 
				break;
			default:
				break;
			}
		}
	}
}
