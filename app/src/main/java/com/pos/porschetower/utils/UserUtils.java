package com.pos.porschetower.utils;

import android.content.Context;

import com.pos.porschetower.datamodel.UserObject;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUtils {

	private static final String SESSION_KEY = "session";
	private static final String SELECTED_CAR = "selected_car";
	private static final String VALET = "valet";
	private static final String SCHEDULE_DATA = "scheduleData";
	private static final String SELECTED_CATEGORY = "selected_category";
	private static final String PARKING_SPACE_ID = "parking_space_id";
	private static final String APP = "com.pos.porschetower";

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
						.putString(SESSION_KEY, json.toString()).apply();
			} else {
				context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit()
						.putString(SESSION_KEY, null).apply();
			}
		} catch (JSONException e) {
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
				String idStr = json.getString("id");
				user.setId(idStr);
				user.setLanguage(json.getString("language"));
				user.setLocale(json.getString("locale"));
				user.setCatID(json.getInt("cat_id"));
				user.setLogoutTime(json.getInt("logoutTime"));
			} else {
				return null;
			}
		} catch (JSONException e) {
			return user;
		}
		return user;
	}
	public static void storeValet(Context context, String valet) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(VALET, valet).apply();
	}
	public static String getValet(Context context) {
		return context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(VALET, null);
	}
	public static void storeScheduleDataArray(Context context, String scheduleString) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SCHEDULE_DATA, scheduleString).apply();
	}
	public static String getScheduleDataArray(Context context) {
		return context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SCHEDULE_DATA, null);
	}
	public static void storeScheduleData(Context context, String scheduleString) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CAR, scheduleString).apply();
	}
	public static String getScheduleData(Context context) {
		return context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CAR, null);
	}
	public static void storeSelectedCar(Context context, JSONObject selectedCar) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CAR, selectedCar.toString()).apply();
	}
	public static String getSelectedCategory(Context context) {
		return context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CATEGORY, null);
	}
	public static void storeSelectedCategory(Context context, String selectedCategory) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(SELECTED_CATEGORY, selectedCategory).apply();
	}

	public static void storeParkingSpaceID(Context context, String parkingSpaceID) {
		context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putString(PARKING_SPACE_ID, parkingSpaceID).apply();
	}

	public static String getParkingSpaceID(Context context) {
		return context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(PARKING_SPACE_ID, null);
	}

	public static JSONObject getSelectedCar(Context context) {
		String jsString = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getString(SELECTED_CAR, null);
		JSONObject jsonObject;
		try {
			if (jsString != null) {
				jsonObject = new JSONObject(jsString);
			}
			else
			{
				return null;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return jsonObject;
	}
}
