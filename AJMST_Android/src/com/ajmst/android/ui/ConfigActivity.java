package com.ajmst.android.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ajmst.android.R;
import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.commmon.entity.Spkfk;
import com.ajmst.common.response.Response;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.service.MaintainService;
import com.ajmst.android.service.SpkfkService;
import com.ajmst.android.util.DateTimeUtils;
import com.ajmst.android.webservice.WsGhService;
import com.ajmst.android.webservice.WsMaintainService;
import com.ajmst.android.webservice.WsResponse;
import com.ajmst.android.webservice.WsSpkfkService;

@SuppressLint("HandlerLeak")
public class ConfigActivity extends Activity {
	private MaintainService maintainService;
	private SpkfkService spkfkService;
	private static final int REQUEST_CODE_CHOSE_FILE = 0;
	private SharedPreferences preferencesOfMaintain;
	private static final int MSG_TYPE_GET_MAINTAIN = 1;
	private static final int MSG_TYPE_UPLOAD_MAINTAIN = 2;
	private static final int MSG_TYPE_GET_SPKFK = 3;
	private static final String LOG_TAG = ConfigActivity.class.getSimpleName();
	//��ȡ��Ʒ������ز���
	private static final int MAX_COUNT_GET_SPKFK = 200;//ÿ�λ�ȡ��Ʒ��������
	private static final int MAX_TRY_COUNT_GET_SPKFK = 3;	//ÿ���ֻ�ȡ��Ʒ�������Դ���
	private static final int GET_SPKFK_RETRY_GAP_SECONDS = 10;//��ȡ��Ʒ����ʧ�ܺ�ȴ�����
	//��ȡλ��������ز���
	private static final int MAX_COUNT_GET_AJMST_GH = 200;//ÿ�λ�ȡ��Ʒ��������
	private static final int MAX_TRY_COUNT_GET_AJMST_GH = 10;	//ÿ���ֻ�ȡ��Ʒ�������Դ���
	private static final int GET_AJMST_GH_RETRY_GAP_SECONDS = 10;//��ȡ��Ʒ����ʧ�ܺ�ȴ�����
	//����������Ϣ����Ϣhandler
	Handler progressMsgHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			ProgressBar prb = (ProgressBar)findViewById(R.id.prbRate);
			EditText etPrbInfo = (EditText)findViewById(R.id.etPrbInfo);

