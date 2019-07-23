package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		LibraryDao dao = new LibraryDao();
		List<Book> list=new ArrayList<Book>(); 
		PrintWriter out=response.getWriter();
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		try 
		{
			if(request.getParameter("publisher")!=null)
			{
				list = dao.searchByPublisher(request.getParameter("publisher"));
			}
			else if(request.getParameter("author")!=null)
			{
				list = dao.searchByAuthor(request.getParameter("author"));
			}
			else if(request.getParameter("title")!=null)
			{
				list = dao.searchByTitle(request.getParameter("title"));
			}
			else
			{
				list = dao.getAllBooks();
			}
			 out.print("<table border='1' width='100%'");  
		     out.print("<tr><th>BookId</th><th>Title</th><th>Author</th><th>Publisher</th>"
		     		+ "<th>ISBN</th><th>Availability</th><th>Genre</th><th>Action</th>");
		     for(Book book:list)
		     {  
		    	 out.print("<tr><td align=center>"+book.getBookId()+"</td><td align=center>"+book.getTitle()
		    	 	+"</td><td align=center>"+book.getAuthorName()+"</td><td align=center>"+book.getPublisherName()
		    	 	+"</td><td align=center>"+book.getIsbn()+"</td><td align=center>"+book.getAvailability()
		    	 	+"</td><td align=center>"+book.getGenre());
		    	 if(book.getAvailability().equalsIgnoreCase("Y"))
		    	 {
			    	 if(dao.isAdmin(username))
			    	 {
			    		 out.print("</td><td align=center><a href='issueBook?bookId="+book.getBookId()+"'>Issue</a></td></tr>");
			    	 }
			    	 else
			    	 {
			    		 out.print("</td><td align=center><a href='issueBook?bookId="+book.getBookId()+"'>Borrow</a></td></tr>");
			    	 }
		    	 }
		    	 else
		    	 {
		    		 out.print("</td><td></td></tr>");
		    	 }
		     } 
		     out.print("</table>"); 

	    	 if(dao.isAdmin(username))
	    	 {
	    		 out.print("<a href='admin-page.html'>Back</a>");
	    	 }
	    	 else
	    	 {
	    		 out.print("<a href='customer-page.html'>Back</a></td>");
	    	 }
		} 
		catch (SQLException e) 
		{
			out.print(e);
		}
		catch (ClassNotFoundException e) 
		{
			out.print(e);
		}   
	}
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.sendRedirect("search-book.html");
	}
	
}
