package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		LibraryDao dao = new LibraryDao();
		try 
		{
			dao.updateLastLogin(username);
			session.invalidate();
			response.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
		    response.setHeader("Pragma","no-cache");
		    response.setDateHeader ("Expires", 0);
		    response.sendRedirect("index.html");
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
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		doGet(request,response);
	}
}