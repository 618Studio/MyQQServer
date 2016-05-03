package com.myqq.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

//检查心跳时间
public class HeartBeatCensor {

	//心跳任务类
	class CensorTask extends TimerTask
	{
		
		public CensorTask()
		{

		}
			
		//客户端发送心跳报文
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
						
						
					//如果大于20s，踢出
					if(d>20000)
					{
						//停止线程
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
		System.out.println("开始检查心跳！");
	}
	
}
