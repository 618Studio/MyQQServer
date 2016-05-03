package com.myqq.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.myqq.db.*;

public class ServerExchangeMessage {

	private final int L_port = 1100;
	private final int S_port = 1101;
	private Socket socket;
	
	public ServerExchangeMessage()

	{
		
		
		//开启长连接检测线程
		new Thread(new Runnable(){

			public void run()
			{		
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(L_port);
					while(true)
					{
						
						Socket socket = serverSocket.accept();
						//读取用户信息
						InputStream in = socket.getInputStream();
						DataInputStream ins = new DataInputStream(in);
						byte type = (byte) ins.read();
						int totalLen = ins.readInt();
						byte[] b = new byte[totalLen-4-1];
						ins.read(b);
						String clientid = new String(b,"UTF-8");
						
						//查找用户是否已经在线
						boolean flag = false;
						Client oneclient = null;
						for(Client client: DataCaching.onlineClient)
						{
							
							if(client.getId().equals(clientid))
							{
								oneclient = client;
								flag = true;
								break;
							}
						}
						
					
						if(flag == true)
						{
							//如果用户已经在线，关闭原有子线程，关闭原有的socket,改为现在的socket
							oneclient.getClientThread().stop();
							oneclient.getSocket().close();
							oneclient.setSocket(socket);
							
							//重新建立线程
					
							oneclient.setClientThread(new ClientSocketThread(socket,oneclient));
							oneclient.getClientThread().start();
						}
						else
						{
							//如果用户不在线，新建客户实例，并开启线程。
							oneclient = new Client(socket,clientid,new ClientSocketThread(socket,oneclient));
							synchronized(DataCaching.onlineClient)
							{
								DataCaching.onlineClient.add(oneclient);
								DataCaching.onlineNr++;
							}
							oneclient.getClientThread().start();
						}

					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
		
		//开启短连接检测线程
		new Thread(new Runnable(){
			public void run()
			{
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(S_port);
					while(true)
					{
						
						socket = serverSocket.accept();
						
						InputStream in = socket.getInputStream();
						DataInputStream ins = new DataInputStream(in);
						byte type = (byte) ins.read();
						int totalLen = ins.readInt();
						byte[] b = new byte[totalLen-4-1];
						ins.read(b);
						findReceiveMsgType(type,new String(b,"UTF-8"));
						socket.close();
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
		});
			
	}
	
	//短连接——服务器检验接收报文的类型并调用相应的处理方法
	private void findReceiveMsgType(byte type, String content)
	{
			
		if(type == MessageType.CLIENT_REQUEST_FRIENDS) //类型：客户请求好友列表
		{
			handleClientRequestFriends(content);
		}
		else if (type == MessageType.CLIENT_REQUEST_ONLINE) //类型：客户请求在线好友列表
		{
			handleClientRequestOnline(content);
		}
		else if(type == MessageType.CLIENT_REQUSET_LOGIN) //类型：客户请求登录
		{
			handleClientRequestLogin(content);
		}
	}
	
	//处理客户请求好友列表
	private void handleClientRequestFriends(String content)
	{
		
		Account account = new Account(content,null,null);
		String[][] friends = ConnectDB.getFriend(account);
		OutputStream out;
		try {
			out = socket.getOutputStream();
			DataOutputStream outs = new DataOutputStream(out);
		
			String s = null;
			for(int i=0;i<friends.length;i++)
			{
				s += friends[i][0] + "\n" +  friends[i][1] + "\n";
			}
			
			byte[] b = s.getBytes("UTF-8");
			int totalLen = 1 + 4 + b.length;
			outs.writeByte(MessageType.SERVER_RESPONSE_FRIENDS);
			outs.writeInt(totalLen);
			outs.write(b);
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//处理客户请求在线好友列表
	private void handleClientRequestOnline(String content)
	{
		Account account = new Account(content,null,null);
		String[][] friends = ConnectDB.getFriend(account);
		
		//匹配在线的好友
		String online = null;
		for(int i=0;i<friends.length;i++ )
		{
			for(Client client:DataCaching.onlineClient)
			{
				if(client.getClientThread().equals(friends[i][0]))
				{
					online += client.getId() + "\n";
				}
					
			}
		}
		
		
		//发送在线的好友
		OutputStream out;
		try {
			out = socket.getOutputStream();
			DataOutputStream outs = new DataOutputStream(out);
			byte[] b = online.getBytes("UTF-8");
			int totalLen = 1 + 4 + b.length;
			outs.writeByte(MessageType.SERVER_RESPONSE_ONLINE);
			outs.writeInt(totalLen);
			outs.write(b);
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//处理客户请求登录
	private void handleClientRequestLogin(String content)
	{
		String[] idAndPasswd = content.split("\n",2);
		Account account = new Account(idAndPasswd[0],null,idAndPasswd[1]);
		boolean flag = ConnectDB.checkAccount(account);
		
		if(flag==true)
		{
			//登录成功
			OutputStream out;
			try {
				out = socket.getOutputStream();
				DataOutputStream outs = new DataOutputStream(out);
				int totalLen = 1 + 4 + 1;
				outs.writeByte(MessageType.SERVER_RESPONSE_LOGIN);
				outs.writeInt(totalLen);
				outs.write(1);
				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//登录失败
			OutputStream out;
			try {
				out = socket.getOutputStream();
				DataOutputStream outs = new DataOutputStream(out);
				int totalLen = 1 + 4 + 1;
				outs.writeByte(MessageType.SERVER_RESPONSE_LOGIN);
				outs.writeInt(totalLen);
				outs.write(0);
				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
