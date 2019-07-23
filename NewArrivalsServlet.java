package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewArrivalsServlet extends HttpServlet 
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		LibraryDao dao = new LibraryDao();
		List<Book> list=new ArrayList<Book>(); 
		PrintWriter out=response.getWriter();
		try 
		{
			list = dao.getNewArrival();
		} 
		catch (SQLException e) 
		{
			out.print(e);
		}
		catch (ClassNotFoundException e) 
		{
			out.print(e);
		} 
		 out.print("<table border='1' width='100%'");  
	     out.print("<tr><th>BookId</th><th>Title</th><th>Author</th><th>Publisher</th>"
	     		+ "<th>ISBN</th><th>Availability</th><th>Genre</th>");
	     for(Book book:list)
	     {  
	       out.print("<tr><td>"+book.getBookId()+"</td><td>"+book.getTitle()+"</td><td>"+book.getAuthorName()+"</td><td>"
	    		   		+book.getPublisherName()+"</td><td>"+book.getIsbn()+"</td><td>"
	    		   		+book.getAvailability()+"</td><td>"+book.getGenre()+"</td>");  
	     }  
	     out.print("</table>");  
	     out.print("<a href='index.html'>Back</a>");  
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		doGet(request,response);
	}
}
