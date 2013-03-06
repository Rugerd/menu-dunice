package com.example.menudunice;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getingMenu {

	ArrayList<LVItems> items_data = new ArrayList<LVItems>();

	public getingMenu() throws Exception {

		String login = MainActivity.etLogin.getText().toString();
		String password = MainActivity.etPassword.getText().toString();
		//String id = extras.getString("id");
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
				items_data.add(new LVItems(el3.get(0).attr("name"), el1.get(0)
						.ownText(), el2.get(0).ownText(), false));

			}
		}
	}
}

