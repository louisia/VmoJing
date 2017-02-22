package com.vmojing.blogger;

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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
@SuppressLint("InflateParams")
public class BloggerBasic extends Fragment implements DragListView.OnRefreshLoadingMoreListener{
	private Context context;
	private View view;
	private PopUpFilter myPopUpWindow;
	private PopupWindow popupWindow;
	private DragListView listView;

	private List<Blogger>bloggersChange=new ArrayList<Blogger>();
	private List<Blogger>bloggers=new ArrayList<Blogger>();
	private BloggerAdapter adapter;
	private MyAppplication myApp;
	/**
	 * ����view
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		context=this.getActivity();
		view = inflater.inflate(R.layout.fragment_center, null);
		myPopUpWindow=new PopUpFilter(context);
		popupWindow=myPopUpWindow.getPopupWindow();

		listView=(DragListView)view.findViewById(R.id.showlsitview);
		adapter=new BloggerAdapter(context, bloggersChange);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		registerForContextMenu(listView);
		
		myApp=(MyAppplication)context.getApplicationContext();
		//TODO ��ʼ������
		String initUrl="http://10.23.0.102:8080/vmojing-web/blogger/updateList?uid="
				+ myApp.getUsername();
		BloggerBasicJson jsonData=new BloggerBasicJson(bloggers,bloggersChange,adapter,listView,context);
		jsonData.execute(initUrl);
		return view;
	}
	/**
	 * ����Activity
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**��ʼ��ͷ���˵�*/
		BloggerBasic fragment=new BloggerBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getCenterBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
		/**����listView����¼�*/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					/**��ת��������������*/
					Intent intent =new Intent();
					intent.setClass(context, BloggerAnalyse.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("blogger", bloggersChange.get(arg2-1));
					intent.putExtras(bundle);
					startActivity(intent);
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("��������");
		menu.add(0, Menu.FIRST, 0, "ɾ���ò���");
		menu.add(0, Menu.FIRST+1, 0, "�޸Ĳ���״̬");
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		String id=bloggersChange.get(position).getId();
		switch(item.getItemId()){
		case 1:
			//TODO:ɾ������
			String deleteUrl="http://10.23.0.102:8080/vmojing-web/blogger/transfer";
			String jsonData="{\"uid\":\""
					+myApp.getUsername()
					+ "\",\"itid\":\""
					+ id
					+ "\",\"os\":"
					+ 0
					+ "}";
			PostAsyncTask asyncTask=new PostAsyncTask(context,deleteUrl,jsonData);
			asyncTask.execute();
			for(int j=0;j<bloggers.size();j++){
				if(id.equals(bloggers.get(j).getId())){
					bloggers.remove(j);
					break;
				}
			}
			bloggersChange.remove(position);
			adapter.notifyDataSetChanged();
			break;
		case 2:
			//TODO:�޸Ĳ���״̬
			
			break;
		}
		return super.onContextItemSelected(item);
	}

	/***
	 * 
	 * @author clyx
	 * �˵�����ť������
	 */
	private class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.left:
				((MainActivity) getActivity()).showLeft();
				break;
			case R.id.center:
				final LinearLayout layout=(LinearLayout)view.findViewById(R.id.menu_add);
				final LinearLayout bloggerLayout=(LinearLayout)layout.findViewById(R.id.add_blogger);
				if(bloggerLayout.getVisibility()==LinearLayout.VISIBLE)
					bloggerLayout.setVisibility(LinearLayout.GONE);
				else{
					bloggerLayout.setVisibility(LinearLayout.VISIBLE);
					Button addBtn=(Button)layout.findViewById(R.id.blogger_add);
					addBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO ���Ӳ�����¼
							TextView keywordTextView=(TextView)layout.findViewById(R.id.blogger_keyword);
							String createUrl="http://10.23.0.102:8080/vmojing-web/blogger/create";
							String createContent="{\"uid\":\""
									+ myApp.getUsername()
									+ "\",\"name\":\""
									+ keywordTextView.getText().toString()
									+ "\"}";
							PostAsyncTask asyncTask=new PostAsyncTask(context,createUrl, createContent);
							asyncTask.execute();
							keywordTextView.setText("");
							bloggerLayout.setVisibility(LinearLayout.GONE);
						}
					});
				}
				break;
			case R.id.right:
				/**ѡ���Բ鿴����*/
				myPopUpWindow.getTypeGroup().setVisibility(RadioGroup.GONE);
				myPopUpWindow.getType().setVisibility(TextView.GONE);
				myPopUpWindow.getStatus().setText("��������");
				myPopUpWindow.getStatusAllButton().setText("ȫ������");
				myPopUpWindow.getStatusnormalButton().setText("��������");
				myPopUpWindow.getStatusabnormalButton().setText("�쳣����");
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						bloggersChange.clear();
						for(int i=0;i<bloggers.size();i++)
							bloggersChange.add(bloggers.get(i));
						bloggersChange=getBloggers(myPopUpWindow.getStatusButton().getText().toString());
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
	 * �����û����ã���ѡ����ʾ����������
	 * @param status:����������״̬��ȫ�����쳣������
	 * @return:������������������ 
	 */
	private List<Blogger> getBloggers(String status){
		String statusarr[]={"��������","�쳣����"};
		int s=-1;
		for(int i=0;i<statusarr.length;i++)
		{
			if(status.equals(statusarr[i]))
				s=i;
		}
		if(status.equals("ȫ������"))
			return bloggers;
		else
		{
			for(int i=0;i<bloggers.size();i++)
			{
				if(bloggers.get(i).getStatus()!=s)
				{
					bloggers.remove(i);
					i--;
				}
			}
			return bloggers;
		}
	}

	@Override
	public void onRefresh() {
		//TODO ˢ�²����б�
		String updateUrl="http://10.23.0.102:8080/vmojing-web/blogger/updateList?uid="
				+ myApp.getUsername()
				+ "&timestamp="
				+ bloggers.get(0).getMonitorAt()
				+ "&direction=1";
		String params[]={updateUrl,""+DragListView.DRAG_INDEX};
		BloggerBasicJson bloggerJson=new BloggerBasicJson(bloggers, bloggersChange, adapter,listView,context);
		bloggerJson.execute(params);
	}
	@Override
	public void onLoadMore() {
		//���ظ��಩��
		String moreUrl="http://10.23.0.102:8080/vmojing-web/blogger/updateList?uid="
				+ myApp.getUsername()
				+ "&timestamp="
				+ bloggers.get(0).getMonitorAt()
				+ "&direction=-1";
		String params[]={moreUrl,""+DragListView.LOADMORE_INDEX};
		BloggerBasicJson bloggerJson=new BloggerBasicJson(bloggers, bloggersChange, adapter,listView,context);
		bloggerJson.execute(params);	
	}
}
