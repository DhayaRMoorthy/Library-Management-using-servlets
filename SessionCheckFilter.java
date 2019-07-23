package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionCheckFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession(false);
		PrintWriter out = res.getWriter();
		if(session!=null)
		{
			String username = (String) session.getAttribute("username");
			LibraryDao dao = new LibraryDao();
			try 
			{
				if(dao.isAdmin(username))
				{
					res.sendRedirect("admin-page.html");
				}
				else
				{
					res.sendRedirect("customer-page.html");
				}
			} 
			catch (ClassNotFoundException e) 
			{
				out.print(e);
			} 
			catch (SQLException e) 
			{
				out.print(e);
			}
		}
		else
		{
			chain.doFilter(request, response);
		}
	}


}
