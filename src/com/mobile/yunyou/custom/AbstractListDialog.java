package com.mobile.yunyou.custom;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbstractListDialog extends Dialog implements OnItemClickListener{

	protected Context mContext;
	protected TextView mTVTitle;
	protected ListView mListView;
	protected OnItemClickListener mItemClickListener;
	
	public AbstractListDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		initView(mContext);
		
		
	}
	
	
	protected void initView(Context context)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_listview_dialog_layout);
		
		
		
		
		mTVTitle = (TextView) findViewById(R.id.tvTitle);
		mListView = (ListView)findViewById(R.id.listView);   	
		mListView.setOnItemClickListener(this);
	
	    Window dialogWindow = getWindow();       
	    WindowManager.LayoutParams lp = dialogWindow.getAttributes();         
		ColorDrawable dw = new ColorDrawable(0x00000000);
	    dialogWindow.setBackgroundDrawable(dw);
	
	    WindowManager m = dialogWindow.getWindowManager();
	    Display d = m.getDefaultDisplay();
	    lp.width = Utils.getScreenWidth(context) - 50;
//	    lp.height = Utils.getScreenHeight(context) - 200;
   
	    dialogWindow.setAttributes(lp);
	    
	    

	}
	
	public void setTitle(String title)
	{
		mTVTitle.setText(title);
	}
	
	public void setItemClickListener(OnItemClickListener listener)
	{
		mItemClickListener = listener;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		dismiss();
		if (mItemClickListener != null)
		{
			mItemClickListener.onItemClick(arg0, arg1, position, arg3);
		}
	}
}
