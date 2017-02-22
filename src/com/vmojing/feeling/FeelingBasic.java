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
		//TODO����ʼ��
		String initUrl="http://10.23.0.102:8080/vmojing-web/opinion/updateList?uid="
				+ myApp.getUsername();
		FeelingBasicJson jsonData=new FeelingBasicJson(feelings,feelingsChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**��ʼ��ͷ���˵�*/
		FeelingBasic fragment=new FeelingBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		registerForContextMenu(listView);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("�������");
		menu.add(0,Menu.FIRST,0,"���Ϊ�Ѵ���");
		menu.add(0,Menu.FIRST+1,0,"���Ϊδ����");
		menu.add(0,Menu.FIRST+2,0,"���Ϊ����");
		menu.add(0,Menu.FIRST+3,0,"�����ʼ�֪ͨ");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		case 1:
			/**���Ϊ�Ѵ���*/
			feelingsChange.get(position).setStatus(0);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(0);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO �޸����ݿ�������״̬
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
			/**���Ϊδ����*/
			feelingsChange.get(position).setStatus(1);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO �޸����ݿ�������״̬
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
			/**���Ϊ����*/
			feelings.get(position).setStatus(2);
			for(int i=0;i<feelings.size();i++){
				if(feelings.get(i).getId().equals(feelingsChange.get(position).getId())){
					feelings.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
			//TODO �޸����ݿ�������״̬
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
			/**�����ʼ�֪ͨ��ͬʱ�޸�����״̬Ϊ�Ѵ���*/
			SharedPreferences sharedPreferences=context.getSharedPreferences("setmail", 0);
			String mail=sharedPreferences.getString("mail", "");
			Uri uri = Uri.parse("mailto:"+mail);
			Intent intent = new Intent(context, getClass());
			intent.setAction(Intent.ACTION_SENDTO);
			intent.setData(uri);
			intent.putExtra(Intent.EXTRA_SUBJECT, "΢������"); // ����
			intent.putExtra(Intent.EXTRA_TEXT, feelings.get(position).getWeibo().getContent()); // ����
			startActivity(Intent.createChooser(intent, "��ѡ���ʼ���Ӧ��"));
			/**�޸������״̬*/
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
				myPopUpWindow.getType().setText("��������");
				myPopUpWindow.getTypeAllButton().setText("ȫ������");
				myPopUpWindow.getTypeDoneButton().setText("�Ѵ�������");
				myPopUpWindow.getTypeDoButton().setText("����������");
				myPopUpWindow.getTypeIgnoreButton().setText("�Ѻ�������");
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
		String statusarr[]={"�Ѵ�������","����������","�Ѻ�������"};
		int s=-1;
		for(int i=0;i<statusarr.length;i++)
		{
			if(status.equals(statusarr[i]))
				s=i;
		}
		if(status.equals("ȫ������"))
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
