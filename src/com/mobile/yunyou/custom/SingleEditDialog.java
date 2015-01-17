package com.mobile.yunyou.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.yunyou.R;

public class SingleEditDialog extends AbstractEditDialog{


	protected TextView mTextView;
	protected EditText mEditText;
	
	
	public SingleEditDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void initView(Context context) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.custom_single_edit_dialog_layout, null);
		
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		mEditText = (EditText) view.findViewById(R.id.etEdit);
		mTextView = (TextView) view.findViewById(R.id.tvTextView);
		
	    Window dialogWindow = getWindow();       
	    WindowManager.LayoutParams lp = dialogWindow.getAttributes();          
	    
		ColorDrawable dw = new ColorDrawable(0x0000ff00);
	    dialogWindow.setBackgroundDrawable(dw);
	}

	
	public void setTextString(int id)
	{
		String str = mContext.getResources().getString(id);
		 mTextView.setText(str);
	}

	public void setInputType(int type)
	{
		mEditText.setInputType(type);
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
