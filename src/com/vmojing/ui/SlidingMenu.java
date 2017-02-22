package com.vmojing.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SlidingMenu extends RelativeLayout {

	private SlidingView mSlidingView;
	private View mMenuView;

	public SlidingMenu(Context context) {
		super(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	/**
	 * 添加左侧边栏的view
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setLeftView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);// 在父控件的左边
		addView(view, behindParams);
		mMenuView = view;
	}
	/**
	 * 添加中间内容的view
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mSlidingView = new SlidingView(getContext());
		mSlidingView.setView(view);
		addView(mSlidingView, aboveParams);
		mSlidingView.setMenuView(mMenuView);
		mSlidingView.invalidate();
	}

	public void showLeftView() {
		mSlidingView.showLeftView();
	}

}
