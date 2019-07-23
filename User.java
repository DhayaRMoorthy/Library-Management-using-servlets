package com.library;

public class User 
{
	private Integer userID;
	private String lastname;
	private String firstname;
	private String phoneNumber;
	private String email;
	private Login login;
	private Integer fine;
	
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
	public Integer getFine() {
		return fine;
	}
	public void setFine(Integer fine) {
		this.fine = fine;
	}
	
	@Override
	public String toString() {
		return "User [userID=" + userID + ", lastname=" + lastname + ", firstname=" + firstname + ", phoneNumber="
				+ phoneNumber + ", email=" + email + ", login=" + login + "]";
	}
	
}
