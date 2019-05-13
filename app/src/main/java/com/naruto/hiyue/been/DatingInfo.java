package com.naruto.hiyue.been;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DatingInfo  {
	//private static final long serialVersionUID = 1L;
	private	String userName;
	@SerializedName("icon")
	private	String userIcon;
	@SerializedName("sex")
	private	String userSex;
	private	String theme;
	private	String place;
	private	String address;
	private	String activity;
	@SerializedName("publishTime")
	private	String time;
	private	String userId;
	private	String datingTime;
	private	String datingDate;
	private	String object;
	private	String city;
	private	String additionExplain;
	private	String datingId;

/*	@Override
	public String toString() {
		return "DatingInfo [userName=" + userName + ", userIcon=" + userIcon
				+ ", userSex=" + userSex + ", theme=" + theme + ", place="
				+ place + ", address=" + address + ", activity=" + activity
				+ ", time=" + time + ", userId=" + userId + ", datingTime="
				+ datingTime + ", datingDate=" + datingDate + ", object="
				+ object + ", city=" + city + ", additionExplain="
				+ additionExplain + ", datingId=" + datingId + "]";
	}*/

	public String getDatingId() {
		return datingId;
	}

	public void setDatingId(String datingId) {
		this.datingId = datingId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getTheme() {
		return theme;
	}

	public String getDatingTime() {
		return datingTime;
	}

	public void setDatingTime(String datingTime) {
		this.datingTime = datingTime;
	}

	public String getDatingDate() {
		return datingDate;
	}

	public void setDatingDate(String datingDate) {
		this.datingDate = datingDate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getAdditionExplain() {
		return additionExplain;
	}

	public void setAdditionExplain(String additionExplain) {
		this.additionExplain = additionExplain;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

/*	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DatingInfo() {
		// TODO Auto-generated constructor stub
	}*/

}
