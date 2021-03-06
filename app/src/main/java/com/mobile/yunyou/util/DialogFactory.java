package com.mobile.yunyou.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobile.yunyou.R;

public class DialogFactory {

	public static interface ISelectComplete{
		public void onSelectComplete(boolean flag);
	}
	
	public static interface IThreeSelectComplete{
		public void onSelectComplete(int index);
	}
	
	
	public static Dialog creatRequestDialog(final Context context, int tip){
		
		String tipString = context.getResources().getString(tip);
		
		return creatRequestDialog(context, tipString);
	}
	
	public static Dialog creatRequestDialog(final Context context, String tip){
		
		final Dialog dialog = new Dialog(context, R.style.dialog);	
		dialog.setContentView(R.layout.dialog_layout);	
	
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		
		int width = (int) (0.6 * Utils.getScreenWidth(context));
		if (width < 300)
		{
			width = 300;
		}
		lp.width = width;	
		
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		if (tip == null || tip.length() == 0)
		{
			titleTxtv.setText(R.string.sending_request);	
		}else{
			titleTxtv.setText(tip);	
		}
		
		return dialog;
	}
	
		public static Dialog creatSingleDialog(final Context context, int title, int message, final OnClickListener onClickListener){
			String titleString = context.getResources().getString(title);
			String messageString = context.getResources().getString(message);
			
			return creatSingleDialog(context, titleString, messageString, onClickListener);
		}
	
