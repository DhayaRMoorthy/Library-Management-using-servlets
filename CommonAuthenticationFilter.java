package com.library;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CommonAuthenticationFilter implements Filter
{
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		response.setContentType("text/html");
		HttpSession session = req.getSession(false);
		PrintWriter out = res.getWriter();
		if(session!=null)
		{
			chain.doFilter(request, response);
		}
		else
		{
			out.println("<script type=\"text/javascript\">");
			out.println("alert('Login to access');");
			out.println("location='login.html';");
			out.println("</script>");
		}
	}

}
