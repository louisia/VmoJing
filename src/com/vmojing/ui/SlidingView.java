package com.vmojing.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlidingView extends ViewGroup {

	private FrameLayout container;
	private Scroller scroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int SNAP_VELOCITY = 1000;
	private View mMenuView;

	public SlidingView(Context context) {
		super(context);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		container.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		container.layout(0, 0, width, height);
	}

	private void init() {
		container = new FrameLayout(getContext());
		container.setBackgroundColor(0xff000000);
		scroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		super.addView(container);
	}

	public void setView(View v) {
		if (container.getChildCount() > 0) {
			container.removeAllViews();
		}
		container.addView(v);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (!scroller.isFinished()) {
			if (scroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = scroller.getCurrX();
				int y = scroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
	}

	private boolean mIsBeingDragged;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingDragged = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (xDiff > mTouchSlop && xDiff > yDiff) {
				mIsBeingDragged = true;
				mLastMotionX = x;
			}
			break;

		}
		return mIsBeingDragged;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			if (getScrollX() == -getMenuViewWidth()
					&& mLastMotionX < getMenuViewWidth()) {
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsBeingDragged) {
				enableChildrenCache();
				final float deltaX = mLastMotionX - x;
				mLastMotionX = x;
				float oldScrollX = getScrollX();
				float scrollX = oldScrollX + deltaX;
				if (deltaX < 0 && oldScrollX < 0) { // left view
					final float leftBound = 0;
					final float rightBound = -getMenuViewWidth();
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
				}
				scrollTo((int) scrollX, getScrollY());
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();
				velocityX = 0;
				Log.e("ad", "velocityX == " + velocityX);
				int oldScrollX = getScrollX();
				int dx = 0;
				if (oldScrollX < 0) {
					if (oldScrollX < -getMenuViewWidth() / 2
							|| velocityX > SNAP_VELOCITY) {
						dx = -getMenuViewWidth() - oldScrollX;
					} else if (oldScrollX >= -getMenuViewWidth() / 2
							|| velocityX < -SNAP_VELOCITY) {
						dx = -oldScrollX;
					}
				}
				smoothScrollTo(dx);
				clearChildrenCache();
			}
			break;
		}
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
		return true;
	}

	private int getMenuViewWidth() {
		if (mMenuView == null) {
			return 0;
		}
		return mMenuView.getWidth();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}


	public View getMenuView() {
		return mMenuView;
	}

	public void setMenuView(View mMenuView) {
		this.mMenuView = mMenuView;
	}


	/**
	 * 左侧边栏的关闭与显示
	 */
	public void showLeftView() {
		int menuWidth = mMenuView.getWidth();
		int oldScrollX = getScrollX();
		if (oldScrollX == 0) {
			smoothScrollTo(-menuWidth);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
		}
	}


	void smoothScrollTo(int dx) {
		int duration = 100;
		int oldScrollX = getScrollX();
		scroller.startScroll(oldScrollX, getScrollY(), dx, getScrollY(),
				duration);
		invalidate();
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

}
