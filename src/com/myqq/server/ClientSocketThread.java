package com.myqq.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

import com.myqq.db.ConnectDB;

public class ClientSocketThread {

	private Socket socket;
	private Thread thread;
	private Client client;
	public final byte[] lock = new byte[0];  //用于发送信息的锁
	private volatile boolean exit = false;  //用于退出该线程
	public ClientSocketThread(Socket soc,Client client)
	{
		this.socket = soc; 
		this.client = client;
		thread = new Thread(new Runnable(){
			public void run()
			{
				//开始线程
				//测试
				System.out.println("开始线程");
				while(!exit)
				{
					//开始读取数据包
					try {
						InputStream in = socket.getInputStream();
						DataInputStream ins = new DataInputStream(in);
						byte type = (byte) ins.read();
						int totalLen = ins.readInt();
						byte[] b = new byte[totalLen-4-1];
						ins.read(b);
						String content = new String(b,"UTF-8");
						findReceiveMsgType(type,content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}
		});
	}
	
	//服务器检验接收报文的类型并调用相应的处理方法
	private void findReceiveMsgType(byte type, String content)
	{
		
		if(type == MessageType.CLIENT_SEND_HEART_BEAT) //类型：客户发送的心跳包
		{
			handleClientHeartBeat();
		}
		else if (type == MessageType.CLIENT_SEND_MESSAGE) //类型：客户发送的聊天记录
		{
			handleClientChatMessage(content);
		}
		else if(type == MessageType.CLIENT_RECV_MESSAGE) //类型：客户发送的接收聊天记录回应
		{
			handleClientResponseChatMessage(content);
		}
	}
	
	//处理客户发送的心跳包
	private void handleClientHeartBeat()
	{
		//测试
		System.out.println("收到心跳");
		//设置上一次心跳时间点
		Calendar calendar = Calendar.getInstance();
		client.setLastTime(calendar);
				
	}
	
	//处理客户发送的聊天记录
	private void handleClientChatMessage(String content)
	{
		//测试
		System.out.println("开始接受聊天记录");
		String[] msg = content.split("\n",5);
		
		//测试
		System.out.println(msg[0]+msg[1]+msg[2]+msg[3]+msg[4]);
		ChatMessage chatMessage = new ChatMessage(msg[0],msg[1],msg[2],msg[3],msg[4]);
		
		Client online = null;
		boolean flag = false;
		for(Client client: DataCaching.onlineClient)
		{
			if(msg[2].equals(client.getId()))
			{
				online = client;
				flag = true;
				break;
			}
		}
		
		System.out.println(flag);
		//如果在线
		if(flag==true)
		{
			synchronized(DataCaching.tempMessage)
			{
				DataCaching.tempMessage.add(chatMessage);	
			}
			
			
			//直接发送信息
			synchronized(online.getClientThread().lock)
			{
			
				try {
					Socket socket = online.getSocket();
					OutputStream out;
					out = socket.getOutputStream();
					DataOutputStream outs = new DataOutputStream(out);
					byte[] b = content.getBytes("UTF-8");
					int totalLen = 1 + 4 + b.length;

					
					//发送信息
					outs.writeByte(MessageType.SERVER_SEND_MESSAGE);
					outs.writeInt(totalLen);
					outs.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
		}
		else
		{
			
			//测试！
			System.out.println(chatMessage.getContent());
			
			//存至数据库
			ConnectDB.saveMessage(chatMessage);
		
		}
	}
	
	//处理客户发送的接收聊天记录回应
	private void handleClientResponseChatMessage(String content)
	{
		int index = 0;
		for(ChatMessage msg: DataCaching.tempMessage)
		{
			if(msg.getId().equals(content))
			{
				DataCaching.tempMessage.remove(index);
				break;
			}
			index++;
		}
	}
	
	//开启线程
	public void start()
	{
		thread.start();
	}
	
	//关闭线程
	public void stop()
	{
		exit = true;
	}	
}