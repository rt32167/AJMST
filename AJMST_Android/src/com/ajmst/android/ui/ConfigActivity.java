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
	//获取商品资料相关参数
	private static final int MAX_COUNT_GET_SPKFK = 200;//每次获取商品资料数量
	private static final int MAX_TRY_COUNT_GET_SPKFK = 3;	//每部分获取商品资料重试次数
	private static final int GET_SPKFK_RETRY_GAP_SECONDS = 10;//获取商品资料失败后等待秒数
	//获取位置资料相关参数
	private static final int MAX_COUNT_GET_AJMST_GH = 200;//每次获取商品资料数量
	private static final int MAX_TRY_COUNT_GET_AJMST_GH = 10;	//每部分获取商品资料重试次数
	private static final int GET_AJMST_GH_RETRY_GAP_SECONDS = 10;//获取商品资料失败后等待秒数
	//进度条和信息的消息handler
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
	
	//控制台消息handler
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
	
	//发送更新进度条的消息
	private void showProgress(String info, int rate){
		Message msg = new Message();
		msg.obj = info;
		msg.arg1 = rate;
		progressMsgHandler.sendMessage(msg);
		Log.i(LOG_TAG, info);
	}
	
	//控制台打印信息(累加)
	private void showConsole(String info){
		showConsole(info,1);
	}
	
	/**
	 * 控制台打印信息
	 * @param 
	 * @param int 1累加 ,0不累加
	 * */
	private void showConsole(String info,int addUp){
		Message msg = new Message();
		msg.obj = info;
		msg.arg1 = addUp;
		consoleMsgHandler.sendMessage(msg);
	}
	
	// webservice调用后的Handler
	final Handler wsMsgHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			
			WsResponse r = (WsResponse) msg.obj;
			int msgType = msg.arg1;
			if(msgType == MSG_TYPE_GET_MAINTAIN){
				if(r.isOk()){
					List<com.ajmst.commmon.entity.AjmstMaintain> maintains = (List<com.ajmst.commmon.entity.AjmstMaintain>)r.getResult();
					Toast.makeText(ConfigActivity.this, "获取养护检查表成功,共 " + maintains.size() + " 条" , Toast.LENGTH_LONG).show();
					Toast.makeText(ConfigActivity.this, "准备创建本地养护检查表..." , Toast.LENGTH_LONG).show();
					MaintainService maintainService = new MaintainService(ConfigActivity.this);
					Toast.makeText(ConfigActivity.this, "删除本地旧数据..." , Toast.LENGTH_LONG).show();
					int delCount = maintainService.clearData();
					Toast.makeText(ConfigActivity.this, "删除本地旧数据,共 "+ delCount +" 条" , Toast.LENGTH_LONG).show();
					Toast.makeText(ConfigActivity.this, "开始创建本地养护检查表..." , Toast.LENGTH_LONG).show();
					int failCount = 0;
					for(int i = 0; i < maintains.size(); i++){
						boolean succeed = maintainService.create(maintains.get(i));
						if(succeed != true){
							failCount++;
						}
					}
					if(failCount > 0){
						Toast.makeText(ConfigActivity.this, "本地养护检查表成功 " + (maintains.size() - failCount) + " 条,失败 " + failCount + " 条", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ConfigActivity.this, "本地养护检查表全部创建成功 ,共" + maintains.size() + " 条", Toast.LENGTH_LONG).show();
					}
					Editor editor = preferencesOfMaintain.edit();
					editor.putString("lastImportTime", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
					editor.putInt("lastInx", 0);
					editor.putString("lastGH", "全部");
					editor.commit();
				}else{
					Toast.makeText(ConfigActivity.this, "获取养护检查表失败:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
				}
			}else if(msgType == MSG_TYPE_UPLOAD_MAINTAIN){
				if(r.isOk()){
					Toast.makeText(ConfigActivity.this, "上传养护资料成功 ", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(ConfigActivity.this, "上传养护资料失败:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
				}
			}else if(msgType == MSG_TYPE_GET_SPKFK){
//				if(r.isOk()){
//					showProgress("调用成功", 10);
//					List<Spkfk> sps = (List<Spkfk>)r.getResult();
//					SpkfkService spService = new SpkfkService();
//					showProgress("开始保存", 15);
//					spService.save(sps,true);
//					showProgress("保存成功", 100);
//					//Toast.makeText(ConfigActivity.this, "下载商品资料成功 ", Toast.LENGTH_LONG).show();
//				}else{
//					showProgress("下载商品资料失败:" + r.getException().getMessage(), 15);
//					//Toast.makeText(ConfigActivity.this, "下载商品资料失败:" + r.getException().getMessage(), Toast.LENGTH_LONG).show();
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
		
		//手工导入养护按钮
		Button buttonImportMaintain = (Button) findViewById(R.id.buttonImportMaintain);
		buttonImportMaintain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);

				try {
					Intent fileChoser = Intent.createChooser(intent,
							"请选择Excel文件");
					/**
					 * 回调方法onActivityResult
					 */
					startActivityForResult(fileChoser, REQUEST_CODE_CHOSE_FILE);
				} catch (android.content.ActivityNotFoundException ex) {
					// Potentially direct the user to the Market with a Dialog
					Toast.makeText(ConfigActivity.this,
							"请先安装文件管理器",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
/*		//导出养护excel按钮
		Button buttonExportMaintain = (Button) findViewById(R.id.buttonExportMaintain);
		buttonExportMaintain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String expPath = preferencesOfMaintain.getString("lastPath", null);
				Toast.makeText(ConfigActivity.this, "正在导出到:" + expPath, Toast.LENGTH_LONG).show();
				Log.i(ConfigActivity.this.getClass().getName(), "正在导出到:" + expPath);
				try {
					maintainService.exportDataToExcel(expPath);
					Toast.makeText(ConfigActivity.this, "导出成功", Toast.LENGTH_LONG).show();
					Log.i(ConfigActivity.this.getClass().getName(), "导出数据到文件(" + expPath +")成功");
				} catch (Exception e) {
					Toast.makeText(ConfigActivity.this, "导出失败:" + e.getMessage(), Toast.LENGTH_LONG).show();
					Log.e(ConfigActivity.this.getClass().getName(), "导出数据到文件(" + expPath +")失败:" + e.getMessage());
				}
		    }
		});*/
		
/*		//导出数据库文件按钮
		Button buttonExportDBFile = (Button) findViewById(R.id.buttonExportDBFile);
		buttonExportDBFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					//检查是否全部养护完成
					List<AjmstMaintain> noQuantityItems = maintainService.getNoQuantityItems();
					if(noQuantityItems.size() > 0){
						Toast.makeText(ConfigActivity.this, "注意!存在 " + noQuantityItems.size() + " 个未养护的条目!", Toast.LENGTH_LONG).show();
						//throw(new Exception("存在 " + noQuantityItems.size() + " 个未养护的条目"));
					}
					//导出数据库文件,文件目录为excel所在的目录
					String excelPath = preferencesOfMaintain.getString("lastPath", null);
					String targetPath = new File(excelPath).getParent() + "/ajmst.db";
					Toast.makeText(ConfigActivity.this, "正在导出数据库文件到:" + targetPath, Toast.LENGTH_LONG).show();
					maintainService.exportDBFile(targetPath);
					Toast.makeText(ConfigActivity.this, "导出成功", Toast.LENGTH_LONG).show();
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(ConfigActivity.this, "导出失败,发生异常:" + e.getMessage(), Toast.LENGTH_LONG).show();
				}
		    }
		});*/
		//从服务器获取养护条目按钮
		Button buttonGetMaintainFromServer = (Button) findViewById(R.id.buttonGetMaintainFromServer);
		buttonGetMaintainFromServer.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "正在从服务器下载数据,请稍后", Toast.LENGTH_SHORT).show();
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
		
		//从上传养护条目到服务器按钮
		Button buttonUploadMaintainToServer = (Button) findViewById(R.id.buttonUploadMaintainToServer);
		buttonUploadMaintainToServer.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "正在上传养护资料到服务器,请稍后", Toast.LENGTH_SHORT).show();
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
									r.setException(new Exception("无数据需要上传"));
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
		
		
		//从服务器下载商品资料按钮
		Button btnGetSpkfk = (Button) findViewById(R.id.btnGetSpkfk);
		btnGetSpkfk.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "正在下载商品资料,请稍后", Toast.LENGTH_SHORT).show();
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
									partInfo = "第 " + partCount + " 部分商品资料";
									showProgress("正在获取" + partInfo + "...", rate);
									
									WsResponse r = WsSpkfkService.getSpkfks(sIdx,MAX_COUNT_GET_SPKFK);
									if(r.isOk()){
										reTryTime = 0;
										sIdx += MAX_COUNT_GET_SPKFK;
										showProgress("获取" +partInfo+ "成功", ++rate);
										List<Spkfk> sps = (List<Spkfk>)r.getResult();
										showProgress("开始保存" +partInfo + "...", ++rate);
										List<AdvSpkfk> advSps;
										try {
											advSps = spService.toAdvSpkfk(sps);
										} catch (Exception e) {
											e.printStackTrace();
											showProgress("内部父类子类转换失败", ++rate);
											break;
										}
										Response rSave = spService.saveOrUpdate(advSps);
										if(rSave.isOk() == false){
											showProgress("保存" + partInfo +"失败,停止下载,原因:" + r.getException().getMessage() ,rate);
											break;
										}
										showProgress("保存"+ partInfo +"成功", ++rate);
										total += sps.size();
										if(sps.size() < MAX_COUNT_GET_SPKFK){
											showProgress("商品资料全部下载成功,共 " + total + " 个", ++rate);
											break;
										}
										partCount++;
									}else{
										showProgress("获取" + partInfo + "失败:" + r.getException().getMessage(), rate);
										if(reTryTime >= MAX_TRY_COUNT_GET_SPKFK){
											showProgress(reTryTime + "次重试获取" + partInfo + "失败,已中止下载资料:" + r.getException().getMessage(), rate);
											break;
										}else{
											try {
												reTryTime++;
												showProgress(GET_SPKFK_RETRY_GAP_SECONDS + " 秒后开始第" + reTryTime + "次重试获取" + partInfo, rate);
												Thread.sleep(GET_SPKFK_RETRY_GAP_SECONDS * 1000);
												showProgress("开始第 " + reTryTime + " 次重试获取" + partInfo, rate);
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
		
		//从服务器下载商品资料按钮(多线程)
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
									int delaySecPerThread = 3;//每个线程延迟秒数,如第一个延迟0秒,第二个5秒,第三个10
									final List<DownloadThread> threads = new ArrayList<DownloadThread>();
									while(currPos < maxSize){
										int delaySec = delaySecPerThread * threads.size();
										DownloadThread thread = new DownloadThread(ConfigActivity.this,currPos,partSize,delaySec);
										thread.start();
										threads.add(thread);
										currPos = currPos + partSize;
									}
									String info = "总共 "+ maxSize + " 个,启动 " + threads.size() + " 个线程下载,每个线程下载 "+ partSize + " 个";
									showProgress(info, 0);
									showConsole(info);
									
									// 定义每秒调度获取一次系统的完成进度  
					                final Timer timer = new Timer();
					                final Date startTime = new Date();
					                timer.schedule(new TimerTask() { 
					                    public void run() {
					                        // 获取下载任务的完成比率  
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
					                        // 发送消息通知界面更新进度条 
					                         String info = "进度 :已下载 " + downLoadSize + " ,已保存 " + finishSize + " ,总数 " + maxSize + " ,耗时 " + DateTimeUtils.getSeconds(startTime, new Date()) + " 秒";
					                         showProgress(info, finishSize/maxSize);
					                         
					                        // 下载完成后取消任务调度  
					                        if (isAllThreadStop) {
					                            timer.cancel();
					                        }
					                    }
					                }, 0, 5000);
								}else{
									String info = "获取总个数失败,无法下载资料,请检查网络";
									showProgress(info, 0);
									showConsole(info);
								}
							}
						}.start();
					}
				}
		);
		
		
		
		//从服务器下载位置资料
		Button btnGetGh = (Button) findViewById(R.id.btnGetGh);
		btnGetGh.setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View v) {
						Toast.makeText(ConfigActivity.this, "正在下载位置资料,请稍后", Toast.LENGTH_SHORT).show();
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
									partInfo = "第 " + partCount + " 部分位置资料";
									showProgress("正在获取" + partInfo + "...", rate);
									
									WsResponse r = WsGhService.getGhs(sIdx,MAX_COUNT_GET_AJMST_GH);
									if(r.isOk()){
										reTryTime = 0;
										sIdx += MAX_COUNT_GET_AJMST_GH;
										showProgress("获取" +partInfo+ "成功", ++rate);
										List<AjmstGh> ghs = (List<AjmstGh>)r.getResult();
										showProgress("开始保存" +partInfo + "...", ++rate);
										Response rSave = spkfkService.updateCabinet(ghs);
										if(rSave.isOk() == false){
											showProgress("保存" + partInfo +"失败,停止下载,原因:" + rSave.getException().getMessage() ,rate);
											break;
										}
										showProgress("保存"+ partInfo +"成功", ++rate);
										total += ghs.size();
										if(ghs.size() < MAX_COUNT_GET_AJMST_GH){
											showProgress("位置资料全部下载成功,共 " + total + " 个", 100);
											break;
										}
										partCount++;
									}else{
										showProgress("获取" + partInfo + "失败:" + r.getException().getMessage(), rate);
										if(reTryTime >= MAX_TRY_COUNT_GET_AJMST_GH){
											showProgress(reTryTime + "次重试获取" + partInfo + "失败,已中止下载资料:" + r.getException().getMessage(), rate);
											break;
										}else{
											try {
												reTryTime++;
												showProgress(GET_AJMST_GH_RETRY_GAP_SECONDS + " 秒后开始第" + reTryTime + "次重试获取" + partInfo, rate);
												Thread.sleep(GET_AJMST_GH_RETRY_GAP_SECONDS * 1000);
												showProgress("开始第 " + reTryTime + " 次重试获取" + partInfo, rate);
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
					Toast.makeText(ConfigActivity.this, "正在导入数据,请稍后", Toast.LENGTH_SHORT)
					.show();
					boolean result = maintainService.initData(path);
					if(result == true){
						Editor editor = preferencesOfMaintain.edit();
						editor.putString("lastPath", path);
						editor.putString("lastImportTime", DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
						editor.putInt("lastInx", 0);
						editor.putString("lastGH", "全部");
						editor.commit();
						Toast.makeText(ConfigActivity.this, "导入成功", Toast.LENGTH_SHORT)
						.show();
						Log.i(ConfigActivity.this.getClass().getName(), "导入文件(" + path +")到数据库成功");
					}else{
						Toast.makeText(ConfigActivity.this, "导入数据失败,请检查文件是否正确", Toast.LENGTH_SHORT)
						.show();
						Log.e(ConfigActivity.this.getClass().getName(), "导入文件(" + path +")到数据库失败");
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
