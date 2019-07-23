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

public class GetUnreturnedBookServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException
	{
		LibraryDao dao = new LibraryDao();
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		PrintWriter out = response.getWriter();
		List<Transaction> list = new ArrayList<Transaction>();
		try 
		{
			if(dao.isAdmin(username))
			{
			list = dao.getUnreturnedBook();
			}
			else
			{
				list = dao.getBorrowedBook(username);
			}
			out.print("<table border='1' width='100%'");  
			out.print("<tr><th>TransactionId</th><th>Book Name</th><th>Username</th><th>Issued Date</th>"
					+ "<th>Due Date</th><th>Action</th><tr>");
			for(Transaction transaction:list)
			{  
				out.print("<tr><td align=center>"+transaction.getTransactionId()+"</td><td align=center>"+transaction.getBookName()
					+"</td><td align=center>"+transaction.getUserName()+"</td><td align=center>"+transaction.getIssuedDate()
					+"</td><td align=center>"+transaction.getDueDate()+"</td>");
				out.print("</td><td align=center><a href='returnBook?bookId="+dao.getBookId(transaction.getBookName())+"'>Return</a></td></tr>");
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
	public void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException
	{
		doGet(request,response);
	}
}
