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

public class CustomerAuthenticationFilter implements Filter
{
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		if(session!=null)
		{
			LibraryDao dao = new LibraryDao();
			String username = (String) session.getAttribute("username");
			try 
			{
				if(!dao.isAdmin(username))
				{
					chain.doFilter(request, response);
				}
				else
				{
					res.sendRedirect("admin-page.html");
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
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Unauthorized Access');");
			out.println("location='login.html';");
			out.println("</script>");
		}
	}


}
