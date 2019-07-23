package com.library;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoginValidationFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		out.println("<script>");
		boolean status = true;
		if(username=="")
		{
			out.print("alert('Username cannot be empty');");
			status=false;
		}
		else if(password=="")
		{
			out.print("alert('Password cannot be empty');");
			status=false;
		}
		else if(username.length()<5)
		{
			out.print("alert('Username should have minimum of 5 characters');");
			status=false;
		}
		else 
		{
			for(int itr=0;itr<username.length();itr++)
			{
				char ch = username.charAt(itr);
				if(!((ch>=65 && ch<=90)||(ch>=97 && ch<=122)||(ch>=48 && ch<=57)))
				{
					out.print("alert('Invalid Username');");
					status=false;
				}
			}
		}
		if(status==true)
		{
			chain.doFilter(request, response);
		}
		else
		{
			out.println("location='login.html';");
			out.println("</script>");
		}
	}


}
