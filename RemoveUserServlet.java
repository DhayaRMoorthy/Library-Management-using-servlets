package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveUserServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String username = request.getParameter("username");
		PrintWriter out = response.getWriter();
		LibraryDao dao = new LibraryDao();
		try 
		{
			if(dao.removeUser(username))
			{
				out.print("User "+username+" successfully removed");
			}
			else
			{
				out.print("user "+username+" doesn't exist");
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
		response.sendRedirect("remove-user.html");
	}
}