		public static Dialog creatSingleDialog(final Context context, String title, String message, final OnClickListener onClickListener){
		
				final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.single_dialog_layout);
					TextView titleTxtv = (TextView) dialog.findViewById(R.id.common_dialog_title_tv);
					titleTxtv.setText(title);
					TextView msgTxtv = (TextView) dialog.findViewById(R.id.common_dialog_msg_tv);
					msgTxtv.setText(message);
					TextView positiveBtn = (TextView) dialog.findViewById(R.id.common_dialog_positive_btn);
				positiveBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (onClickListener != null)
						{
							onClickListener.onClick(v);
						}
					}
				});

				Window window = dialog.getWindow();
				window.setBackgroundDrawableResource(R.drawable.dialog_common_bg);
				window.setGravity(Gravity.CENTER);
				WindowManager.LayoutParams lp = window.getAttributes();	
				int width = (int) (0.8 * Utils.getScreenWidth(context));
				if (width < 300)
				{
					width = 300;
				}
				lp.width = width;	
				return dialog;
	}
	
	public static Dialog creatDoubleDialog(final Context context, int title, int message, final OnClickListener onClickListener)
	{
		String titleString = context.getResources().getString(title);
		String messageString = context.getResources().getString(message);
		
		return creatDoubleDialog(context, titleString, messageString, onClickListener);
	}
	
	public static Dialog creatDoubleDialog(final Context context, String title, String message, final OnClickListener onClickListener){
		
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.common_dialog_layout);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.common_dialog_title_tv);
		titleTxtv.setText(title);
		TextView msgTxtv = (TextView) dialog.findViewById(R.id.common_dialog_msg_tv);
		msgTxtv.setText(message);
		TextView positiveBtn = (TextView) dialog.findViewById(R.id.common_dialog_positive_btn);
		TextView negativeBtn = (TextView) dialog.findViewById(R.id.common_dialog_negative_btn);
		positiveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (onClickListener != null)
				{
					onClickListener.onClick(v);
				}
			}
		});
		negativeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Window window = dialog.getWindow();
		window.setBackgroundDrawableResource(R.drawable.dialog_common_bg);
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = (int) (0.8 * Utils.getScreenWidth(context));
		if (width < 300)
		{
			width = 300;
		}
		lp.width = width;	
		return dialog;
	}
	
	public static Dialog creatDoubleDialog(final Context context, int title, int message, 
									int posTxt, int negTxt,		final OnClickListener onClickListener)
	{
		String titleString = context.getResources().getString(title);
		String messageString = context.getResources().getString(message);
		String posString = context.getResources().getString(posTxt);
		String negString = context.getResources().getString(negTxt);
		
		return creatDoubleDialog(context, titleString, messageString, posString, negString, onClickListener);
	}

	public static Dialog creatDoubleDialog(final Context context, String title, String message, 
											String posTxt, String negTxt, final OnClickListener onClickListener){
		
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.common_dialog_layout);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.common_dialog_title_tv);
		titleTxtv.setText(title);
		TextView msgTxtv = (TextView) dialog.findViewById(R.id.common_dialog_msg_tv);
		msgTxtv.setText(message);
		TextView positiveBtn = (TextView) dialog.findViewById(R.id.common_dialog_positive_btn);
		positiveBtn.setText(posTxt);		
		TextView negativeBtn = (TextView) dialog.findViewById(R.id.common_dialog_negative_btn);
		negativeBtn.setText(negTxt);
		positiveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (onClickListener != null)
				{
					onClickListener.onClick(v);
				}
			}
		});
		negativeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Window window = dialog.getWindow();
		window.setBackgroundDrawableResource(R.drawable.dialog_common_bg);
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = (int) (0.8 * Utils.getScreenWidth(context));
		if (width < 300)
		{
			width = 300;
		}
		lp.width = width;	
		return dialog;
	}

	
	
	
	
	
	
	
		public static Dialog creatSelectDialog(final Context context, int title, int message, 
					int posTxt, int negTxt,		final ISelectComplete listener){
				String titleString = context.getResources().getString(title);
				String messageString = context.getResources().getString(message);
				String posString = context.getResources().getString(posTxt);
				String negString = context.getResources().getString(negTxt);
				
				return creatSelectDialog(context, titleString, messageString, posString, negString, listener);
		}
	
	

		public static Dialog creatSelectDialog(final Context context, String title, String message, 
				String posTxt, String negTxt, final ISelectComplete listener){
		
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.common_dialog_layout);
				TextView titleTxtv = (TextView) dialog.findViewById(R.id.common_dialog_title_tv);
				titleTxtv.setText(title);
				TextView msgTxtv = (TextView) dialog.findViewById(R.id.common_dialog_msg_tv);
				msgTxtv.setText(message);
				TextView positiveBtn = (TextView) dialog.findViewById(R.id.common_dialog_positive_btn);
				positiveBtn.setText(posTxt);		
				TextView negativeBtn = (TextView) dialog.findViewById(R.id.common_dialog_negative_btn);
				negativeBtn.setText(negTxt);
				positiveBtn.setOnClickListener(new OnClickListener() {
				
					@Override
					public void onClick(View v) {
							dialog.dismiss();
							if (listener != null)
							{
								listener.onSelectComplete(true);
							}
						}
				});
				negativeBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (listener != null)
					{
						listener.onSelectComplete(false);
					}
				}
				});
				Window window = dialog.getWindow();
				window.setBackgroundDrawableResource(R.drawable.dialog_common_bg);
				window.setGravity(Gravity.CENTER);
				WindowManager.LayoutParams lp = window.getAttributes();	
				int width = (int) (0.8 * Utils.getScreenWidth(context));
				if (width < 300)
				{
				width = 300;
				}
				lp.width = width;	
				return dialog;
		}
		
		public static Dialog creatSelectDialog(final Context context, int title, int message, 
				int oneTxt, int twoTxt, int threeTxt, final IThreeSelectComplete listener){
			String titleString = context.getResources().getString(title);
			String messageString = context.getResources().getString(message);
			String String1 = context.getResources().getString(oneTxt);
			String String2 = context.getResources().getString(twoTxt);
			String String3 = context.getResources().getString(threeTxt);
			
			return creatSelectDialog(context, titleString, messageString, String1, String2, String3, listener);
		}
		
		
		public static Dialog creatSelectDialog(final Context context, String title, String message, 
				String oneTxt, String twoTxt, String threeTxt, final IThreeSelectComplete listener){
		
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.three_dialog_layout);
				TextView titleTxtv = (TextView) dialog.findViewById(R.id.common_dialog_title_tv);
				titleTxtv.setText(title);
				TextView msgTxtv = (TextView) dialog.findViewById(R.id.common_dialog_msg_tv);
				msgTxtv.setText(message);
				TextView btn1 = (TextView) dialog.findViewById(R.id.three_dialog_1);
				btn1.setText(oneTxt);		
				TextView btn2 = (TextView) dialog.findViewById(R.id.three_dialog_2);
				btn2.setText(twoTxt);
				TextView btn3 = (TextView) dialog.findViewById(R.id.three_dialog_3);
				btn3.setText(threeTxt);
				
				OnClickListener onClickListener = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int index = 1;
						switch(v.getId()){
							case R.id.three_dialog_1:
								index = 1;
								break;
							case R.id.three_dialog_2:
								index = 2;
								break;
							case R.id.three_dialog_3:
								index = 3;
								break;
						}
						if (listener != null){
							listener.onSelectComplete(index);
						}
						dialog.dismiss();
					}
				};
				btn1.setOnClickListener(onClickListener);
				btn2.setOnClickListener(onClickListener);
				btn3.setOnClickListener(onClickListener);
				
			
				
				
				Window window = dialog.getWindow();
				window.setBackgroundDrawableResource(R.drawable.dialog_common_bg);
				window.setGravity(Gravity.CENTER);
				WindowManager.LayoutParams lp = window.getAttributes();	
				int width = (int) (0.8 * Utils.getScreenWidth(context));
				if (width < 300)
				{
				width = 300;
				}
				lp.width = width;	
				return dialog;
		}

	
}
