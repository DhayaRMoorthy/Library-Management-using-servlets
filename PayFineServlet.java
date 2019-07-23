package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PayFineServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("<!DOCTYPE html>");
		out.print("<html>");
		out.print("<head>");
		out.print("<meta charset=\"UTF-8\">");
		out.print("<title>Get Username</title>");
		out.print("</head>");
		out.print("<body>");
		out.print("<form action=\"payFine\" method=\"post\">");
		out.print("	Amount :<input type=\"number\" name=\"amount\"><br>");
		out.print("	<input type=\"submit\" value=\"Pay Fine\">");
		out.print("</form>");
		out.print("</body>");
		out.print("</html>");
		
	}
	
	public void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		Integer amount = Integer.parseInt(request.getParameter("amount"));
		PrintWriter out = response.getWriter();
		LibraryDao dao = new LibraryDao();
		try 
		{
			dao.payFine(username, amount);
			response.sendRedirect("customer-page.html");
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
