package com.myqq.server;

public class MessageType {
	/*
	 * �����������ƣ�ȷ����¼״̬���ͻ��˷�������������
	 * �����������ƣ�ȷ����¼״̬����������Ӧ�ͻ���ȷ�ϵ�¼��
	 */
	static final byte CLIENT_SEND_HEART_BEAT = 1;
	static final byte SERVER_RESPONSE_HEART_BEAT = 2;
	
	
	/*
	 * ���ڷ�����Ϣ���ͻ��˷�����Ϣ����������
	 * ���ڷ�����Ϣ����������Ӧ�ͻ���ȷ���յ���
	 */
	static final byte CLIENT_SEND_MESSAGE = 3;
	static final byte SERVER_RECV_MESSAGE = 4;
	
	
	/*
	 * ���ڽ�����Ϣ,�ͻ��˻�Ӧ������ȷ�Ͻ��ա�
	 * ���ڽ�����Ϣ,�������������ͻ��ˡ�
	 */
	static final byte CLIENT_RECV_MESSAGE = 5;
	static final byte SERVER_SEND_MESSAGE = 6;
	
	/*
	 * �ͻ���������б�
	 * ���������غ����б�
	 */
	static final byte CLIENT_REQUEST_FRIENDS = 7;
	static final byte SERVER_RESPONSE_FRIENDS = 8;
	
	/*
	 * �ͻ��������ߺ����б�
	 * �������������ߺ����б�
	 */
	static final byte CLIENT_REQUEST_ONLINE = 9;
	static final byte SERVER_RESPONSE_ONLINE = 10;
	
	/*
	 *	�ͻ������¼
	 *	���������ص�¼���
	 */
	static final byte CLIENT_REQUSET_LOGIN = 11;
	static final byte SERVER_RESPONSE_LOGIN = 12;
	
 }
