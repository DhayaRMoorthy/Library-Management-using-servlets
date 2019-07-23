package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminAuthenticationFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession(false);
		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
		if(session!=null)
		{
			String username = (String) session.getAttribute("username");
			LibraryDao dao = new LibraryDao();
			try 
			{
				if(dao.isAdmin(username))
				{
					chain.doFilter(request, response);
				}
				else
				{
					out.println("<script type=\"text/javascript\">");
					out.println("alert('Access Denied');");
					out.println("location='customer-page.html';");
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
		else
		{
			out.print("Unauthorized access request");	
			req.getRequestDispatcher("login.html").include(req, res);
		}
	}


}
