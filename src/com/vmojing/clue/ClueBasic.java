package com.vmojing.clue;

import java.util.ArrayList;
import java.util.List;

import com.vmojing.R;
import com.vmojing.base.MyAppplication;
import com.vmojing.base.PostAsyncTask;
import com.vmojing.login.MainActivity;
import com.vmojing.ui.DragListView;
import com.vmojing.ui.PopUpFilter;
import com.vmojing.ui.MenuTop;

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
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("InflateParams")
public class ClueBasic extends Fragment implements DragListView.OnRefreshLoadingMoreListener {
	private View view;
	private Context context;

	private DragListView listView;
	private List<Clue>cluesChange =new ArrayList<Clue>();
	private List<Clue>clues=new ArrayList<Clue>();
	private ClueAdapter adapter;
	
	private PopUpFilter myPopUpWindow;
	private PopupWindow popupWindow;
	
	private MyAppplication myApp;
	/**
	 * 创建view
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_center, null);
		context=this.getActivity();
		listView=(DragListView)view.findViewById(R.id.showlsitview);
		adapter=new ClueAdapter(context, cluesChange);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		registerForContextMenu(listView);
		myApp=(MyAppplication)context.getApplicationContext();
		myPopUpWindow=new PopUpFilter(context);
		popupWindow=myPopUpWindow.getPopupWindow();
		//TODO 初始化
		String initUrl="http://10.23.0.102:8080/vmojing-web/clue/updateList?uid=test";
		ClueBasicJson jsonData=new ClueBasicJson(clues,cluesChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}
	/**
	 * 创建Activity
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**初始化头部菜单*/
		ClueBasic fragment=new ClueBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getCenterBtn().setVisibility(Button.GONE);;
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		/**listView点击事件*/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					Intent intent =new Intent();
					intent.setClass(context, ClueAnalyse.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("clue", cluesChange.get(arg2-1));
					intent.putExtras(bundle);
					startActivity(intent);
				}
		});

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("线索操作");
		menu.add(0, Menu.FIRST, 0, "删除该线索");
		menu.add(0, Menu.FIRST+1, 0, "修改线索状态");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=cluesChange.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:删除线索
			String deleteUrl="http://10.23.0.102:8080/vmojing-web/clue/transfer";
			String jsonData="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ id
					+ "\",\"os\":"
					+ 0
					+ "\"}";
			PostAsyncTask asyncTask=new PostAsyncTask(context,deleteUrl,jsonData);
			asyncTask.execute();
			for(int j=0;j<clues.size();j++){
				if(id.equals(clues.get(j).getId())){
					clues.remove(j);
					break;
				}
			}
			cluesChange.remove(position);
			adapter.notifyDataSetChanged();
			break;
		case 2:
			//TODO:修改线索状态
			
			break;
		}
		return super.onContextItemSelected(item);
	}
	private class MyClick  implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			case R.id.right:
				myPopUpWindow.getTypeGroup().setVisibility(RadioGroup.GONE);
				myPopUpWindow.getType().setVisibility(TextView.GONE);
				myPopUpWindow.getStatus().setText("线索属性");
				myPopUpWindow.getStatusAllButton().setText("全部线索");
				myPopUpWindow.getStatusnormalButton().setText("正常线索");
				myPopUpWindow.getStatusabnormalButton().setText("异常线索");
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						cluesChange.clear();
						for(int i=0;i<clues.size();i++){
							cluesChange.add(clues.get(i));
						}
						cluesChange=getClues(myPopUpWindow.getStatusButton().getText().toString());
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
	public List<Clue> getClues(String status){
		String statusarr[]={"正常线索","异常线索"};
		int s=-1;
		for(int i=0;i<statusarr.length;i++)
		{
			if(status.equals(statusarr[i]))
				s=i;
		}
		if(status.equals("全部线索"))
			return clues;
		else
		{
			for(int i=0;i<clues.size();i++)
			{
				if(clues.get(i).getStatus()!=s)
				{
					clues.remove(i);
					i--;
				}
			}
			return clues;
		}
	}
	@Override
	public void onRefresh() {
		//TODO 刷新微博线索
		String updateUrl="http://10.23.0.102:8080/vmojing-web/clue/updateList?uid"
				+ myApp.getUsername()
				+ "&timestamp="
				+ clues.get(0).getMonitorAt()
				+ "&direction=1";
		String params[]={updateUrl,""+DragListView.DRAG_INDEX};
		ClueBasicJson clueJson=new ClueBasicJson(clues, cluesChange, adapter,listView,context);
		clueJson.execute(params);
		
	}
	@Override
	public void onLoadMore() {
		//TODO 加载更多微博线索
		String moreUrl="http://10.23.0.102:8080/vmojing-web/clue/updateList?uid"
				+ myApp.getUsername()
				+ "&timestamp="
				+ clues.get(clues.size()-1).getMonitorAt()
				+ "&direction=-1";
		String params[]={moreUrl,""+DragListView.LOADMORE_INDEX};
		ClueBasicJson clueJson=new ClueBasicJson(clues, cluesChange,adapter,listView,context);
		clueJson.execute(params);
	}

}
