package com.myqq.server;

import java.net.Socket;
import java.util.Calendar;

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