			String info = msg.obj.toString();
			int rate = msg.arg1;
			prb.setProgress(rate);
			etPrbInfo.setText(info);
		}
	};
	
	//����̨��Ϣhandler
	Handler consoleMsgHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			EditText etConsole = (EditText)findViewById(R.id.etConsole);
			String info = etConsole.getText().toString();
			if(msg.arg1 == 0){
				info = msg.obj.toString();
			}else{
				info = info + "\n" + msg.obj.toString();
			}
			etConsole.setText(info);
		}
	};
	
	//���͸��½���������Ϣ
	private void showProgress(String info, int rate){
		Message msg = new Message();
		msg.obj = info;
		msg.arg1 = rate;
		progressMsgHandler.sendMessage(msg);
		Log.i(LOG_TAG, info);
	}
	
	//����̨��ӡ��Ϣ(�ۼ�)
	private void showConsole(String info){
		showConsole(info,1);
	}
	
	/**
	 * ����̨��ӡ��Ϣ
	 * @param 
	 * @param int 1�ۼ� ,0���ۼ�
	 * */
	private void showConsole(String info,int addUp){
		Message msg = new Message();
		msg.obj = info;
		msg.arg1 = addUp;
		consoleMsgHandler.sendMessage(msg);
	}
	
	// webservice���ú��Handler
	final Handler wsMsgHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			
			WsResponse r = (WsResponse) msg.obj;
			int msgType = msg.arg1;
			if(msgType == MSG_TYPE_GET_MAINTAIN){
				if(r.isOk()){
					List<com.ajmst.commmon.entity.AjmstMaintain> maintains = (List<com.ajmst.commmon.entity.AjmstMaintain>)r.getResult();
					Toast.makeText(ConfigActivity.this, "��ȡ��������ɹ�,�� " + maintains.size() + " ��" , Toast.LENGTH_LONG).show();
					Toast.makeText(ConfigActivity.this, "׼������������������..." , Toast.LENGTH_LONG).show();
					MaintainService maintainService = new MaintainService(ConfigActivity.this);
					Toast.makeText(ConfigActivity.this, "ɾ�����ؾ�����..." , Toast.LENGTH_LONG).show();
					int delCount = maintainService.clearData();
					Toast.makeText(ConfigActivity.this, "ɾ�����ؾ�����,�� "+ delCount +" ��" , Toast.LENGTH_LONG).show();
					Toast.makeText(ConfigActivity.this, "��ʼ����������������..." , Toast.LENGTH_LONG).show();
					int failCount = 0;
					for(int i = 0; i < maintains.size(); i++){
						boolean succeed = maintainService.create(maintains.get(i));
						if(succeed != true){
							failCount++;
						}
					}
					if(failCount > 0){
						Toast.makeText(ConfigActivity.this, "������������ɹ� " + (maintains.size() - failCount) + " ��,ʧ�� " + failCount + " ��", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ConfigActivity.this, "������������ȫ�������ɹ� ,��" + maintains.size() + " ��", Toast.LENGTH_LONG).show();
					}
					Editor editor = preferencesOfMaintain.edit();
					editor.putString("lastImportTime", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
					editor.putInt("lastInx", 0);
					editor.putString("lastGH", "ȫ��");
					editor.commit();
				}else{
					Toast.makeText(ConfigActivity.this, "��ȡ��������ʧ��:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
				}
			}else if(msgType == MSG_TYPE_UPLOAD_MAINTAIN){
				if(r.isOk()){
					Toast.makeText(ConfigActivity.this, "�ϴ��������ϳɹ� ", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(ConfigActivity.this, "�ϴ���������ʧ��:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
				}
			}else if(msgType == MSG_TYPE_GET_SPKFK){
//				if(r.isOk()){
//					showProgress("���óɹ�", 10);
//					List<Spkfk> sps = (List<Spkfk>)r.getResult();
//					SpkfkService spService = new SpkfkService();
//					showProgress("��ʼ����", 15);
//					spService.save(sps,true);
//					showProgress("����ɹ�", 100);
//					//Toast.makeText(ConfigActivity.this, "������Ʒ���ϳɹ� ", Toast.LENGTH_LONG).show();
//				}else{
//					showProgress("������Ʒ����ʧ��:" + r.getException().getMessage(), 15);
//					//Toast.makeText(ConfigActivity.this, "������Ʒ����ʧ��:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
//				}
			}
		}
	};
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		maintainService = new MaintainService(this);
		spkfkService = new SpkfkService(this);
		String preferencesName = this.getString(R.string.preferences_of_maintain);
		preferencesOfMaintain = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		
		//�ֹ�����������ť
		Button buttonImportMaintain = (Button) findViewById(R.id.buttonImportMaintain);
		buttonImportMaintain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);

				try {
					Intent fileChoser = Intent.createChooser(intent,
							"��ѡ��Excel�ļ�");
					/**
					 * �ص�����onActivityResult
					 */
					startActivityForResult(fileChoser, REQUEST_CODE_CHOSE_FILE);
				} catch (android.content.ActivityNotFoundException ex) {
					// Potentially direct the user to the Market with a Dialog
					Toast.makeText(ConfigActivity.this,
							"���Ȱ�װ�ļ�������",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
/*		//��������excel��ť
		Button buttonExportMaintain = (Button) findViewById(R.id.buttonExportMaintain);
		buttonExportMaintain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String expPath = preferencesOfMaintain.getString("lastPath", null);
				Toast.makeText(ConfigActivity.this, "���ڵ�����:" + expPath, Toast.LENGTH_LONG).show();
				Log.i(ConfigActivity.this.getClass().getName(), "���ڵ�����:" + expPath);
				try {
					maintainService.exportDataToExcel(expPath);
					Toast.makeText(ConfigActivity.this, "�����ɹ�", Toast.LENGTH_LONG).show();
					Log.i(ConfigActivity.this.getClass().getName(), "�������ݵ��ļ�(" + expPath +")�ɹ�");
				} catch (Exception e) {
					Toast.makeText(ConfigActivity.this, "����ʧ��:" + e.getMessage(), Toast.LENGTH_LONG).show();
					Log.e(ConfigActivity.this.getClass().getName(), "�������ݵ��ļ�(" + expPath +")ʧ��:" + e.getMessage());
				}
		    }
		});*/
		
/*		//�������ݿ��ļ���ť
		Button buttonExportDBFile = (Button) findViewById(R.id.buttonExportDBFile);
		buttonExportDBFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					//����Ƿ�ȫ���������
					List<AjmstMaintain> noQuantityItems = maintainService.getNoQuantityItems();
					if(noQuantityItems.size() > 0){
						Toast.makeText(ConfigActivity.this, "ע��!���� " + noQuantityItems.size() + " ��δ��������Ŀ!", Toast.LENGTH_LONG).show();
						//throw(new Exception("���� " + noQuantityItems.size() + " ��δ��������Ŀ"));
					}
					//�������ݿ��ļ�,�ļ�Ŀ¼Ϊexcel���ڵ�Ŀ¼
					String excelPath = preferencesOfMaintain.getString("lastPath", null);
					String targetPath = new File(excelPath).getParent() + "/ajmst.db";
					Toast.makeText(ConfigActivity.this, "���ڵ������ݿ��ļ���:" + targetPath, Toast.LENGTH_LONG).show();
					maintainService.exportDBFile(targetPath);
					Toast.makeText(ConfigActivity.this, "�����ɹ�", Toast.LENGTH_LONG).show();
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(ConfigActivity.this, "����ʧ��,�����쳣:" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
		    }
		});*/
		//�ӷ�������ȡ������Ŀ��ť
		Button buttonGetMaintainFromServer = (Button) findViewById(R.id.buttonGetMaintainFromServer);
		buttonGetMaintainFromServer.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "���ڴӷ�������������,���Ժ�", Toast.LENGTH_SHORT).show();
						new Thread() {
							@Override
							public void run() {
								Message msg = new Message();
								msg.obj = WsMaintainService.getMaintain(new Date());
								msg.arg1 = MSG_TYPE_GET_MAINTAIN;
								wsMsgHandler.sendMessage(msg);
							}
						}.start();
					}
				}
		);
		
		//���ϴ�������Ŀ����������ť
		Button buttonUploadMaintainToServer = (Button) findViewById(R.id.buttonUploadMaintainToServer);
		buttonUploadMaintainToServer.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "�����ϴ��������ϵ�������,���Ժ�", Toast.LENGTH_SHORT).show();
						new Thread() {
							@Override
							public void run() {
								Message msg = new Message();
								MaintainService maintainService = new MaintainService(ConfigActivity.this);
								List<AjmstMaintain> maintains = maintainService.getMaintainItems();
								WsResponse r = null;
								if(maintains.size() <= 0){
									r = new WsResponse();
									r.setIsOk(false);
									r.setException(new Exception("��������Ҫ�ϴ�"));
								}else{
									r = WsMaintainService.uploadMaintain(maintains);
								}
								msg.obj = r;
								msg.arg1 = MSG_TYPE_UPLOAD_MAINTAIN;
								wsMsgHandler.sendMessage(msg);
							}
						}.start();
					}
				}
		);
		
		
		//�ӷ�����������Ʒ���ϰ�ť
		Button btnGetSpkfk = (Button) findViewById(R.id.btnGetSpkfk);
		btnGetSpkfk.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "����������Ʒ����,���Ժ�", Toast.LENGTH_SHORT).show();
						new Thread() {
							@Override
							public void run() {
								int sIdx = 0;
								int rate = 0;
								int partCount = 1;
								int total = 0;
								SpkfkService spService = new SpkfkService(ConfigActivity.this);
								String partInfo;
								int reTryTime = 0;
								while(true){
									partInfo = "�� " + partCount + " ������Ʒ����";
									showProgress("���ڻ�ȡ" + partInfo + "...", rate);
									
									WsResponse r = WsSpkfkService.getSpkfks(sIdx,MAX_COUNT_GET_SPKFK);
									if(r.isOk()){
										reTryTime = 0;
										sIdx += MAX_COUNT_GET_SPKFK;
										showProgress("��ȡ" +partInfo+ "�ɹ�", ++rate);
										List<Spkfk> sps = (List<Spkfk>)r.getResult();
										showProgress("��ʼ����" +partInfo + "...", ++rate);
										List<AdvSpkfk> advSps;
										try {
											advSps = spService.toAdvSpkfk(sps);
										} catch (Exception e) {
											e.printStackTrace();
											showProgress("�ڲ���������ת��ʧ��", ++rate);
											break;
										}
										Response rSave = spService.saveOrUpdate(advSps);
										if(rSave.isOk() == false){
											showProgress("����" + partInfo +"ʧ��,ֹͣ����,ԭ��:" + r.getException().getMessage() ,rate);
											break;
										}
										showProgress("����"+ partInfo +"�ɹ�", ++rate);
										total += sps.size();
										if(sps.size() < MAX_COUNT_GET_SPKFK){
											showProgress("��Ʒ����ȫ�����سɹ�,�� " + total + " ��", ++rate);
											break;
										}
										partCount++;
									}else{
										showProgress("��ȡ" + partInfo + "ʧ��:" + r.getException().getMessage(), rate);
										if(reTryTime >= MAX_TRY_COUNT_GET_SPKFK){
											showProgress(reTryTime + "�����Ի�ȡ" + partInfo + "ʧ��,����ֹ��������:" + r.getException().getMessage(), rate);
											break;
										}else{
											try {
												reTryTime++;
												showProgress(GET_SPKFK_RETRY_GAP_SECONDS + " ���ʼ��" + reTryTime + "�����Ի�ȡ" + partInfo, rate);
												Thread.sleep(GET_SPKFK_RETRY_GAP_SECONDS * 1000);
												showProgress("��ʼ�� " + reTryTime + " �����Ի�ȡ" + partInfo, rate);
											} catch (InterruptedException e) {
											}
										}
									}
								}
							}
						}.start();
					}
				}
		);
		
		//�ӷ�����������Ʒ���ϰ�ť(���߳�)
		Button btnGetSpkfkMultThread = (Button) findViewById(R.id.btnGetSpkfkMultThread);
		btnGetSpkfkMultThread.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						
						new Thread() {
							@Override
							public void run() {
								WsResponse r = WsSpkfkService.getSpkfkSizeOfActive();
								if(r.isOk()){
									final Integer maxSize = (Integer)r.getResult();
									int partSize = 600;
									int currPos = 0;
									int delaySecPerThread = 3;//ÿ���߳��ӳ�����,���һ���ӳ�0��,�ڶ���5��,������10
									final List<DownloadThread> threads = new ArrayList<DownloadThread>();
									while(currPos < maxSize){
										int delaySec = delaySecPerThread * threads.size();
										DownloadThread thread = new DownloadThread(ConfigActivity.this,currPos,partSize,delaySec);
										thread.start();
										threads.add(thread);
										currPos = currPos + partSize;
									}
									String info = "�ܹ� "+ maxSize + " ��,���� " + threads.size() + " ���߳�����,ÿ���߳����� "+ partSize + " ��";
									showProgress(info, 0);
									showConsole(info);
									
									// ����ÿ����Ȼ�ȡһ��ϵͳ����ɽ���  
					                final Timer timer = new Timer();
					                final Date startTime = new Date();
					                timer.schedule(new TimerTask() { 
					                    public void run() {
					                        // ��ȡ�����������ɱ���  
					                         int finishSize = 0;
					                         int downLoadSize = 0;
					                         boolean isAllThreadStop = true;
					                         for(int i = 0; i < threads.size();i++){
					                        	 Thread thread = threads.get(i);
					                        	 if(thread.isAlive()){
					                        		 isAllThreadStop = false;
					                        	 }
					                        	 finishSize = finishSize + threads.get(i).getFinishSize();
					                        	 downLoadSize = downLoadSize + threads.get(i).getDownLoadSize();
					                         }
					                        // ������Ϣ֪ͨ������½����� 
					                         String info = "���� :������ " + downLoadSize + " ,�ѱ��� " + finishSize + " ,���� " + maxSize + " ,��ʱ " + DateTimeUtils.getSeconds(startTime, new Date()) + " ��";
					                         showProgress(info, finishSize/maxSize);
					                         
					                        // ������ɺ�ȡ���������  
					                        if (isAllThreadStop) {
					                            timer.cancel();
					                        }
					                    }
					                }, 0, 5000);
								}else{
									String info = "��ȡ�ܸ���ʧ��,�޷���������,��������";
									showProgress(info, 0);
									showConsole(info);
								}
							}
						}.start();
					}
				}
		);
		
		
		
		//�ӷ���������λ������
		Button btnGetGh = (Button) findViewById(R.id.btnGetGh);
		btnGetGh.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "��������λ������,���Ժ�", Toast.LENGTH_SHORT).show();
						new Thread() {
							@Override
							public void run() {
								int sIdx = 0;
								int rate = 0;
								int partCount = 1;
								int total = 0;
								//AjmstGhService ghService = new AjmstGhService(ConfigActivity.this);
								boolean firstTime = true;
								String partInfo;
								int reTryTime = 0;
								while(true){
									partInfo = "�� " + partCount + " ����λ������";
									showProgress("���ڻ�ȡ" + partInfo + "...", rate);
									
									WsResponse r = WsGhService.getGhs(sIdx,MAX_COUNT_GET_AJMST_GH);
									if(r.isOk()){
										reTryTime = 0;
										sIdx += MAX_COUNT_GET_AJMST_GH;
										showProgress("��ȡ" +partInfo+ "�ɹ�", ++rate);
										List<AjmstGh> ghs = (List<AjmstGh>)r.getResult();
										showProgress("��ʼ����" +partInfo + "...", ++rate);
										Response rSave = spkfkService.updateCabinet(ghs);
										if(rSave.isOk() == false){
											showProgress("����" + partInfo +"ʧ��,ֹͣ����,ԭ��:" + rSave.getException().getMessage() ,rate);
											break;
										}
										showProgress("����"+ partInfo +"�ɹ�", ++rate);
										total += ghs.size();
										if(ghs.size() < MAX_COUNT_GET_AJMST_GH){
											showProgress("λ������ȫ�����سɹ�,�� " + total + " ��", 100);
											break;
										}
										partCount++;
									}else{
										showProgress("��ȡ" + partInfo + "ʧ��:" + r.getException().getMessage(), rate);
										if(reTryTime >= MAX_TRY_COUNT_GET_AJMST_GH){
											showProgress(reTryTime + "�����Ի�ȡ" + partInfo + "ʧ��,����ֹ��������:" + r.getException().getMessage(), rate);
											break;
										}else{
											try {
												reTryTime++;
												showProgress(GET_AJMST_GH_RETRY_GAP_SECONDS + " ���ʼ��" + reTryTime + "�����Ի�ȡ" + partInfo, rate);
												Thread.sleep(GET_AJMST_GH_RETRY_GAP_SECONDS * 1000);
												showProgress("��ʼ�� " + reTryTime + " �����Ի�ȡ" + partInfo, rate);
											} catch (InterruptedException e) {
											}
										}
									}
								}
							}
						}.start();
					}
				}
		);
		
		
		
		Button buttonTest = (Button) findViewById(R.id.buttonTest);
		buttonTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
