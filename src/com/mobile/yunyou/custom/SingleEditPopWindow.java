package com.mobile.yunyou.custom;

import com.mobile.yunyou.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SingleEditPopWindow extends AbstractEditPopWindow implements OnClickListener{


	protected TextView mTextView;
	protected EditText mEditText;


	public SingleEditPopWindow(Context context,View parentView)
	{
		super(context, parentView);

	}
	
	protected void initView(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.custom_single_edit_dialog_layout, null);
	
		mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
		mButtonOK = (Button) view.findViewById(R.id.btnOK);
		mButtonOK.setOnClickListener(this);
		mButtonCancel = (Button) view.findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);
		
		mEditText = (EditText) view.findViewById(R.id.etEdit);
		mTextView = (TextView) view.findViewById(R.id.tvTextView);
		
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
	
	
	public void setTitle(int id)
	{
		String str = mContext.getResources().getString(id);
		mTVTitle.setText(str);
	}
	
	
	public void setEditString(String str)
	{
		 mEditText.setText(str);
	}

	public String getEditString()
	{
		return mEditText.getText().toString().trim();
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
