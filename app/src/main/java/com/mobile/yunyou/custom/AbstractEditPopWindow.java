package com.mobile.yunyou.custom;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public abstract class AbstractEditPopWindow implements OnClickListener{

	protected Context mContext;
	protected View mParentView;
	
	protected TextView mTVTitle;
	protected Button mButtonOK;
	protected Button mButtonCancel;
	
	
	protected PopupWindow mPopupWindow;    
	
	protected OnClickListener mOkListener;
	
	protected Object tag;
	
	public AbstractEditPopWindow(Context context,View parentView)
	{
		mContext = context;
		mParentView = parentView;
	
		initView(context); 
	}
	
	protected abstract  void initView(Context context);
	

	
	public void setTag(Object tag)
	{
		this.tag = tag;
	}
	
	public Object getTag()
	{
		return tag;
	}
	
	public void setOnOKButtonListener(View.OnClickListener onClickListener) {
		mOkListener = onClickListener;
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
	
}
