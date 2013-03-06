package com.example.menudunice;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<LVItems>{
	Context context;
	int layoutResourceId;
	ArrayList<LVItems> items, originalitems;

	
	public MyArrayAdapter (Context context, int layoutResourceId, ArrayList<LVItems> items){
		super(context, 0, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}
	
	// кол-во элементов
	  @Override
	  public int getCount() {
	    return items.size();
	  }

	  // элемент по позиции
	  @Override
	  public LVItems getItem(int position) {
	    return items.get(position);
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
            holder.ckbSet = (CheckBox) row.findViewById(R.id.ckbSet);
            holder.ckbSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            	@Override    
            	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		      // меняем данные меню
            		LVItems i = ((LVItems) buttonView.getTag());
            		if (i != null) {
            			i.checked = isChecked;
            		}
        		}
            });
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
	        
	        holder.ckbSet.setTag(lvitems);
	        holder.ckbSet.setChecked(lvitems.checked);
	    }
        return row;
    }
	
	public ArrayList<LVItems> getOriginal() {
		if (originalitems == null) {
			return new ArrayList<LVItems>(items);
		} else {
			return new ArrayList<LVItems>(originalitems);
		}
	}
	
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults oReturn = new FilterResults();
				ArrayList<LVItems> results;
				if (originalitems == null) {
					originalitems = new ArrayList<LVItems>(items);
				}
				if (constraint.length() > 0) {
					results = new ArrayList<LVItems>();
					for (LVItems item : originalitems) {
						if (item.item.toLowerCase().contains(constraint.toString().toLowerCase())){
							results.add(item);
						}
					}
				} else {
					results = new ArrayList<LVItems>(originalitems);
				}
				oReturn.values = results;
				oReturn.count = results.size();
				return oReturn;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				items = (ArrayList<LVItems>) results.values;
				notifyDataSetChanged();

			}
		};
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
    static class ItemsHolder
    {
        TextView txtTitle;
        TextView txtSubTitle;
        CheckBox ckbSet;
    }
}
