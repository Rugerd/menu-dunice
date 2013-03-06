package com.example.menudunice;

import android.os.Parcel;
import android.os.Parcelable;

public class LVItems implements Parcelable {
	public String item;
	public String subItem;
	public String name;
	public boolean checked;
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		parcel.writeString(item);
		parcel.writeString(subItem);
		parcel.writeString(name);
		parcel.writeString(Boolean.toString(checked));
	}
	
	private LVItems(Parcel parcel){
		item = parcel.readString();
		subItem = parcel.readString();
		name = parcel.readString();
		checked = Boolean.parseBoolean(parcel.readString());
	}
	
	public LVItems(String name,String item, String subItem, boolean checked) {
//	        super();
	        this.item = item;
	        this.subItem = subItem;
	        this.checked = checked;
	        this.name = name;
	    }
	
	public static final Parcelable.Creator<LVItems> CREATOR = new Parcelable.Creator<LVItems>() {

		@Override
		public LVItems createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new LVItems(in);
		}

		@Override
		public LVItems[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LVItems[size];
		}
		
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	public String getName() {
		return name;
	}

	public String getPrice() {
		return subItem;
	}

	public String getTitle() {
		return item;
	}

}
