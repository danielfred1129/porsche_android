package com.pos.porschetower.datamodel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;

public class UserObject implements Serializable {
	private int index;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private JSONObject unit;
    private String userPass;
	private String language;
	private String locale;
	private String id;
	private int catID;
	private int logoutTime;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
	}

	public String getUserPass() {return userPass; }

    public void setUserPass(String userPass) { this.userPass = userPass; }

	public String getPhone() {return phone; }

	public void setPhone(String phone) { this.phone = phone; }
	
	public JSONObject getUnit() {
		return unit;
	}
	
	public void setUnit(JSONObject unit) {
		this.unit = unit;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

	public int getLogoutTime() { return logoutTime; }

	public void setLogoutTime(int logoutTime) { this.logoutTime = logoutTime; }

    public boolean isChecked = false;
}
