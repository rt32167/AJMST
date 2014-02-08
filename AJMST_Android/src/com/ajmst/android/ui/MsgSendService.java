package com.ajmst.android.ui;

import com.ajmst.android.service.MsgQueueService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MsgSendService extends Service{

	private static final String TAG = MsgSendService.class.getSimpleName();
	private static final int sleepTime = 1000 * 10;//ÿ��10����һ����Ϣ����
	MsgQueueService msgService;
	Thread sendThread;
	boolean stop = false;
	@Override
	public void onCreate() {
		msgService = new MsgQueueService(MsgSendService.this.getApplicationContext());
		super.onCreate();
		sendThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						if(stop){
							break;
						}
						Log.d(TAG, "������Ϣ������������");
						msgService.sendMsgs();
						Thread.sleep(sleepTime);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		sendThread.start();
		Log.i(TAG, "������Ϣ����ʼ����");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stop = true;
		Log.i(TAG, "������Ϣ������ֹ");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
