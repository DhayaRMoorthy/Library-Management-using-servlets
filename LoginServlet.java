package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out= response.getWriter();
		HttpSession session = request.getSession(false);
		response.setContentType("text/html");
		LibraryDao dao = new LibraryDao();
		try 
		{
			if(dao.isAuthenticatedUser(username, password))
			{
				session = request.getSession();
				session.setAttribute("username", username);
				request.setAttribute("username", username);
				if(dao.isAdmin(username))
				{
					response.sendRedirect("admin-page.html");
				}
				else
				{
					response.sendRedirect("customer-page.html");
				}
			}
			else
			{	
				out.println("alert('Invalid Username or Password');");
				out.println("location='login.html';");
				out.println("</script>");
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
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		//response.sendRedirect("login.html");
	}
}
