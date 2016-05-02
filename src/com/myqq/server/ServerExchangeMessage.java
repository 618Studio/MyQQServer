package com.myqq.server;

import java.io.DataInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.myqq.db.*;

public class ServerExchangeMessage {

	
	public ServerExchangeMessage()

	{
		final int L_port = 1100;
		final int S_port = 1101;
		
		//���������Ӽ���߳�
		new Thread(new Runnable(){

			public void run()
			{		
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(L_port);
					while(true)
					{
						
						Socket socket = serverSocket.accept();
						//��ȡ�û���Ϣ
						InputStream in = socket.getInputStream();
						DataInputStream ins = new DataInputStream(in);
						byte type = (byte) ins.read();
						int totalLen = ins.readInt();
						byte[] b = new byte[totalLen-4-1];
						ins.read(b);
						String clientid = new String(b,"UTF-8");
						
						//�����û��Ƿ��Ѿ�����
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
							//����û��Ѿ����ߣ��ر�ԭ�����̣߳��ر�ԭ�е�socket,��Ϊ���ڵ�socket
							oneclient.getClientThread().stop();
							oneclient.getSocket().close();
							oneclient.setSocket(socket);
							
							//���½����߳�
					
							oneclient.setClientThread(new ClientSocketThread(socket,oneclient));
							oneclient.getClientThread().start();
						}
						else
						{
							//����û������ߣ��½��ͻ�ʵ�����������̡߳�
							oneclient = new Client(socket,clientid,new ClientSocketThread(socket,oneclient));
							synchronized(DataCaching.onlineClient)
							{
								DataCaching.onlineClient.add(oneclient);
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
		
		
		//���������Ӽ���߳�
		new Thread(new Runnable(){
			public void run()
			{
				ServerSocket serverSocket;
				try {
					serverSocket = new ServerSocket(S_port);
					while(true)
					{
						
						Socket socket = serverSocket.accept();
						
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
	
	//�����ӡ���������������ձ��ĵ����Ͳ�������Ӧ�Ĵ�����
	private void findReceiveMsgType(byte type, String content)
	{
			
		if(type == MessageType.CLIENT_REQUEST_FRIENDS) //���ͣ��ͻ���������б�
		{
			handleClientRequestFriends(content);
		}
		else if (type == MessageType.CLIENT_REQUEST_ONLINE) //���ͣ��ͻ��������ߺ����б�
		{
			handleClientRequestOnline(content);
		}
		else if(type == MessageType.CLIENT_REQUSET_LOGIN) //���ͣ��ͻ������¼
		{
			handleClientRequestLogin(content);
		}
	}
	
	//����ͻ���������б�
	private void handleClientRequestFriends(String content)
	{
		
		Account account = new Account(content, content, content);
		String[][] list = ConnectDB.getFriend(account);
		
	}
	
	//����ͻ��������ߺ����б�
	private void handleClientRequestOnline(String content)
	{
		
	}
	
	//����ͻ������¼
	private void handleClientRequestLogin(String content)
	{
		
	}
}
