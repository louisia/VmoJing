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
 * ȫ���ȵ㣺
 * 		������ʾ�����е��ȵ�΢��,��Щ΢������ԭ��΢��
 * 		ԭʼ���ݣ�����ͷ�񡢲����ǳơ�΢������ʱ�䡢΢�����ݡ�΢����ͼ��ת��������������������
 * 		�������ݣ�Ԥ��ת����Ԥ�����ۡ�Ԥ�����
 * 		��������ĳ���ȵ�΢������Ϊ΢����������������⡢���ĳ������������ˢ�¡��������ظ���
 * @author clyx
 *
 */
@SuppressLint({ "InflateParams", "NewApi" })
public class PublicsBasic  extends Fragment {
	private View view;            //ҳ�沼��
	private Context context;      //�����Ŀؼ�
	private List<Publics>publics=new ArrayList<Publics>();//�ȵ��б�
	private ListView listView; //�Զ���ListView�ؼ�
	private PublicsAdapter adapter;    //����������
	private MyAppplication myApp;      //ȫ�ֶ���
	private PopUpFilter myPopUpWindow;    //����ʽ�˵�
	private PopupWindow popupWindow;
	/**
	 * ��ʼ����Ա����
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
		//TODO ��ʼ��
		String initUrl="http://10.23.0.102:8080/vmojing-web/hot/weibo?"
				+ "timeOpt=1";
		PublicsBasicJson jsonData=new PublicsBasicJson(publics, adapter, context);
		jsonData.execute(initUrl);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**��ʼ��ͷ���˵�*/
		PublicsBasic fragment=new PublicsBasic();
		MenuTop topMenu=new MenuTop(fragment, view);
		topMenu.getLeftBtn().setOnClickListener(new MyClick());
		topMenu.getRightBtn().setOnClickListener(new MyClick());
	}
	/**
	 * ���õ���ContextMenu������
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("�ȵ����");
		menu.add(0,Menu.FIRST,0,"����Ϊ����");
		menu.add(0,Menu.FIRST+1,0,"���������");
		menu.add(0,Menu.FIRST+2,0,"���ò���");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	/**
	 * ����ContextMenu��item����¼�
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position-1;
		switch(item.getItemId()){
		case 1:
			//TODO:����Ϊ����
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
			//TODO:���������
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
			//TODO ���ò���
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
	 * �Զ����ಶ׽�ؼ�����¼�
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
				myPopUpWindow.getStatus().setText("ʱ��ڵ�");
				myPopUpWindow.getStatusAllButton().setText("0����3h");
				myPopUpWindow.getStatusnormalButton().setText("3����6h");
				myPopUpWindow.getStatusabnormalButton().setText("6����9h");
				popupWindow.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						//TODO ������ɸѡ�ȵ�΢��
						publics.clear();
						int timeOpt=0;
						if(myPopUpWindow.getStatusButton().getText().toString().equals("0����3h"))
							timeOpt=1;
						else if(myPopUpWindow.getStatusButton().getText().toString().equals("3����6h"))
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
