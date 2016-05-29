package com.myqq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.myqq.server.Account;
import com.myqq.server.ChatMessage;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class ConnectDB {
	/*public static void main(String args[]){
		ConnectDB connect = new ConnectDB();
		String s = "2016-05-03 09:25:12";
		Timestamp time = Timestamp.valueOf(s);
		ChatMessage mes = new ChatMessage("1", "10000", "10001", s, "helloworld!");
		System.out.println(connect.saveMessage(mes));
		Account a = new Account("10000",null,null);
		getFriend(a);
	}*/
	private static ConnectDB instance = new ConnectDB();
	public static ConnectDB getInstance(){
		   return instance;
	}
	
	public static Connection conn;
	//public static ResultSet rs;
	
	private void closeDB(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize()
    {
		 instance.closeDB(); 
    }
	
	private ConnectDB() {
		// ����������
		String driver = "com.mysql.jdbc.Driver";
		// URLָ��Ҫ���ʵ����ݿ���
		String url = "jdbc:mysql://127.0.0.1:3306/myQQ";
		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "apple";
		try {
			// ������������
			Class.forName(driver);
			// �������ݿ�
			conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
		} catch (Exception e) {
			System.out.print("Connected fail!");
			System.exit(1);
		}
	}
	
	private static int update(String sql){
		int rs;
		try {
			// statement����ִ��SQL���
			Statement statement = conn.createStatement();
			// Ҫִ�е�SQL���
			rs = statement.executeUpdate(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private static ResultSet query(String sql) {
		ResultSet rs;
		try {
			// statement����ִ��SQL���
			Statement statement = conn.createStatement();
			// Ҫִ�е�SQL���
			// �����
			rs = statement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//���غ����б�
	public static String[][] getFriend(Account account){
		String sql = "SELECT FB FROM friend where FA = '"+account.getId()+"';";
		ResultSet res = query(sql);
		try{
			res.last();
	        int row = res.getRow();
	        res.beforeFirst();//���ع�
	        	String[][] result= new String[row][2];
		
			int i = 0;
			while(res.next()){
				String findNickname = "SELECT Anickname FROM account where Aid='"+res.getString("FB")+"';";
				ResultSet res2 = query(findNickname);
				result[i][0] = res.getString("FB");
				res2.next();
				result[i++][1] = res2.getString("Anickname"); 
			}
			return result;
		}
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	//����������Ϣ
	public static boolean saveMessage(ChatMessage message){
		String sql = "insert into delaymessage(Mid,Mfrom,Mto,Mtime,Mcontent) values('" + message.getId() + "','" + message.getFrom() +"','"+message.getTo() + "','" + message.getTime()+ "','" +message.getContent()+"')";
		if (update(sql)!=-1)
			return true;
		else return false;
	}
	
	//����������Ϣ
	public static ChatMessage[] getDelayMessage(String id){
		String sql="SELECT * FROM myQQ.delaymessage where Mto='"+id+"';";
		ResultSet res=query(sql);
		try{
			res.last();
	        int row = res.getRow();
	        res.beforeFirst();//���ع�
	        	ChatMessage[] result= new ChatMessage[row];
		
			int i = 0;
			while(res.next()){
				result[i++] = new ChatMessage(res.getString("Mid"), 
											res.getString("Mfrom"), 
											res.getString("Mto"),
											res.getString("Mtime"),
											res.getString("Mcontent"));
			}
			String delete = "delete from delaymessage where Mto='"+id+"'";
			update(delete);
			return result;
		}catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	//ע��
	public static boolean register(Account account){
		String sql = "select * from account where Aid = '"+account.getId()+"'";
		ResultSet rst=query(sql);
		try {
			rst.last();
			if (rst.getRow() !=0) return false;
			else{
				sql = "insert into account(Aid,Anickname,Apassword) values('" + account.getId() + "','" + account.getNickname() + "','" +account.getPassword()+"')";
				if (update(sql)!=-1)
					return true;
				else return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean checkAccount(Account account) {
		String sql = "select * from account where Aid = '"+account.getId() +"'";
		ResultSet rs=query(sql);
		try{
			rs.next();
			if(rs.getString("Apassword").equals(account.getPassword())){
				return true;
			}else
				return false;
		}catch(Exception e){
			System.exit(1);
			return false;
		}
	}
}