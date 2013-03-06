package com.example.menudunice;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Boolean flag = false;
	// ProgressDialog Dialog = null;

	static MyTask mt;

	Button btnSet;
	Button btnLogin;

	public static EditText etLogin;
	public static EditText etPassword;

	static ProgressDialog Dialog;

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean("sd", flag);
	}

	public void onDestroy() {
		super.onDestroy();
		if (Dialog != null)
			if (Dialog.isShowing()) {
				Dialog.cancel();
			}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());
		setContentView(R.layout.activity_main);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		etLogin = (EditText) findViewById(R.id.etLogin);
		etPassword = (EditText) findViewById(R.id.etPassword);
		/*
		 * if( savedInstanceState != null ) { flag =
		 * savedInstanceState.getBoolean("sd"); if (flag == true) { try {
		 * showDil(); } catch (Exception e) { Toast.makeText(this,
		 * e.getMessage() , 5000).show(); } } }
		 */
		if (Dialog != null) {
			Dialog.show();
		}
		mt = (MyTask) getLastNonConfigurationInstance();
		if (mt == null) {
			mt = new MyTask();
		}
		mt.link(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin:
			// mt = new MyTask();
			// mt.link(this);
			if (mt == null) {
				mt = new MyTask();
				mt.link(this);
			}
			mt.execute();

			// mt.execute();
			break;
		default:
			break;
		}
	}

	/*
	 * public void showDil (){ Dialog = new ProgressDialog(MainActivity.this);
	 * Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * Dialog.setMessage("Авторизация..."); Dialog.setCancelable(false);
	 * Dialog.show();
	 */

	public Object onRetainNonConfigurationInstance() {
		// удаляем из MyTask ссылку на старое MainActivity
		if (mt == null) {
			mt = new MyTask();
			mt.link(this);
		}
		mt.unLink();
		return mt;
	}

	static class MyTask extends AsyncTask<String, Void, String> {
		private String sessionId;
		MainActivity activity;

		// получаем ссылку на MainActivity
		void link(MainActivity act) {
			activity = act;
		}

		// обнуляем ссылку
		void unLink() {
			activity = null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Dialog = new ProgressDialog(activity);
			Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			Dialog.setMessage("Авторизация...");
			Dialog.setCancelable(false);
			Dialog.show();
			/*
			 * showDil(); flag = true;
			 */

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String login = etLogin.getText().toString();
			String password = etPassword.getText().toString();
			try {
				String token = Logon.doLogin();
				sessionId = Logon.getSessionId(login, password, token);
				if (sessionId.equals("")) {
					return "";
				} else {
					Intent intent = new Intent(activity, MenuElement.class);

					intent.putExtra("id", sessionId);
					intent.putExtra("token", token);
					activity.startActivity(intent);
					// startActivity(intent);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			/*
			 * if (Dialog != null) { Dialog.dismiss(); } Dialog = null; flag =
			 * false;
			 */
			if ((Dialog != null) && Dialog.isShowing()) {
				Dialog.dismiss();
			}
			// Dialog.dismiss();
			Dialog = null;
			if (sessionId.equals("")) {
				Toast toast = Toast.makeText(activity,
						"Не правильный Логин/Пароль!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			mt = null;
		}
	}
}
