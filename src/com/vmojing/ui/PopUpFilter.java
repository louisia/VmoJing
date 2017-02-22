package com.vmojing.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vmojing.R;

public class PopUpFilter {
	private View popupWindowView;
	private PopupWindow popupWindow;
	
	private TextView status;
	private RadioGroup statusGroup;
	private RadioButton statusButton;
	private RadioButton statusAllButton;
	private RadioButton statusnormalButton;
	private RadioButton statusabnormalButton;
	
	private TextView type;
	private RadioGroup typeGroup;
	private RadioButton typeButton;
	
	private RadioButton typeAllButton;
	private RadioButton typeDoneButton;
	private RadioButton typeDoButton;
	private RadioButton typeIgnoreButton;
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public PopUpFilter(Context context) {
		// TODO Auto-generated constructor stub
		popupWindowView = LayoutInflater.from(context).inflate(R.layout.popup_filter, null); 
		popupWindow = new PopupWindow(popupWindowView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
		
		status=(TextView)popupWindowView.findViewById(R.id.status);
		statusGroup=(RadioGroup)popupWindowView.findViewById(R.id.topic_status);
		statusButton=(RadioButton)popupWindowView.findViewById(statusGroup.getCheckedRadioButtonId());
		statusAllButton=(RadioButton)popupWindowView.findViewById(R.id.topic_all2);
		statusnormalButton=(RadioButton)popupWindowView.findViewById(R.id.topic_normal);
		statusabnormalButton=(RadioButton)popupWindowView.findViewById(R.id.topic_abnormal);
		
		type=(TextView)popupWindowView.findViewById(R.id.type);
		typeGroup=(RadioGroup)popupWindowView.findViewById(R.id.topic_type);
		typeButton=(RadioButton)popupWindowView.findViewById(typeGroup.getCheckedRadioButtonId());
		typeAllButton=(RadioButton)popupWindowView.findViewById(R.id.topic_all1);
		typeDoneButton=(RadioButton)popupWindowView.findViewById(R.id.topic_leader);
		typeDoButton=(RadioButton)popupWindowView.findViewById(R.id.topic_depar);
		typeIgnoreButton=(RadioButton)popupWindowView.findViewById(R.id.topic_other);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true); 
		popupWindow.setFocusable(true); 
		statusGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				statusButton=(RadioButton)popupWindowView.findViewById(group.getCheckedRadioButtonId());
			}
		});
		typeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				typeButton=(RadioButton)popupWindowView.findViewById(group.getCheckedRadioButtonId());

			}
		});
	}
	
	public PopupWindow getPopupWindow() {
		return popupWindow;
	}
	public TextView getStatus() {
		return status;
	}
	public TextView getType() {
		return type;
	}
	public RadioGroup getStatusGroup() {
		return statusGroup;
	}
	public RadioGroup getTypeGroup() {
		return typeGroup;
	}

	public RadioButton getStatusButton() {
		return statusButton;
	}

	public RadioButton getTypeButton() {
		return typeButton;
	}

	public RadioButton getStatusAllButton() {
		return statusAllButton;
	}

	public RadioButton getStatusnormalButton() {
		return statusnormalButton;
	}

	public RadioButton getStatusabnormalButton() {
		return statusabnormalButton;
	}

	public RadioButton getTypeAllButton() {
		return typeAllButton;
	}

	public RadioButton getTypeDoneButton() {
		return typeDoneButton;
	}

	public RadioButton getTypeDoButton() {
		return typeDoButton;
	}

	public RadioButton getTypeIgnoreButton() {
		return typeIgnoreButton;
	}

	public void setTypeButton(RadioButton typeButton) {
		this.typeButton = typeButton;
	}
}
