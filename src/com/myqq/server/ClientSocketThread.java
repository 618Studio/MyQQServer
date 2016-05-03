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
	public final byte[] lock = new byte[0];  //���ڷ�����Ϣ����
	private volatile boolean exit = false;  //�����˳����߳�
	public ClientSocketThread(Socket soc,Client client)
	{
		this.socket = soc; 
		this.client = client;
		thread = new Thread(new Runnable(){
			public void run()
			{
				//��ʼ�߳�
				//����
				System.out.println("��ʼ�߳�");
				while(!exit)
				{
					//��ʼ��ȡ���ݰ�
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
	
	//������������ձ��ĵ����Ͳ�������Ӧ�Ĵ�����
	private void findReceiveMsgType(byte type, String content)
	{
		
		if(type == MessageType.CLIENT_SEND_HEART_BEAT) //���ͣ��ͻ����͵�������
		{
			handleClientHeartBeat();
		}
		else if (type == MessageType.CLIENT_SEND_MESSAGE) //���ͣ��ͻ����͵������¼
		{
			handleClientChatMessage(content);
		}
		else if(type == MessageType.CLIENT_RECV_MESSAGE) //���ͣ��ͻ����͵Ľ��������¼��Ӧ
		{
			handleClientResponseChatMessage(content);
		}
	}
	
	//����ͻ����͵�������
	private void handleClientHeartBeat()
	{
		//����
		System.out.println("�յ�����");
		//������һ������ʱ���
		Calendar calendar = Calendar.getInstance();
		client.setLastTime(calendar);
				
	}
	
	//����ͻ����͵������¼
	private void handleClientChatMessage(String content)
	{
		//����
		System.out.println("��ʼ���������¼");
		String[] msg = content.split("\n",5);
		
		//����
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
		//�������
		if(flag==true)
		{
			synchronized(DataCaching.tempMessage)
			{
				DataCaching.tempMessage.add(chatMessage);	
			}
			
			
			//ֱ�ӷ�����Ϣ
			synchronized(online.getClientThread().lock)
			{
			
				try {
					Socket socket = online.getSocket();
					OutputStream out;
					out = socket.getOutputStream();
					DataOutputStream outs = new DataOutputStream(out);
					byte[] b = content.getBytes("UTF-8");
					int totalLen = 1 + 4 + b.length;

					
					//������Ϣ
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
			
			//���ԣ�
			System.out.println(chatMessage.getContent());
			
			//�������ݿ�
			ConnectDB.saveMessage(chatMessage);
		
		}
	}
	
	//����ͻ����͵Ľ��������¼��Ӧ
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
	
	//�����߳�
	public void start()
	{
		thread.start();
	}
	
	//�ر��߳�
	public void stop()
	{
		exit = true;
	}	
}