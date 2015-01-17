package com.mobile.yunyou.custom;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DoubleEditDialog extends AbstractEditDialog {

	protected TextView mTextView1;
	protected TextView mTextView2;
	
	protected EditText mEditText1;
	protected EditText mEditText2;
	
	
	public DoubleEditDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void initView(Context context) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.custom_two_edit_dialog_layout, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		mEditText1 = (EditText) view.findViewById(R.id.etEdit1);
		mEditText2 = (EditText) view.findViewById(R.id.etEdit2);
		
		mTextView1 = (TextView) view.findViewById(R.id.tvText1);
		mTextView2 = (TextView) view.findViewById(R.id.tvText2);
		    
	    Window dialogWindow = getWindow();      
	    WindowManager.LayoutParams lp = dialogWindow.getAttributes();         
		ColorDrawable dw = new ColorDrawable(0x00000000);
	    dialogWindow.setBackgroundDrawable(dw);
	
	    WindowManager m = dialogWindow.getWindowManager();
	    Display d = m.getDefaultDisplay();
//	    lp.width = Utils.getScreenWidth(context);
//	    lp.height = Utils.getScreenHeight(context);

	    dialogWindow.setAttributes(lp);
	    
	   
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
	
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btnOK:
			onButtonOK();
			break;
		case R.id.btnCancel:
			onButtonCancel();
			break;
		}
	}
	
	protected void onButtonOK()
	{
		dismiss();
		if (mOkListener != null)
		{
			mOkListener.onClick(null);
		}
	}
	
	protected void onButtonCancel()
	{
		dismiss();
	}


	



}
