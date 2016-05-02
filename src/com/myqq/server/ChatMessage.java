package com.myqq.server;

import java.util.Calendar;

public class ChatMessage {

	private String id = null;
	private String from = null;
	private String to = null;
	private String time = null;
	private String content = null;
	private Calendar calendar = null;
 
	public ChatMessage(String id, String from, String to,  String time,String content)
	{
		this.id = id;
		this.from = from;
		this.to = to;
		this.content = content;
		this.time = time;
		calendar = Calendar.getInstance();
	}
	
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	//返回报文格式
	public String getMessage()
	{
		String msg = null;
		msg = id +'\n'+ from +'\n'+ to +'\n'+ time +'\n'+ content;
		return msg;
	}
}
