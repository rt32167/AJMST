package com.ajmst.android.ui.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ajmst.android.R;
import com.ajmst.android.barcode.client.CaptureActivity;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.service.SpkfkService;
import com.ajmst.android.ui.NumberInputActivity;
import com.ajmst.android.webservice.WsSpkfkService;
import com.ajmst.android.webservice.WsResponse;
import com.ajmst.common.exception.ExceptionUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChinesePriceActivity extends Activity {
	Context context;
	SpkfkService spService;
	List<AdvSpkfk> spkfks;
/*	private static final int MSG_TYPE_GET_CHINESE_SPKFK = 1;
	private static final int  MSG_TYPE_CHANGE_PRICE = 2;*/
/*	// webservice调用后的Handler
	@SuppressLint("HandlerLeak")
	final Handler wsMsgHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			//EditText editTextInfo = (EditText) findViewById(R.id.editTextInfo);
			String info = null;
			int msgType = 0;
			try {
				WsResponse response = (WsResponse) msg.obj;
				msgType = msg.arg1;
				
				if (response.isOk()) {
					info = "调用成功,正在处理...";
					Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
					switch(msgType){
						case MSG_TYPE_GET_CHINESE_SPKFK:
							ChinesePriceActivity.this.spkfks = (List<Spkfk>)response.getResult();
							displaySpkfk(ChinesePriceActivity.this.spkfks);
							info = "处理完成";
							break;
						case MSG_TYPE_CHANGE_PRICE:
							int focusIdx =  msg.arg2;
							displaySpkfk(ChinesePriceActivity.this.spkfks,focusIdx);
							info = "处理完成";
							break;
						default:
							info = "未知动作";
							break;
					}
				}else{
					info = ExceptionUtil.getStackTrace(response.getException());
				}
				//editTextInfo.setText(info);
			} catch (Exception e) {
				info = "处理调用结果时发生异常:"+ ExceptionUtil.getStackTrace(e);
				//editTextInfo.setText("处理调用结果时发生异常:"+ ExceptionUtil.getStackTrace(e));
			}
			//若为查询商品价格,显示刷新按钮,并隐藏进度条
			if(msgType == MSG_TYPE_GET_CHINESE_SPKFK){
				Button buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
				buttonRefresh.setVisibility(View.VISIBLE);
				ProgressBar progressBarGetSpkfk = (ProgressBar)findViewById(R.id.progressBarGetSpkfk);
				progressBarGetSpkfk.setVisibility(View.GONE);
			}

			Toast.makeText(context, info, Toast.LENGTH_LONG).show();
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chinese_price);
		
		this.spService = new SpkfkService(this);
		this.spkfks = this.spService.getSelfCnSp();
		this.displaySpkfk(this.spkfks);
		Toast.makeText(this, "中药共 " + this.spkfks.size() + " 个", Toast.LENGTH_LONG).show();
		// 刷新按钮
		Button buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
		buttonRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				spkfks = spService.getSelfCnSp();
				ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
				int focusIdx = listViewSpkfk.getFirstVisiblePosition();
				displaySpkfk(spkfks, focusIdx);
			}
		});

		//查询输入文本框
		EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				List<AdvSpkfk> spkfksToDispaly = null;
				System.out.println("CharSequence:" + s + ",start:" + ",before:" + count +"," + count);
				System.out.println("spkfks.size:" + spkfks.size());
				spkfksToDispaly = new ArrayList<AdvSpkfk>();
				if(spkfks != null){
					for(int i = 0; i < spkfks.size(); i++){
						AdvSpkfk spkfk = spkfks.get(i);
						String zjm = spkfk.getZjm();
						if(zjm.contains(s.toString().toUpperCase())){
							spkfksToDispaly.add(spkfk);
						}
					}
				}
				displaySpkfk(spkfksToDispaly);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		// 设置商品价格列表点击可弹出数字输入窗口
		ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		listViewSpkfk.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(context, NumberInputActivity.class);
				EditText editTextPrice = (EditText) v
						.findViewById(R.id.editTextPrice);
				String number = editTextPrice.getText().toString();
				intent.putExtra(NumberInputActivity.NUMBER, number);
				intent.putExtra(NumberInputActivity.TAG, "" + position);
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//设置最多只能输入两位小数,因为平安数据库中只存2位
				startActivityForResult(intent, 0);
			}
		});
		
		//设置扫码启动按钮
		Button buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
/*				//启动条码扫描软件BarcodeScanner
				IntentIntegrator integrator = new IntentIntegrator(ChinesePriceActivity.this);//指定当前的activity
				//integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES); //启动扫描二维码
				integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES); //启动扫描所有类型的码,如条形码和二维码
*/			
				 Intent intent = new Intent(ChinesePriceActivity.this, CaptureActivity.class);
				 startActivity(intent);
				 
			}
		});
	}

	// 启动其他activity后接收返回结果的方法,目前处理了弹出数字输入窗口的返回结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NumberInputActivity.RESULT_CODE_GET_INPUT) {
			final Intent intent = data;
			if (intent != null) {
				String number = intent.getStringExtra(NumberInputActivity.NUMBER);
				int position = Integer.valueOf(intent
						.getStringExtra(NumberInputActivity.TAG));
				if (number != null && "".equals(number) == false) {
					ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
					BigDecimal lshj = new BigDecimal(number);
					AdvSpkfk sp = (AdvSpkfk) listViewSpkfk
							.getItemAtPosition(position);
					sp.setLshj(lshj);
					this.spService.saveOrUpdate(sp);
					this.displaySpkfk(spkfks, listViewSpkfk.getFirstVisiblePosition());
				}
			}
/*			new Thread() {
				@Override
				public void run() {
					if (intent != null) {
						String number = intent.getStringExtra(NumberInputActivity.NUMBER);
						int position = Integer.valueOf(intent
								.getStringExtra(NumberInputActivity.TAG));
						if (number != null && "".equals(number) == false) {
							ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
							BigDecimal lshj = new BigDecimal(number);
							Spkfk spkfk = (Spkfk) listViewSpkfk
									.getItemAtPosition(position);
							WsResponse r = WsSpkfkService.changePrice(
									spkfk.getSpid(), lshj);
							if(r.isOk()){
								spkfk.setLshj(lshj);
							}
							Message msg = new Message();
							msg.obj = r;
							msg.arg1 = MSG_TYPE_CHANGE_PRICE;
							msg.arg2 = position;
							wsMsgHandler.sendMessage(msg);
						}
					}
				}
			}.start();*/
			/*
			 * WsResponse r = SpkfkService.changePrice(spkfk.getSpid().trim(),
			 * lshj); if(r.isOk()){ spkfk.setLshj(lshj); BaseAdapter adapter =
			 * (BaseAdapter)listViewSpkfk.getAdapter();
			 * adapter.notifyDataSetChanged(); }else{
			 * 
			 * }
			 */
		}else{
		//接收条码扫描软件BarcodeScanner的扫码结果
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
			if (result != null) {
				String contents = result.getContents();
				if (contents != null) {
					Toast.makeText(this, "成功:" + contents, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chinese_price, menu);
		return true;
	}

	public void displaySpkfk(List<AdvSpkfk> spkfks){
		displaySpkfk(spkfks,0);
	}
	
	public void displaySpkfk(List<AdvSpkfk> spkfks,int focusIdx) {
		ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		SpkfkPriceListAdaper adapter = new SpkfkPriceListAdaper(context, spkfks);
		listViewSpkfk.setAdapter(adapter);
		listViewSpkfk.setSelection(focusIdx);
	}

}
