package com.vmojing.feeling;

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
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("InflateParams")
public class FeelingBasic extends Fragment implements DragListView.OnRefreshLoadingMoreListener{
	private Context context;
	private View view;

	private DragListView listView;
	private List<Feeling>feelingsChange=new ArrayList<Feeling>();
	private List<Feeling>feelings=new ArrayList<Feeling>();
	private FeelingAdapter adapter;

	private PopUpFilter myPopUpWindow;
	private PopupWindow popupWindow;
	private MyAppplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context=this.getActivity();
		view = inflater.inflate(R.layout.fragment_center, null);
		listView=(DragListView)view.findViewById(R.id.showlsitview);
		adapter=new FeelingAdapter(context, feelingsChange);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		myApp=(MyAppplication)context.getApplicationContext();
		myPopUpWindow=new PopUpFilter(context);
		popupWindow=myPopUpWindow.getPopupWindow();
		//TODO　初始化
		String initUrl="http://10.23.0.102:8080/vmojing-web/opinion/updateList?uid="
				+ myApp.getUsername();
		FeelingBasicJson jsonData=new FeelingBasicJson(feelings,feelingsChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**初始化头部菜单*/
		FeelingBasic fragment=new FeelingBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		registerForContextMenu(listView);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("舆情操作");
		menu.add(0,Menu.FIRST,0,"标记为已处理");
		menu.add(0,Menu.FIRST+1,0,"标记为未处理");
		menu.add(0,Menu.FIRST+2,0,"标记为忽略");
		menu.add(0,Menu.FIRST+3,0,"发送邮件通知");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		case 1:
			/**标记为已处理*/
			feelingsChange.get(position).setStatus(0);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(0);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO 修改数据库中舆情状态
			String url1="http://10.23.0.102:8080/vmojing-web/opinion/transfer";
			String jsonData1="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ feelingsChange.get(position).getId()
					+ "\",\"os\":"
					+ 2
					+ "\"}";
			PostAsyncTask asyncTask1=new PostAsyncTask(context,url1,jsonData1);
			asyncTask1.execute();
			break;
		case 2:
			/**标记为未处理*/
			feelingsChange.get(position).setStatus(1);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO 修改数据库中舆情状态
			String url2="http://10.23.0.102:8080/vmojing-web/opinion/transfer";
			String jsonData2="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ feelingsChange.get(position).getId()
					+ "\",\"os\":"
					+ 1
					+ "\"}";
			PostAsyncTask asyncTask2=new PostAsyncTask(context,url2,jsonData2);
			asyncTask2.execute();
		case 3:
			/**标记为忽略*/
			feelings.get(position).setStatus(2);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO 修改数据库中舆情状态
			String url3="http://10.23.0.102:8080/vmojing-web/opinion/transfer";
			String jsonData3="{\"uid\":\""
					+ myApp.getUsername()
					+ "\",\"itid\":\""
					+ feelingsChange.get(position).getId()
					+ "\",\"os\":"
					+ 3
					+ "\"}";
			PostAsyncTask asyncTask3=new PostAsyncTask(context,url3,jsonData3);
			asyncTask3.execute();
			break;
		case 4:
			/**发送邮件通知，同时修改舆情状态为已处理*/
			SharedPreferences sharedPreferences=context.getSharedPreferences("setmail", 0);
			String mail=sharedPreferences.getString("mail", "");
			Uri uri = Uri.parse("mailto:"+mail);
			Intent intent = new Intent(context, getClass());
			intent.setAction(Intent.ACTION_SENDTO);
			intent.setData(uri);
			intent.putExtra(Intent.EXTRA_SUBJECT, "微博舆情"); // 主题
			intent.putExtra(Intent.EXTRA_TEXT, feelings.get(position).getWeibo().getContent()); // 正文
			startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
			/**修改舆情的状态*/
			feelingsChange.get(position).setStatus(1);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private class MyClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			case R.id.right:
				myPopUpWindow.getStatusGroup().setVisibility(RadioGroup.GONE);
				myPopUpWindow.getStatus().setVisibility(TextView.GONE);
				myPopUpWindow.getType().setText("舆情属性");
				myPopUpWindow.getTypeAllButton().setText("全部舆情");
				myPopUpWindow.getTypeDoneButton().setText("已处理舆情");
				myPopUpWindow.getTypeDoButton().setText("待处理舆情");
				myPopUpWindow.getTypeIgnoreButton().setText("已忽略舆情");
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						feelingsChange.clear();
						for(int i=0;i<feelings.size();i++){
							feelingsChange.add(feelings.get(i));
						}
						feelingsChange=getFeelings(myPopUpWindow.getTypeButton().getText().toString());
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
	public List<Feeling> getFeelings(String status){
		String statusarr[]={"已处理舆情","待处理舆情","已忽略舆情"};
		int s=-1;
		for(int i=0;i<statusarr.length;i++)
		{
			if(status.equals(statusarr[i]))
				s=i;
		}
		if(status.equals("全部舆情"))
			return feelings;
		else
		{
			for(int i=0;i<feelings.size();i++)
			{
				if(feelings.get(i).getStatus()!=s)
				{
					feelings.remove(i);
					i--;
				}
			}
			return feelings;
		}
	}
	
	@Override
	public void onRefresh() {
		String updateUrl="http://10.23.0.102:8080/vmojing-web/opinion/updateList?uid="
				+ myApp.getUsername()
				+"&timestamp="
				+feelings.get(0).getTimeStamp()
				+"&direction=1";
		String params[]={updateUrl,""+DragListView.DRAG_INDEX};
		FeelingBasicJson feelingBasicJson=new FeelingBasicJson(feelings, feelingsChange,adapter,listView,context);
		feelingBasicJson.execute(params);
	}

	@Override
	public void onLoadMore() {
		String moreUrl="http://10.23.0.102:8080/vmojing-web/opinion/updateList?uid="
				+ myApp.getUsername()
				+"&timestamp="
				+feelings.get(feelings.size()-1).getTimeStamp()
				+"&direction=-1";
		String params[]={moreUrl,""+DragListView.LOADMORE_INDEX};
		FeelingBasicJson feelingBasicJson=new FeelingBasicJson(feelings, feelingsChange,adapter,listView, context);
		feelingBasicJson.execute(params);
	}
}
