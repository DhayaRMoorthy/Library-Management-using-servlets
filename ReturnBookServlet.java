package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReturnBookServlet extends HttpServlet
{

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		Integer bookId = Integer.parseInt(request.getParameter("bookId"));
		String username =(String) session.getAttribute("username");
		String returnUsername = (String) session.getAttribute("returnUserName");
		LibraryDao dao = new LibraryDao();
		try 
		{
			dao.returnBook(bookId, returnUsername);
			dao.generateFine(bookId, username);
			out.print("Book successfully returned");
			if(dao.isAdmin(username))
			{
				request.getRequestDispatcher("admin-page.html").include(request, response);
			}
			else
			{
				request.getRequestDispatcher("customer-page.html").include(request, response);
			}
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
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		doGet(request,response);
	}
}