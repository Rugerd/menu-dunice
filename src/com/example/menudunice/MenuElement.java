package com.example.menudunice;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MenuElement extends Activity {
			
	SQLiteDatabase db;
	final String LOG_TAG = "myLogs";
	
	Button btnFirst;
	Button btnNext;
	
	TextView tvSum;
		
	Boolean flagDialogShow = false;
	SharedPreferences sPref;
	
	CustomDialog d;
	
	ListView lvFirst;
	ArrayList<String> sumPrice = new ArrayList<String>();
	ArrayList<LVItems> dataSelected = new ArrayList<LVItems>();
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList("dataSelected", dataSelected);
        savedInstanceState.putBoolean("sd", flagDialogShow);
        if ((d != null)&&(flagDialogShow)) {
        	d.WriteToBundle(savedInstanceState);
        }
    }
	/*
	@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
      dataSelected = savedInstanceState.getParcelableArrayList("dataSelected");
    }
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu_element);
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
		StrictMode.setThreadPolicy(policy); 
	    
	    lvFirst = (ListView) findViewById(R.id.lvFirst);   
	    btnFirst = (Button) findViewById(R.id.btnFirst);
	    btnNext = (Button) findViewById(R.id.btnNext);
	    tvSum = (TextView) findViewById(R.id.tvSum);
	    
	    DBHelper dbh = new DBHelper(this);
		db = dbh.getWritableDatabase();
		try {
			get_Menu();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.close();
	    
	    
	    if( savedInstanceState != null ) {
	    	dataSelected = savedInstanceState.getParcelableArrayList("dataSelected");
	    	setListVeiw(dataSelected);
	    	flagDialogShow = savedInstanceState.getBoolean("sd");
	    	if (flagDialogShow == true) {
		    	try {
		    		showCusDial(savedInstanceState);
		    		flagDialogShow = true;
		    	} catch (Exception e) {
		    		//Toast.makeText(this,  e.getMessage() , 5000).show();
		    	}
	    	}
	    }   
	}
	
	public void newMenu(ArrayList<LVItems> items_data) {
		Log.d(LOG_TAG, "--- Insert in mytable: ---");
		ContentValues cv = new ContentValues();
		for (LVItems el : items_data) {
			cv.put("name", el.getName());
			cv.put("price", el.getPrice());
			cv.put("title", el.getTitle());
			db.insert("items", null, cv);
		}
		
	}
	
	public  void get_Menu() throws Exception {
		getingMenu gm = new getingMenu();
		newMenu(gm.items_data);	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void sumPrice(ArrayList<LVItems> data){
		sumPrice.clear();
		for(LVItems d: data){
			sumPrice.add(d.subItem);
		}
		float temp = 0;
		for (int i = 0; i < sumPrice.size(); i++){
			temp = temp + Float.parseFloat(sumPrice.get(i).toString());
		}
		tvSum.setText("Сумма:" + temp);
	}
	
	//удаление блюда из заказа
	public void itemDel(final MyArrayAdapter adapter, final int arg2, final ArrayList<LVItems> data){
	
		new AlertDialog.Builder(this)
	    .setTitle("Удаление")
	    .setMessage("Вы уверены?")
	    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	adapter.remove(adapter.getItem(arg2));
				lvFirst.invalidateViews();
				sumPrice(data);
	        }
	     })
	    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	     .show();
	}
	
	//Заполнение lvFirst
	public void setListVeiw (final ArrayList<LVItems> data){
		
		final MyArrayAdapter adapter = new MyArrayAdapter2(this, R.layout.selected_menu_item, data);
		lvFirst.setAdapter(adapter);
		
		sumPrice(data);
		lvFirst.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//adapter.remove(adapter.getItem(arg2));
				//lvFirst.invalidateViews();
				//sumPrice(data);
				itemDel(adapter, arg2, data);
			}
	    	
	    });
	}
	//создание и запуск диалога
	public void showCusDial(Bundle b){
		d = new CustomDialog(this, b, new SelectApplicationDialogResult(){
			public void OnApplicationDialogDissmiss(Context c, ArrayList<LVItems> selected){
				flagDialogShow = false;
				dataSelected.addAll(selected);
				setListVeiw(dataSelected);
			}
		});
		d.show();
	}
	
	public void onClick(View v) throws IOException {
		
	    switch (v.getId()) {
	    case R.id.btnNext:
	    	Bundle extras = getIntent().getExtras();
	    	String token = extras.getString("token");
			String id = extras.getString("id");
	    	Logon.setOrder(dataSelected, id, token);
	    	Toast.makeText(this,  "Заказ отправлен", 50).show();
	    	
	    	break;
	    case R.id.btnFirst:
	    	try {
	    		showCusDial(null);
	    		flagDialogShow = true;
	    			    		
	    	} catch (Exception e) {
	    		Toast.makeText(this,  e.getMessage() , 5000).show();
	    	}
	   
	    default:
	      break;
	    }
	    
	}
	 
	@Override
	protected void onDestroy() {
		//saveFlag(flagDialogShow);
	    super.onDestroy();
	}
}
