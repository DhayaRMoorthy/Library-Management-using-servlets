package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddUserServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		User user = new User();
		user.setFirstname(request.getParameter("firstname"));
		user.setLastname(request.getParameter("lastname"));
		user.setEmail(request.getParameter("email"));
		user.setPhoneNumber(request.getParameter("phoneNumber"));
		Login login = new Login();
		login.setUsername(request.getParameter("username"));
		login.setPassword(request.getParameter("password"));
		login.setUsertype(request.getParameter("usertype"));
		user.setLogin(login);
		
		LibraryDao dao = new LibraryDao();
		try 
		{
			dao.addUser(user);
			out.print("User successfully registered");
			request.getRequestDispatcher("add-user.html").include(request, response);
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