package com.myqq.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

//�������ʱ��
public class HeartBeatCensor {

	//����������
	class CensorTask extends TimerTask
	{
		
		public CensorTask()
		{

		}
			
		//�ͻ��˷�����������
		public void run()
		{
			synchronized(DataCaching.onlineClient)
			{
				Calendar standard = Calendar.getInstance();
				int index = -1;
				for(Client client:DataCaching.onlineClient)
				{
					index++;
					long d = standard.getTimeInMillis()- client.getLastTime().getTimeInMillis();
						
						
					//�������20s���߳�
					if(d>20000)
					{
						//ֹͣ�߳�
						client.getClientThread().stop();
						try {
							client.getSocket().close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DataCaching.onlineClient.remove(index);
					}
				}
					
			}
				
		}
	}
	
	public HeartBeatCensor()
	{
		Timer heartTask = new Timer();
		heartTask.scheduleAtFixedRate(new CensorTask(),1000,10000);
		System.out.println("��ʼ���������");
	}
	
}
