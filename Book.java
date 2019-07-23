package com.library;

public class Book 
{
	private Integer bookId;
	private String title;
	private String authorName;
	private String publisherName;
	private Integer isbn;
	private String availability;
	private String genre;
	
	public Integer getBookId() {
		return this.bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public Integer getIsbn() {
		return this.isbn;
	}
	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getGenre() {
		return this.genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	@Override
	public String toString() {
		return "Book [BookId=" + bookId + ", title=" + title + ", authorName=" + authorName + ", publisherName="
				+ publisherName + ", isbn=" + isbn + ", availability=" + availability + ", genre=" + genre + "]";
	}
}
