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
 * ��������
 * �����id������ı��⡢�������΢����������΢���������������͡��������ࡢ���ⴴ��ʱ��
 * ���ݻ������͡�����鿴���⡢����ɾ�����⡢ˢ�»�����𡢲鿴���ӻ���
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
	 * ����view
	 */
	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		/**��ʼ��*/
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
		//TODO �����ʼ��
		String initUrl="http://10.23.0.102:8080/vmojing-web/topic/updateList?uid="
				+ myApp.getUsername();
		TopicBasicJson jsonData=new TopicBasicJson(topics,topicsChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**��ʼ��ͷ���˵�*/
		TopicBasic fragment=new TopicBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getCenterBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		
		/**listView����¼�*/
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
		menu.setHeaderTitle("�������");
		menu.add(0, Menu.FIRST, 0, "ɾ���û���");
		menu.add(0, Menu.FIRST+1, 0, "�޸Ļ���״̬");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=topicsChange.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:ɾ������
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
			//TODO:�޸Ļ���״̬
			
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
							//TODO ��ӻ���
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
	 * �����û����ã���ѡ�����ʾ����
	 * @param type:��������ͣ��쵼������
	 * @param status�������״̬���������쳣
	 * @return �����������õĻ���
	 */
	public List<Topic> getTopics(String type,String status){
		int s=-1,t=-1;
		String statusarr[]={"��������","�쳣����"};
		String typearr[]={"�쵼����","���Ż���","���໰��"};
		for(int i=0;i<statusarr.length;i++){
			if(status.equals(statusarr[i]))
				s=i;
		}
		for(int i=0;i<typearr.length;i++){
			if(type.equals(typearr[i]))
				t=i;
		}
		if(type.equals("ȫ������")&&!status.equals("ȫ������")){
			for(int i=0;i<topics.size();i++)
			{
				if(topics.get(i).getStatus()!=s)
				{
					topics.remove(i);
					i--;
				}
			}
		}
		if(!type.equals("ȫ������")&&status.equals("ȫ������")){
			for(int i=0;i<topics.size();i++)
			{
				if(topics.get(i).getType()!=t)
				{
					topics.remove(i);
					i--;
				}
			}
		}
		if(!type.equals("ȫ������")&&!status.equals("ȫ������")){
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
		//TODO ˢ�»���
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
		//TODO ���ظ��໰��
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



