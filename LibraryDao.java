package com.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDao 
{
	private Connection connect() throws ClassNotFoundException, SQLException
	{
		Connection con;
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root","pass");
		return con;
	}
	
	public boolean isAuthenticatedUser(String username,String password) throws SQLException, ClassNotFoundException
	{
		String query ="select password from login where username=?";
		ResultSet rs=null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next() && rs.getString(1).equals(password))
			{
				return true;
			}
			return false;
		}
		finally
		{
			rs.close();
		}
	}
	
	public boolean isAdmin(String username) throws SQLException, ClassNotFoundException
	{
		String query="select * from login where (username= ? and usertypeid = "
						+ "(select usertypeid from usertype where usertypename='Admin'))";
		ResultSet rs = null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1,username);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return true;
			}
			return false;
		}
		finally
		{
			rs.close();
		}
	}
	
	public void updateLastLogin(String username) throws SQLException, ClassNotFoundException
	{
		String query = "update login set lastLogin = now() where username=?";
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			pst.executeUpdate();
		}
	}
	
	public List<Book> getNewArrival() throws SQLException, ClassNotFoundException
	{
		String query = "select b.bookid,b.title,a.authorname,p.publishername,b.isbn,b.availability,b.genre "
				+ "from book b "
				+ "inner join author a on a.authorid=b.authorid "
				+ "inner join publisher p on p.publisherid=b.publisherid "
				+ "order by bookid desc limit 2";
		ResultSet rs = null;
		try (Connection con=connect(); Statement pst = con.createStatement())
		{
			rs=pst.executeQuery(query);
			return getBookFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	public void addBook(Book book) throws SQLException, ClassNotFoundException
	{
		String query="insert into book(title,authorid,publisherid,isbn,Availability,genre) values(?,?,?,?,'Y',?)";
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1,book.getTitle());
			pst.setInt(2,getAuthorId(book.getAuthorName()));
			pst.setInt(3,getPublisherId(book.getPublisherName()));
			pst.setInt(4,book.getIsbn());
			pst.setString(5,book.getGenre());
			pst.executeUpdate();
		}
	}
	
	public void issueBook(Integer bookId,String username) throws SQLException, ClassNotFoundException
	{
		String query = "insert into transaction(bookid,issueddate,duedate,userid) "
				+ "values(?,curdate(),date_add(curdate(), interval 3 day),"
				+ "(select userid from login where username=?))";
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			con.setAutoCommit(false);
			pst.setInt(1, bookId);
			pst.setString(2, username);
			pst.executeUpdate();
			query="update book set availability='N' where bookid=?";
			try(PreparedStatement pstd = con.prepareStatement(query))
			{
				pstd.setInt(1, bookId);
				pstd.executeUpdate();
				con.commit();
			}
		}
	}
	
	public boolean isBookAvailable(Integer bookId) throws SQLException, ClassNotFoundException
	{
		String query = "select * from book where bookid = ? and availability='Y'";
		ResultSet rs = null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setInt(1, bookId);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return true;
			}
			return false;
		}
		finally
		{
			rs.close();
		}
	}
	
	public List<Transaction> getIssuedBook(String username) throws SQLException, ClassNotFoundException
	{
		String query = "select t.transactionid,b.title,l.username,t.issueddate,t.duedate,t.returneddate"
							+ " from transaction t"
							+ " inner join book b on t.bookid=b.bookid"
							+ " inner join login l on l.userid=t.userid"
							+ " where l.userid=(select userid from login where username=?)";
		ResultSet rs = null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			rs = pst.executeQuery();
			return getTransactionFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	public void returnBook(Integer bookId,String username) throws SQLException, ClassNotFoundException
	{
		Integer transactionId = getTransactionId(bookId,username);
		String query = "update transaction set returneddate=curdate() where transactionid=?";
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setInt(1, transactionId);
			pst.executeUpdate();
			query = "update book set availability='y' where bookid = (select bookid from transaction where transactionid = ?)";
			try(PreparedStatement pstd = con.prepareStatement(query))
			{
				pstd.setInt(1, transactionId);
				pstd.executeUpdate();
			}
		}
	}
	
	public void generateFine(Integer bookId,String username) throws SQLException, ClassNotFoundException
	{
		Integer transactionId = getTransactionId(bookId,username);
		String query="select datediff(returneddate,duedate) from transaction where transactionid=?";
		ResultSet rs = null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setInt(1, transactionId);
			rs = pst.executeQuery();
			rs.next();
			Integer daysDelayed = rs.getInt(1);
			if(daysDelayed>0)
			{
				query="update user set fine = fine+(?*2)"
						+ " where userid=(select userid from transaction where transactionid=?)";
				try(PreparedStatement pstd = con.prepareStatement(query))
				{
					pstd.setInt(1, daysDelayed);
					pstd.setInt(2, transactionId);
					pstd.executeUpdate();
				}
				query="update transaction set fine = (?*2) where transactionid=?";
				try(PreparedStatement pstd = con.prepareStatement(query))
				{
					pstd.setInt(1, daysDelayed);
					pstd.setInt(2, transactionId);
					pstd.executeUpdate();
				}
			}
		}
		finally
		{
			rs.close();
		}
	}
	
	public void payFine(String username,Integer amount) throws SQLException, ClassNotFoundException
	{
		String query="update user set fine=fine-? where userid = (select userid from login where username = ?)";
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setInt(1, amount);
			pst.setString(2, username);
			pst.executeUpdate();
		}
	}
	
	private Integer getAuthorId(String authorName) throws SQLException, ClassNotFoundException
	{
		String query = "select authorid from author where authorname = ?";
		ResultSet rs = null;
		try (Connection con=connect(); PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, authorName);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}
			else
			{
				query="insert into author(authorname) values (?)";
				try (PreparedStatement pstd = con.prepareStatement(query))
				{
					pstd.setString(1, authorName);
					pstd.executeUpdate();
					return getAuthorId(authorName);
				}
			}
		}
		finally
		{
			rs.close();
		}
	}

	private int getPublisherId(String publisherName) throws SQLException, ClassNotFoundException
	{
		String query = "select publisherid from publisher where publishername = ?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, publisherName);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}
			else
			{
				query="insert into publisher(publishername) values (?)";
				try(PreparedStatement pstd = con.prepareStatement(query))
				{
					pstd.setString(1, publisherName);
					pstd.executeUpdate();
					return getPublisherId(publisherName);
				}
			}
		}
		finally
		{
			rs.close();
		}
	}

	public Integer getBookId(String bookName) throws SQLException, ClassNotFoundException
	{
		String query = "select bookid from book where title = ?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, bookName);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		}
		finally
		{
			rs.close();
		}
	}
	
	private Integer getTransactionId(Integer bookId,String username) throws SQLException, ClassNotFoundException
	{
		String query = "select max(transactionid) from transaction"
						+ " where bookid = ?"
						+ " and userid=(select userid from login where username=?)";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setInt(1, bookId);
			pst.setString(2, username);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		}
		finally
		{
			rs.close();
		}
	}
	
	public boolean isUsernameAvailable(String username) throws SQLException, ClassNotFoundException
	{
		String query = "select * from login where username=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return false;
			}
			return true;
		}
		finally
		{
			rs.close();
		}
	}

	public boolean isEmailRegistered(String email) throws SQLException, ClassNotFoundException
	{
		String query = "select * from user where email=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, email);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return false;
			}
			return true;
		}
		finally
		{
			rs.close();
		}
	}
	
	public void addUser(User user) throws SQLException, ClassNotFoundException
	{
		String query="insert into user (firstname,lastname,phonenumber,email) values(?,?,?,?)";
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, user.getFirstname());
			pst.setString(2, user.getLastname());
			pst.setString(3, user.getPhoneNumber());
			pst.setString(4, user.getEmail());
			pst.executeUpdate();
			query="insert into login (username,password,userid,usertypeid) "
					+ "values(?,?,"
					+ "(select userid from user where email=?),"
					+ "(select usertypeid from usertype where usertypename = ?))";
			try (PreparedStatement pstd = con.prepareStatement(query))
			{
				pstd.setString(1, user.getLogin().getUsername());
				pstd.setString(2, user.getLogin().getPassword());
				pstd.setString(3, user.getEmail());
				pstd.setString(4, user.getLogin().getUsertype());
				pstd.executeUpdate();
			}
		}
	}
	
	public boolean removeUser(String username) throws SQLException, ClassNotFoundException
	{
		Integer userId = getUserId(username);
		if(userId!=0)
		{
			String query = "delete from login where username=?";
			try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
			{
				pst.setString(1, username);
				pst.executeUpdate();
				query = "delete from user where userid=?";
				try(PreparedStatement pstd = con.prepareStatement(query))
				{
					pstd.setInt(1,userId);
					pstd.executeUpdate();
					return true;
				}
			}
		}
		else
		{
			return false;
		}
	}
	
	public List<User> viewProfile(String username) throws SQLException, ClassNotFoundException
	{
		String query ="select u.userid,l.username,u.firstname,u.lastname,u.phonenumber,u.email,l.lastLogin,u.fine"
				+ " from user u"
				+ " inner join login l on u.userid=l.userid"
				+ " where username=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			rs = pst.executeQuery();
			return getUserFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	public List<Transaction> getUnreturnedBook() throws SQLException, ClassNotFoundException
	{
		String query = "select t.transactionid,b.title,l.username,t.issueddate,t.duedate,t.returneddate"
							+ " from transaction t"
							+ " inner join book b on t.bookid=b.bookid"
							+ " inner join login l on l.userid=t.userid"
							+ " where returneddate is null";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			rs = pst.executeQuery();
			return getTransactionFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	public List<Transaction> getBorrowedBook(String username) throws SQLException, ClassNotFoundException
	{
		String query = "select t.transactionid,b.title,l.username,t.issueddate,t.duedate,t.returneddate"
							+ " from transaction t"
							+ " inner join book b on t.bookid=b.bookid"
							+ " inner join login l on l.userid=t.userid"
							+ " where returneddate is null"
							+ " and l.username = ?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, username);
			rs = pst.executeQuery();
			return getTransactionFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	private Integer getUserId(String username) throws SQLException, ClassNotFoundException
	{
		String query="select userid from login where username = ?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1,username);
			rs = pst.executeQuery();
			if(rs.next())
			{
				return rs.getInt(1);
			}
			else
			{
				return 0;
			}
		}
		finally
		{
			rs.close();
		}
	}
	
	public List<Book> getAllBooks() throws SQLException, ClassNotFoundException
	{
		String query = "select b.bookid,b.title,a.authorname,p.publishername,b.isbn,b.availability,b.genre "
							+ "from book b "
							+ "inner join author a on a.authorid=b.authorid "
							+ "inner join publisher p on p.publisherid=b.publisherid "
							+ "order by b.bookid";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			rs=pst.executeQuery();
			return getBookFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}

	public List<Book> searchByTitle(String title) throws SQLException, ClassNotFoundException
	{
		String query = "select b.bookid,b.title,a.authorname,p.publishername,b.isbn,b.availability,b.genre "
				+ "from book b "
				+ "inner join author a on a.authorid=b.authorid "
				+ "inner join publisher p on p.publisherid=b.publisherid "
				+ "where title=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, title);
			rs=pst.executeQuery();
			return getBookFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	public List<Book> searchByAuthor(String author) throws SQLException, ClassNotFoundException
	{
		String query = "select b.bookid,b.title,a.authorname,p.publishername,b.isbn,b.availability,b.genre from book b "
				+ "inner join author a on a.authorid=b.authorid "
				+ "inner join publisher p on p.publisherid=b.publisherid "
				+ "where a.authorname=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, author);
			rs=pst.executeQuery();
			return getBookFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}

	public List<Book> searchByPublisher(String publisher) throws SQLException, ClassNotFoundException
	{
		String query = "select b.bookid,b.title,a.authorname,p.publishername,b.isbn,b.availability,b.genre "
				+ "from book b "
				+ "inner join author a on a.authorid=b.authorid "
				+ "inner join publisher p on p.publisherid=b.publisherid "
				+ "where p.publishername=?";
		ResultSet rs = null;
		try(Connection con = connect();PreparedStatement pst = con.prepareStatement(query))
		{
			pst.setString(1, publisher);
			rs=pst.executeQuery();
			return getBookFromResultSet(rs);
		}
		finally
		{
			rs.close();
		}
	}
	
	private List<Book> getBookFromResultSet(ResultSet rs) throws SQLException
	{
		List<Book> list=new ArrayList<Book>();
		while(rs.next())
		{  
            Book book=new Book();  
            book.setBookId(Integer.valueOf(rs.getInt(1))); 
            book.setTitle(rs.getString(2));  
            book.setAuthorName(rs.getString(3)); 
            book.setPublisherName(rs.getString(4));
            book.setIsbn(Integer.valueOf(rs.getInt(5))); 
            book.setAvailability(rs.getString(6)); 
            book.setGenre(rs.getString(7));
            list.add(book);  
        }
		return list;
	}
	
	private List<User> getUserFromResultSet(ResultSet rs) throws SQLException
	{
		List<User> list=new ArrayList<User>();
		while(rs.next())
		{  
            User user=new User();
            Login login = new Login();
            user.setUserID(Integer.valueOf(rs.getInt(1)));
            user.setFirstname(rs.getString(3));
            user.setLastname(rs.getString(4));
            user.setPhoneNumber(rs.getString(5));
            user.setEmail(rs.getString(6));
            user.setFine(Integer.valueOf(rs.getInt(8)));
            login.setUsername(rs.getString(2));
            login.setLastLogin(rs.getTimestamp(7));
            user.setLogin(login);
            list.add(user);  
        }
		return list;
	}
	
	private List<Transaction> getTransactionFromResultSet(ResultSet rs) throws SQLException
	{
		List<Transaction> list=new ArrayList<Transaction>();
		while(rs.next())
		{  
            Transaction transaction = new Transaction();
            transaction.setTransactionId(Integer.valueOf(rs.getInt(1)));
            transaction.setBookName(rs.getString(2));
            transaction.setUserName(rs.getString(3));
            transaction.setIssuedDate(rs.getDate(4));
            transaction.setDueDate(rs.getDate(5));
            transaction.setReturnedDate(rs.getDate(6));
            list.add(transaction);  
        }
		return list;
	}
}