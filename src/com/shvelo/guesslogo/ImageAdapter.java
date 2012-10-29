package com.shvelo.guesslogo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{
Context context;

public ImageAdapter(Context context)
{
 this.context = context;
}

public int getCount() 
{
 return BrandManager.size();
}

public View getView(int position, View convertView, ViewGroup parent) 
{
 View v = convertView;

 if ( convertView == null )
 {
    LayoutInflater li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    v = li.inflate(R.layout.grid_item, null);
    
    ImageView iv = (ImageView)v.findViewById(R.id.grid_item);
    iv.setImageDrawable(BrandManager.get(position).logo);
 }
 
 if(BrandManager.get(position).guessed){
 	v.setBackgroundColor(Color.GRAY);
 }

 return v;
}

public Object getItem(int arg0) {
 return null;
}

public long getItemId(int arg0) {
 return 0;
}
}