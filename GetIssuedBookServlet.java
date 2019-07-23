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

public class GetIssuedBookServlet extends HttpServlet 
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		LibraryDao dao = new LibraryDao();
		try 
		{
			if(!dao.isAdmin(username)) 
			{
				displayIssuedBook(request,response,username);
			}
			else
	    	{
				out.print("<!DOCTYPE html>");
				out.print("<html>");
				out.print("<head>");
				out.print("<meta charset=\"UTF-8\">");
				out.print("<title>Get Username</title>");
				out.print("</head>");
				out.print("<body>");
				out.print("<form action=\"getIssuedBook\" method=\"post\">");
				out.print("	Username :<input type=\"text\" name=\"username\"><br>");
				out.print("	<input type=\"submit\" value=\"View Issued Books\">");
				out.print("</form>");
				out.print("</body>");
				out.print("</html>");
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
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String username = request.getParameter("username");
		displayIssuedBook(request,response,username);
	}
	
	public static void displayIssuedBook(HttpServletRequest request,HttpServletResponse response,String username) throws IOException
	{
		response.setContentType("text/html");
		LibraryDao dao= new LibraryDao();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		session.setAttribute("returnUserName", username);
		List<Transaction> list= new ArrayList<Transaction>();
		try 
		{
			list = dao.getIssuedBook(username);
			out.print("<table border='1' width='100%'");  
			out.print("<tr><th>TransactionId</th><th>Book Name</th><th>Username</th><th>Issued Date</th>"
					+ "<th>Due Date</th><th>Returned Date</th>");
			for(Transaction transaction:list)
			{  
				out.print("<tr><td align=center>"+transaction.getTransactionId()+"</td><td align=center>"+transaction.getBookName()
					+"</td><td align=center>"+transaction.getUserName()+"</td><td align=center>"+transaction.getIssuedDate()
					+"</td><td align=center>"+transaction.getDueDate()+"</td>");
				if(transaction.getReturnedDate()!=null)
				{
					out.print("<td align=center>"+transaction.getReturnedDate()+"</td></tr>");
				}
				else
				{
					out.print("</td><td align=center><a href='returnBook?bookId="+dao.getBookId(transaction.getBookName())+"'>Return</a></td></tr>");
				}
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