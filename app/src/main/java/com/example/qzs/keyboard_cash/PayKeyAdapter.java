package com.example.qzs.keyboard_cash;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PayKeyAdapter extends BaseAdapter {
	private Context mContext;
	
	private Context context;  
	   
	 public PayKeyAdapter(Context context) {  
		 this.context=context;  
	 }  
	   
	 private String[] texts = {  
	   //九宫格图片下方文字的设置  
	   "1",  
	   "2",  
	   "3",  
	   "清空",
	   "4",  
	   "5",  
	   "6",  
	   "←",
	   "7",  
	   "8",  
	   "9",  
	   "=",
	   ".",  
	   "0",
	   "+",
	   "收款",
	   }; 
	 
	 private String[] types = {  
	   //存放在text的tag中
	   "num",  
	   "num",  
	   "num",  
	   "cancel",
	   "num",  
	   "num",  
	   "num",  
	   "back",
	   "num",  
	   "num",  
	   "num",  
	   "equal",
	   "point",  
	   "num",
	   "plus",
	   "pay",
	 };
	 
	 @Override  
	 public int getCount() {  
	  return texts.length;  
	 }  
	 
	 @Override  
	 public Object getItem(int position) {  
	  return position;  
	 }  
	 
	 @Override  
	 public long getItemId(int position) {  
	  return position;  
	 }  
	 
	 @Override  
	 public View getView(int position, View view, ViewGroup viewgroup) {  
	  ImgTextWrapper wrapper;  
	  if(view==null) {  
	   wrapper = new ImgTextWrapper();  
	   LayoutInflater inflater = LayoutInflater.from(context);  
	   view = inflater.inflate(R.layout.grid_menu_item_pay, null);
	   view.setTag(wrapper);  
	   view.setPadding(3, 3, 3, 3);  //每格的间距  
	  } else {  
	   wrapper = (ImgTextWrapper)view.getTag();  
	  }  
	    
	  wrapper.textView = (TextView)view.findViewById(R.id.GridTextView);
	  wrapper.textView.setText(texts[position]);  
	  wrapper.textView.setTag(types[position]);
	  if(types[position].equals("back")
			  || types[position].equals("cancel")
			  || types[position].equals("equal")){
		  wrapper.textView.setBackgroundResource(R.drawable.btn_cell_delete);
	  }else if(types[position].equals("pay")){
		  wrapper.textView.setBackgroundResource(R.drawable.btn_cell_pay);
		  wrapper.textView.setTextColor(Color.WHITE);
	  }
			  
//	  wrapper.textView = (TextView)view.findViewById(R.id.GridItemText);  
//	  wrapper.textView.setText(texts[position]);
//	  wrapper.textView.setTag(payTypes[position]);
	    
	  return view;  
	 }  
	 
	 class ImgTextWrapper {  
		 ImageView imageView;  
		 TextView textView;  
	 } 
}