/*				AjmstGhService ghService = new AjmstGhService(ConfigActivity.this);
				AjmstGh ajmstGh = new AjmstGh();
				ajmstGh.setGh("1");
				ajmstGh.setSpbh("000809");
				Response r = ghService.saveOrUpdate(ghService.getAdvAjmstGh(ajmstGh));
				System.out.println(r);
				AjmstGh gh = ghService.getAjmstGh("000809", "1");
				System.out.println(gh);*/
			}
		});
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_CHOSE_FILE) {
				// Get the Uri of the selected file
				Toast.makeText(this, "call back from file choser",
						Toast.LENGTH_SHORT).show();
				String path = data.getData().getPath();
				if(path != null && "".equals(path) == false){
					Toast.makeText(ConfigActivity.this, "���ڵ�������,���Ժ�", Toast.LENGTH_SHORT)
					.show();
					boolean result = maintainService.initData(path);
					if(result == true){
						Editor editor = preferencesOfMaintain.edit();
						editor.putString("lastPath", path);
						editor.putString("lastImportTime", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
						editor.putInt("lastInx", 0);
						editor.putString("lastGH", "ȫ��");
						editor.commit();
						Toast.makeText(ConfigActivity.this, "����ɹ�", Toast.LENGTH_SHORT)
						.show();
						Log.i(ConfigActivity.this.getClass().getName(), "�����ļ�(" + path +")�����ݿ�ɹ�");
					}else{
						Toast.makeText(ConfigActivity.this, "��������ʧ��,�����ļ��Ƿ���ȷ", Toast.LENGTH_SHORT)
						.show();
						Log.e(ConfigActivity.this.getClass().getName(), "�����ļ�(" + path +")�����ݿ�ʧ��");
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}



}
