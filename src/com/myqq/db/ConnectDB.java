package com.myqq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.myqq.server.Account;
import com.myqq.server.ChatMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

public class ConnectDB {
	public static void main(String args[]){
		ConnectDB conn = new ConnectDB();
	}
	
	public static Connection conn;
	public static ResultSet rs;
	
	public void connectDB() {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名
		String url = "jdbc:mysql://127.0.0.1:3306/myQQ";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "apple";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连接数据库
			conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
		} catch (Exception e) {
			System.out.print("Connected fail!");
			System.exit(1);
		}
	}
	
	private static ResultSet query(String sql) {
		try {
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			// 结果集
			rs = statement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[][] getFriend(Account account){
		String sql = "SELECT FB FROM friend where FA = '"+account.getId()+"';";
		ResultSet res = query(sql);
		try{
			res.last();
	        int row = res.getRow();
	        rs.beforeFirst();//光标回滚
	        	String[][] result= new String[row][2];
		
			int i = 0;
			while(res.next()){
				String findNickname = "SELECT Anickname FROM account where Aid='"+res.getString("FB")+"';";
				ResultSet res2 = query(findNickname);
				result[i][0] = res.getString("FB");
				result[i++][1] = res2.getString("Anickname"); 
			}
			return result;
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean saveMessage(ChatMessage message){
		String sql = "insert into delaymessage(Mid,Mfrom,Mto,Mtime,Mcontent) values('" + message.getId() + "','" + message.getFrom() +"','"+message.getTo() + "','" + message.getTime()+ "','" +message.getContent()+"')";
		if (query(sql)!=null)
			return true;
		else return false;
	}
	
	public boolean register(Account account){
		String sql = "select * from account";
		ResultSet rst=query(sql);
		if (rst==null) System.exit(1);
		sql = "insert into account(Aid,Anickname,Apassword) valuse('" + account.getId() + "','" + account.getNickname() + "','" +account.getPassword()+"')";
		if (query(sql)!=null)
			return true;
		else return false;
	}
}