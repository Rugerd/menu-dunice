package com.example.menudunice;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyArrayAdapter2 extends MyArrayAdapter {

	public MyArrayAdapter2(Context context, int layoutResourceId,
			ArrayList<LVItems> items) {
		super(context, layoutResourceId, items);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent){
		View row = convertView;
        ItemsHolder holder;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false); 
            holder = new ItemsHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtSubTitle = (TextView) row.findViewById(R.id.txtSubTitle);
            row.setTag(holder);
        }
        else
        {
            holder = (ItemsHolder)row.getTag();
        }
        
        LVItems lvitems = this.items.get(position);
	    if (lvitems != null){    
	        holder.txtTitle.setText(lvitems.item);
	        holder.txtSubTitle.setText(lvitems.subItem);
	    }
        return row;
    }

}
