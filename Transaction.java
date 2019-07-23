package com.library;
import java.sql.Date;
public class Transaction 
{
	
	private Integer transactionId;
	private String bookName;
	private String userName;
	private Date issuedDate;
	private Date dueDate;
	private Date returnedDate;
	

	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getReturnedDate() {
		return returnedDate;
	}
	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}
	
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", bookName=" + bookName + ", userName=" + userName
				+ ", issuedDate=" + issuedDate + ", dueDate=" + dueDate + ", returnedDate=" + returnedDate + "]";
	}
}
