package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IssueBookServlet extends HttpServlet 
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		Integer bookId = Integer.parseInt(request.getParameter("bookId"));
		session.setAttribute("bookId", bookId);
		String username = (String) session.getAttribute("username");
		LibraryDao dao = new LibraryDao();
		try 
		{
			if(dao.isBookAvailable(bookId))
			{
				if(!dao.isAdmin(username))
				{
					dao.issueBook(bookId, username);
					out.println("The book has been issued");
					request.getRequestDispatcher("customer-page.html").include(request, response);
				}
				else
				{
					out.print("<!DOCTYPE html>");
					out.print("<html>");
					out.print("<head>");
					out.print("<meta charset=\"UTF-8\">");
					out.print("<title>Issue Book</title>");
					out.print("</head>");
					out.print("<body>");
					out.print("<form action=\"issueBook\" method=\"post\">");
					out.print("	Username :<input type=\"text\" name=\"username\"><br>");
					out.print("	<input type=\"submit\" value=\"Issue Book\">");
					out.print("</form>");
					out.print("</body>");
					out.print("</html>");
				}
			}
			else
			{
				out.print("The book is currently unavailable");
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
		response.setContentType("text/html");
		HttpSession session = request.getSession(false);
		if(session!=null)
		{
			Integer bookId = (Integer)(session.getAttribute("bookId"));
			String username = request.getParameter("username");
			PrintWriter out = response.getWriter();
			LibraryDao dao = new LibraryDao();
			try 
			{
				dao.issueBook(bookId, username);
				out.println("The book has been issued");
				session.removeAttribute("bookId");
				request.getRequestDispatcher("admin-page.html").include(request, response);
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
}