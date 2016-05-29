package com.myqq.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

import com.myqq.db.ConnectDB;

public class Client {

	private Socket socket;
	private String id;
	private Calendar lastTime;
	private ClientSocketThread clientThread;
	
	public Client(Socket socket, String id, ClientSocketThread clientThread)
	{
		this.socket = socket;
		this.id = id;
		this.clientThread = clientThread;
		this.lastTime = Calendar.getInstance();
		
		//新建客户后 从数据库中读取滞留的信息并发送。
		OutputStream out;
		try {
			out = socket.getOutputStream();
			DataOutputStream outs = new DataOutputStream(out);
			ConnectDB conn = ConnectDB.getInstance();
			ChatMessage[] msgs = conn.getDelayMessage(id);
			
			for(int i=0;i<msgs.length;i++)
			{
				byte[] b = msgs[i].getMessage().getBytes("UTF-8");
				int totalLen = 1 + 4 + b.length;
				outs.writeByte(MessageType.SERVER_SEND_MESSAGE);
				outs.writeInt(totalLen);
				outs.write(b);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @return the clientThread
	 */
	public ClientSocketThread getClientThread() {
		return clientThread;
	}

	/**
	 * @param clientThread the clientThread to set
	 */
	public void setClientThread(ClientSocketThread clientThread) {
		this.clientThread = clientThread;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the lastTime
	 */
	public Calendar getLastTime() {
		return lastTime;
	}

	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(Calendar lastTime) {
		this.lastTime = lastTime;
	}
		
}
