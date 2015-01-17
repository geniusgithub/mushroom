package com.mobile.yunyou.custom;

import java.util.Calendar;

import com.mobile.yunyou.R;
import com.mobile.yunyou.widget.NumericWheelAdapter;
import com.mobile.yunyou.widget.WheelView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomTimeExPopWindow implements OnClickListener{

	protected Context mContext;
	protected View mParentView;


	protected TextView mTVTitle;
	protected Button mButtonOK;
	protected Button mButtonCancel;
	protected  WheelView yearWheelView;
	protected  WheelView monthWheelView;
	protected  WheelView dayWheelView ;
	
	
	protected PopupWindow mPopupWindow;                                                                                                                                                  

	protected Object tag;
	
	private OnClickListener mOkListener;

	public CustomTimeExPopWindow(Context context,View parentView)
	{
		mContext = context;
		mParentView = parentView;
	
		initView(mContext);
	}
	
	protected void initView(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.custom_timeex_dialog_layout, null);
	
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		yearWheelView =  (WheelView) view.findViewById(R.id.year);
		monthWheelView =  (WheelView) view.findViewById(R.id.month);
		dayWheelView = (WheelView) view.findViewById(R.id.day);
		
		mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	mPopupWindow.setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		 	
		initWheelView(view);
	}
	
	
	private void initWheelView(View view)
	{

		yearWheelView = (WheelView) view.findViewById(R.id.year);
		yearWheelView.setAdapter(new NumericWheelAdapter(2012, 2020));
		yearWheelView.setCyclic(true);
		yearWheelView.TEXT_SIZE = 30;
		
		monthWheelView = (WheelView) view.findViewById(R.id.month);
		monthWheelView.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		monthWheelView.setCyclic(true);
		monthWheelView.TEXT_SIZE = 30;
		

		dayWheelView = (WheelView) view.findViewById(R.id.day);
		dayWheelView.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
		dayWheelView.setCyclic(true);
		dayWheelView.TEXT_SIZE = 30;
		
		// set current time
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curDay = c.get(Calendar.DAY_OF_MONTH);
	
		setYear(curYear);
		setMonth(curMonth);
		setDay(curDay);


	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btnOK:
			onButtonOK(v);
			break;
		case R.id.btnCancel:
			onButtonCancel(v);
			break;
		}
	}
		
	public void setTag(Object object)
	{
		tag = object;
	}
	
	public Object getTag()
	{
		return tag;
	}
	
	public void setYear(int year)
	{
		yearWheelView.setCurrentItem(year - 2012);
	}
	
	public void setMonth(int month)
	{
		monthWheelView.setCurrentItem(month - 1);
	}
	
	public void setDay(int day)
	{
		dayWheelView.setCurrentItem( day - 1);
	}
	
	public int getYear()
	{
		return yearWheelView.getCurrentItem() + 2012;
	}
	
	public int getMonth()
	{
		return monthWheelView.getCurrentItem() + 1;
	}
	
	public int getDay()
	{
		return dayWheelView.getCurrentItem() + 1;
	}
	
	public void setOnOKButtonListener(View.OnClickListener onClickListener) {
		mOkListener = onClickListener;
	}
	
	public void setTitle(String title)
	{
		mTVTitle.setText(title);
	}
	


	public void show(boolean bShow)
	{

		if (bShow)
		{		
			mPopupWindow.showAtLocation(mParentView, Gravity.TOP, 0, 0);
		}else{
			mPopupWindow.dismiss();
		}
	}

	protected void onButtonOK(View v)
	{
		show(false);
		
		if (mOkListener != null)
		{
			mOkListener.onClick(v);
		}
	}
	
	protected void onButtonCancel(View v)
	{
		show(false);

	}
}
