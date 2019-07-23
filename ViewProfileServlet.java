package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ViewProfileServlet extends HttpServlet 
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		String username = (String)session.getAttribute("username");
	    out.print("Hello,"+username+" Welcome to Profile");
        LibraryDao dao = new LibraryDao();
        List<User> list=new ArrayList<User>();
	    try 
        {
	    	list=dao.viewProfile(username);
	    	out.print("<table border='1' width='100%'");  
	    	out.print("<tr><th>UserId</th><th>Username</th><th>Firstname</th><th>Lastname</th>"
			     		+ "<th>Phone Number</th><th>E-Mail</th><th>Last Login</th><th>Fine</th>");
	    	for(User user:list)
	    	{  
	    		out.print("<tr><td align=center>"+user.getUserID()+"</td><td align=center>"+user.getLogin().getUsername()
	    					+"</td><td align=center>"+user.getFirstname()+"</td><td align=center>"+user.getLastname()
	    					+"</td><td align=center>"+user.getPhoneNumber()+"</td><td align=center>"+user.getEmail()
	    					+"</td><td align=center>"+user.getLogin().getLastLogin()+"</td><td align=center>"+user.getFine()
	    					+"</td>");  
			}  
	    	out.print("</table>");
	    	if(dao.isAdmin(username))
	    	{
	    		out.print("<a href=admin-page.html>Back</a>");
	    	}
	    	else
	    	{
	    		out.print("<a href=customer-page.html>Back</a>");
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
}
