package com.myqq.server;

import java.util.LinkedList;

public class DataCaching {

	//���߿ͻ�����
	static LinkedList<Client> onlineClient = new LinkedList<Client>();
	static LinkedList<ChatMessage> tempMessage = new LinkedList<ChatMessage>();
	
	static int onlineNr = 0;
}
