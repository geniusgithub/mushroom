package com.mobile.yunyou.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public abstract class AbstractEditDialog extends Dialog implements View.OnClickListener{

	protected Context mContext;
	protected TextView mTVTitle;
	protected Button mButtonOK;
	protected Button mButtonCancel;
	
	protected Object tag;
	
	protected View.OnClickListener mOkListener;

	
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	    Window dialogWindow = getWindow();    
	    dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public AbstractEditDialog(Context context)
	{
		super(context);
		mContext = context;
	
		initView(context); 
	
	}
	
	protected abstract  void initView(Context context);
	
	
	public void setOnOKButtonListener(View.OnClickListener onClickListener) {
		mOkListener = onClickListener;
	}
	
	public void setTitle(String title)
	{
		mTVTitle.setText(title);
	}
	
	public void setTag(Object tag)
	{
		this.tag = tag;
	}
	
	public Object getTag()
	{
		return tag;
	}
}
