package com.example.menudunice;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class CustomDialog extends Dialog { // implements OnClickListener{

	ListView lvMenu;
	Button btnOk;
	Button btnCancel;
	EditText etFilter;
	Context c;

	ArrayList<LVItems> items_data = new ArrayList<LVItems>();

	public void WriteToBundle(Bundle b) {
		b.putParcelableArrayList("dlgdataSelected", items_data);
		b.putString("filtertext", etFilter.getText().toString());
		
			
	}
	
	public void LoadFromParcel(Bundle b) {
		items_data = b.getParcelableArrayList("dlgdataSelected");
		etFilter.setText(b.getString("filtertext"));
	}
		
	public CustomDialog(final Context c, Bundle b,
			final SelectApplicationDialogResult result) {
		super(c);
		this.c = c;
		setContentView(R.layout.custom_dialog);

		lvMenu = (ListView) findViewById(R.id.lvMenu);
		etFilter = (EditText) findViewById(R.id.etFilter);

		String login = MainActivity.etLogin.getText().toString();
		String password = MainActivity.etPassword.getText().toString();
		if (b != null) {
			LoadFromParcel(b);
		} else {
			try {
				String token = Logon.doLogin();
				String id = Logon.getSessionId(login, password, token);
				Document doc2 = Jsoup.connect("http://dunice.ru:7000/menu/")
						.cookie("sessionid", id).get();
				Elements el = doc2.select("tr#dish");
				for (Element e1 : el) {
					Elements el1 = e1.select("td#dish_name");
					Elements el2 = e1.select("td[id*=price]");
					Elements el3 = e1.select("input[type=checkbox]");
					if ((el1 != null) && (el2 != null) && (el1.get(0) != null)
							&& (el2.get(0) != null) && (el3.get(0) != null)) {
						items_data
								.add(new LVItems(el3.get(0).attr("name"), el1
										.get(0).ownText(),
										el2.get(0).ownText(), false));
					}

				}
			} catch (Exception e) {

			}
		}

		setTitle("Меню");
		
		final MyArrayAdapter adapter = new MyArrayAdapter(c,
				R.layout.listview_item_row, items_data);
		
		lvMenu.setAdapter(adapter);
		adapter.getFilter().filter(etFilter.getText());
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.ckbSet);
				boolean b = !adapter.getItem(arg2).checked;
				cb.setChecked(b);
			}
		});

		etFilter.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				adapter.getFilter().filter(s.toString());
				
			}
		});

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				if (result != null) {
					ArrayList<LVItems> res = new ArrayList<LVItems>();

					for (LVItems d : items_data) {
						if ((d != null) && d.checked) {
							res.add(d);
						}
					}
					result.OnApplicationDialogDissmiss(c, res);
				}

			}
		});

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
				if (result != null) {
					ArrayList<LVItems> res = new ArrayList<LVItems>();
					result.OnApplicationDialogDissmiss(c, res);
				}
	
			}
		});

	}

}
