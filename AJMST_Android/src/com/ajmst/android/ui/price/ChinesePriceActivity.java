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
/*	// webservice���ú��Handler
	@SuppressLint("HandlerLeak")
	final Handler wsMsgHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
		public void handleMessage(Message msg) {
			//EditText editTextInfo = (EditText) findViewById(R.id.editTextInfo);
			String info = null;
			int msgType = 0;
			try {
				WsResponse response = (WsResponse) msg.obj;
				msgType = msg.arg1;
				
				if (response.isOk()) {
					info = "���óɹ�,���ڴ���...";
					Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
					switch(msgType){
						case MSG_TYPE_GET_CHINESE_SPKFK:
							ChinesePriceActivity.this.spkfks = (List<Spkfk>)response.getResult();
							displaySpkfk(ChinesePriceActivity.this.spkfks);
							info = "�������";
							break;
						case MSG_TYPE_CHANGE_PRICE:
							int focusIdx =  msg.arg2;
							displaySpkfk(ChinesePriceActivity.this.spkfks,focusIdx);
							info = "�������";
							break;
						default:
							info = "δ֪����";
							break;
					}
				}else{
					info = ExceptionUtil.getStackTrace(response.getException());
				}
				//editTextInfo.setText(info);
			} catch (Exception e) {
				info = "������ý��ʱ�����쳣:"+ ExceptionUtil.getStackTrace(e);
				//editTextInfo.setText("������ý��ʱ�����쳣:"+ ExceptionUtil.getStackTrace(e));
			}
			//��Ϊ��ѯ��Ʒ�۸�,��ʾˢ�°�ť,�����ؽ�����
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
		Toast.makeText(this, "��ҩ�� " + this.spkfks.size() + " ��", Toast.LENGTH_LONG).show();
		// ˢ�°�ť
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

		//��ѯ�����ı���
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
		
		// ������Ʒ�۸��б����ɵ����������봰��
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
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//�������ֻ��������λС��,��Ϊƽ�����ݿ���ֻ��2λ
				startActivityForResult(intent, 0);
			}
		});
		
		//����ɨ��������ť
		Button buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
/*				//��������ɨ�����BarcodeScanner
				IntentIntegrator integrator = new IntentIntegrator(ChinesePriceActivity.this);//ָ����ǰ��activity
				//integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES); //����ɨ���ά��
				integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES); //����ɨ���������͵���,��������Ͷ�ά��
*/			
				 Intent intent = new Intent(ChinesePriceActivity.this, CaptureActivity.class);
				 startActivity(intent);
				 
			}
		});
	}

	// ��������activity����շ��ؽ���ķ���,Ŀǰ�����˵����������봰�ڵķ��ؽ��
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
		//��������ɨ�����BarcodeScanner��ɨ����
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
			if (result != null) {
				String contents = result.getContents();
				if (contents != null) {
					Toast.makeText(this, "�ɹ�:" + contents, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "ʧ��", Toast.LENGTH_LONG).show();
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
