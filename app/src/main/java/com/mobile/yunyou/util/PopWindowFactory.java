package com.mobile.yunyou.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mobile.yunyou.R;

public class PopWindowFactory {

	
	
	public static PopupWindow creatLoadingPopWindow(final Context context, String tip){


		View view = LayoutInflater.from(context).inflate(R.layout.load_progress_layout, null);
		TextView tvTip = (TextView) view.findViewById(R.id.tv_progress_tip);
		if (tip != null)
		{
			tvTip.setText(tip);
		}
		
		
		PopupWindow popWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		
		ColorDrawable dw = new ColorDrawable(0x00);
		popWindow.setBackgroundDrawable(dw);
		
		
		
		return popWindow;
	}
	
	public static PopupWindow creatLoadingPopWindow(final Context context, int id){


		View view = LayoutInflater.from(context).inflate(R.layout.load_progress_layout, null);
		TextView tvTip = (TextView) view.findViewById(R.id.tv_progress_tip);
		tvTip.setText(id);
		
		
		
		PopupWindow popWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		
		ColorDrawable dw = new ColorDrawable(0x00);
		popWindow.setBackgroundDrawable(dw);
		
		
		
		return popWindow;
	}
	
}
