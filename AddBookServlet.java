package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddBookServlet extends HttpServlet
{
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.sendRedirect("add-book.html");
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		LibraryDao dao = new LibraryDao();
		
		Book book = new Book();
		book.setTitle(request.getParameter("title"));
		book.setAuthorName(request.getParameter("author"));
		book.setPublisherName(request.getParameter("publisher"));
		book.setIsbn(Integer.parseInt(request.getParameter("isbn")));
		book.setGenre(request.getParameter("genre"));
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try 
		{
			dao.addBook(book);
			out.print("Book has been successfully added");
			request.getRequestDispatcher("add-book.html").include(request, response);
		} 
		catch (SQLException e) 
		{
			out.print(e);
		}
		catch (ServletException e) 
		{
			out.print(e);
		} 
		catch (ClassNotFoundException e) 
		{
			out.print(e);
		} 
		
		
	}
}
