package com.mobile.yunyou.custom;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;

public class DoubleEditPopWindow extends AbstractEditPopWindow implements OnClickListener{


	protected TextView mTextView1;
	protected TextView mTextView2;
	
	protected EditText mEditText1;
	protected EditText mEditText2;

	public DoubleEditPopWindow(Context context,View parentView)
	{
		super(context, parentView);

		
	}
	
	protected void initView(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.custom_two_edit_dialog_layout, null);
	
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		mEditText1 = (EditText) view.findViewById(R.id.etEdit1);
		mEditText2 = (EditText) view.findViewById(R.id.etEdit2);
		
		mTextView1 = (TextView) view.findViewById(R.id.tvText1);
		mTextView2 = (TextView) view.findViewById(R.id.tvText2);
		
		
		mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mPopupWindow.setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		 	
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
	
	
	public void setTitle(String title)
	{
		mTVTitle.setText(title);
	}
	
	public  EditText getEditText1()
	{
		return mEditText1;
	}
	
	public  EditText getEditText2()
	{
		return mEditText2;
	}
	
	public void setEdit1String(String str)
	{
		 mEditText1.setText(str);
	}
	
	public void setEdit2String(String str)
	{
		mEditText2.setText(str);
	}
	
	
	public String getEdit1String()
	{
		return mEditText1.getText().toString().trim();
	}
	
	public String getEdit2String()
	{
		return mEditText2.getText().toString().trim();
	}
	
	public void setText1String(int id)
	{
		String str = mContext.getResources().getString(id);
		 mTextView1.setText(str);
		 
	}
	
	public void setText2String(int id)
	{
		String str = mContext.getResources().getString(id);
		mTextView2.setText(str);
	}
	
	
	public String getText1String()
	{
		return mTextView1.getText().toString().trim();
	}
	
	public String getText2String()
	{
		return mTextView2.getText().toString().trim();
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
