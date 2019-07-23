package com.library;

import java.sql.Timestamp;

public class Login 
{
	private String username;
	private String password;
	private String usertype;
	private Timestamp lastLogin;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	@Override
	public String toString() {
		return "Login [username=" + username + ", password=" + password + ", lastLogin=" + lastLogin + ", usertype="
				+ usertype + "]";
	}
}
