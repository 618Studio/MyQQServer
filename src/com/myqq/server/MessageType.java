package com.myqq.server;

public class MessageType {
	/*
	 * 用于心跳机制，确保登录状态。客户端发送至服务器。
	 * 用于心跳机制，确保登录状态。服务器回应客户端确认登录。
	 */
	static final byte CLIENT_SEND_HEART_BEAT = 1;
	static final byte SERVER_RESPONSE_HEART_BEAT = 2;
	
	
	/*
	 * 用于发送消息，客户端发送消息至服务器。
	 * 用于发送消息，服务器响应客户端确认收到。
	 */
	static final byte CLIENT_SEND_MESSAGE = 3;
	static final byte SERVER_RECV_MESSAGE = 4;
	
	
	/*
	 * 用于接收消息,客户端回应服务器确认接收。
	 * 用于接收消息,服务器发送至客户端。
	 */
	static final byte CLIENT_RECV_MESSAGE = 5;
	static final byte SERVER_SEND_MESSAGE = 6;
	
	/*
	 * 客户请求好友列表
	 * 服务器返回好友列表
	 */
	static final byte CLIENT_REQUEST_FRIENDS = 7;
	static final byte SERVER_RESPONSE_FRIENDS = 8;
	
	/*
	 * 客户请求在线好友列表
	 * 服务器返回在线好友列表
	 */
	static final byte CLIENT_REQUEST_ONLINE = 9;
	static final byte SERVER_RESPONSE_ONLINE = 10;
	
	/*
	 *	客户请求登录
	 *	服务器返回登录情况
	 */
	static final byte CLIENT_REQUSET_LOGIN = 11;
	static final byte SERVER_RESPONSE_LOGIN = 12;
	
 }
