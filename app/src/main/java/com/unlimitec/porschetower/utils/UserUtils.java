package com.unlimitec.porschetower.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.unlimitec.porschetower.datamodel.UserObject;

public class UserUtils {

	private static final String SESSION_KEY = "session";
	private static final String APP = "com.unlimitec.porschetower";

	public static void storeSession(Context context, UserObject session) {

		JSONObject json = new JSONObject();
		try {
			if (session != null) {

				json.put("index", session.getIndex());
				json.put("first_name", session.getFirstName());
				json.put("last_name", session.getLastName());
				json.put("email", session.getEmail());
				json.put("password", session.getUserPass());
				json.put("phone", session.getPhone());
				json.put("unit", session.getUnit());
				json.put("id", session.getId());
				json.put("language", session.getLanguage());
				json.put("locale", session.getLocale());
				json.put("cat_id", session.getCatID());
				json.put("logoutTime", session.getLogoutTime());

				context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit()
						.putString(SESSION_KEY, json.toString()).commit();
			} else {
				context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit()
						.putString(SESSION_KEY, null).commit();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static UserObject getSession(Context context) {
		String jsString = context.getSharedPreferences(APP,
				Context.MODE_PRIVATE).getString(SESSION_KEY, null);
		UserObject user = null ;
		try {
			if (jsString != null) {
				user = new UserObject();
				JSONObject json = new JSONObject(jsString);

				user.setIndex(json.getInt("index"));
				user.setFirstName(json.getString("first_name"));
				user.setLastName(json.getString("last_name"));
				user.setEmail(json.getString("email"));
				user.setUserPass(json.getString("password"));
				user.setPhone(json.getString("phone"));
				user.setUnit(json.getJSONObject("unit"));
				user.setId(json.getString("id"));
				user.setLanguage(json.getString("language"));
				user.setLocale(json.getString("locale"));
				user.setIndex(json.getInt("cat_id"));
				user.setLogoutTime(json.getInt("logoutTime"));
			} else {
				return null;
			}
		} catch (JSONException e) {
			return user;
		}
		return user;
	}
}
