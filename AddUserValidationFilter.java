package com.library;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AddUserValidationFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		boolean status = true;

		PrintWriter out = response.getWriter();
		LibraryDao dao = new LibraryDao();
		response.setContentType("text/html");
		out.println("<script>");
		try {
			if(username=="")
			{
				out.println("alert('Username cannot be empty');");
				status = false;
			}
			else if(password=="")
			{
				out.println("alert('Password cannot be empty');");
				status = false;
			}
			else if(email=="")
			{
				out.print("alert('e-mail cannot be empty');");
				status = false;
			}
			else if(phoneNumber=="")
			{
				out.print("alert('Phone Number cannot be empty');");
				status = false;
			}
			else if(!dao.isUsernameAvailable(username))
			{
				out.print("alert('Username is already taken');");
				status = false;
			}
			else if(!dao.isEmailRegistered(email))
			{
				out.print("alert('This email is already registered');");
				status = false;
			}
			else if(phoneNumber.length()!=10)
			{
				out.print("alert('Enter a valid Phone Number');");
				status = false;
			}
			else if(username.length()<7)
			{
				out.print("alert('Username should be atleast 7 characters');");
				status = false;
			}
			else if(password.length()<7)
			{
				out.print("alert('Password should be atleast 7 characters');");
				status = false;
			}
			else if(password.length()!=confirmPassword.length())
			{
				out.print("alert('Passwords dont match');");
				status = false;
			}
			else
			{
				for(Integer index=0;index<10;index++)
				{
					if(!((phoneNumber.charAt(index)>=48)&&(phoneNumber.charAt(index)<=57)))
					{
						out.print("alert('Invalid data in Phone Number');");
						status=false;
						break;
					}
				}
				for(Integer index=0;index<password.length();index++)
				{
					if(password.charAt(index)!=confirmPassword.charAt(index))
					{
						out.print("alert('Passwords dont match');");
						status=false;
						break;
					}
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
		if(status==true)
		{
			chain.doFilter(request, response);
		}
		else
		{
			out.println("location='add-user.html';");
			out.println("</script>");
		}
	}

}
