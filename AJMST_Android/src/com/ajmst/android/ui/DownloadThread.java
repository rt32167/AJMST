package com.ajmst.android.ui;

import java.util.List;

import android.content.Context;

import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.service.SpkfkService;
import com.ajmst.android.webservice.WsResponse;
import com.ajmst.android.webservice.WsSpkfkService;
import com.ajmst.commmon.entity.Spkfk;
import com.ajmst.common.response.Response;

public class DownloadThread extends Thread{
	//相关参数
	private static final int MAX_FETCH_SIZE_PER_TIME = 200;//每次最大获取数目
	private static final int MAX_TRY_COUNT = 10;	//最大获取重试次数
	private static final int RETRY_GAP_SECONDS = 5;//获取失败后等待秒数
	
	private int currPos;//当前下载位置
	private final int partSize;//负责下载的大小
	private int downLoadSize = 0;//已经下载的大小
	private int finishSize = 0;//已下载的大小
	private boolean isSuccess = false;
	private int reTryTime = 0;//失败重试次数
	//private String failReason;
	private final int delaySec;
	
	private SpkfkService spService;

	public DownloadThread(Context context,int startPos, int partSize,int delaySec) {
		this.currPos = startPos;
		this.partSize = partSize;
		this.spService = new SpkfkService(context);
		this.delaySec = delaySec;
	}

	public void run() {
		try {
			Thread.sleep(delaySec * 1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true){
			if(finishSize >= partSize){
				isSuccess = true;
				break;
			}
			int fetchSize = MAX_FETCH_SIZE_PER_TIME;
			if(partSize - finishSize < MAX_FETCH_SIZE_PER_TIME){
				fetchSize = partSize - finishSize;
			}
			WsResponse r = WsSpkfkService.getSpkfks(currPos,fetchSize);
			if(r.isOk()){
				reTryTime = 0;
				List<Spkfk> sps = (List<Spkfk>)r.getResult();
				downLoadSize += sps.size();
				List<AdvSpkfk> advSps;
				try {
					advSps = spService.toAdvSpkfk(sps);
				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
					break;
				}
				Response rSave = spService.saveOrUpdate(advSps);
				if(rSave.isOk() == false){
					isSuccess = false;
					break;
				}
				if(sps.size() == 0){
					isSuccess = true;
					break;
				}
				currPos = currPos + sps.size();
				finishSize += sps.size();
			}else{
				if(reTryTime >= MAX_TRY_COUNT){
					isSuccess = false;
					break;
				}else{
					try {
						reTryTime++;
						Thread.sleep(RETRY_GAP_SECONDS * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	public int getFinishSize() {
		return finishSize;
	}

	public void setFinishSize(int finishSize) {
		this.finishSize = finishSize;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getDownLoadSize() {
		return downLoadSize;
	}

	public void setDownLoadSize(int downLoadSize) {
		this.downLoadSize = downLoadSize;
	}
	
	
}
