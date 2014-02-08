package com.ajmst.android.ui.spkfk;

import java.math.BigDecimal;
import java.util.Date;

import com.ajmst.android.R;
import com.ajmst.android.R.layout;
import com.ajmst.android.R.menu;
import com.ajmst.android.barcode.client.CaptureActivity;
import com.ajmst.android.barcode.client.Intents;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.service.MsgQueueService;
import com.ajmst.android.service.SpkfkService;
import com.ajmst.android.ui.NumberInputActivity;
import com.ajmst.android.ui.price.ChinesePriceActivity;
import com.ajmst.common.response.Response;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SpkfkDetailActivity extends Activity {
	public static final String EXTRA_NAME_SPKFK = "Spkfk";
	public static final int RESULT_CODE_SUCCESS = 1;
	private static final int REQUEST_CODE_GET_LSHJ = 2;
	private AdvSpkfk sp;
	private SpkfkService spService;
	private MsgQueueService msgQueueService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_spkfk_detail);
		this.spService = new SpkfkService(SpkfkDetailActivity.this);
		this.msgQueueService = new MsgQueueService(SpkfkDetailActivity.this);
		sp = (AdvSpkfk) this.getIntent().getSerializableExtra(EXTRA_NAME_SPKFK);
		display();
		//返回按钮
		Button btnReturn = (Button) findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//修改柜号按钮
		Button btnCabinet = (Button) findViewById(R.id.btnCabinet);
		btnCabinet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "";
				cabinets[1] = "1柜";
				cabinets[2] = "2柜";
				cabinets[3] = "3柜";
				cabinets[4] = "4柜";
				cabinets[5] = "5柜";
				cabinets[6] = "6柜";
				cabinets[7] = "7柜";
				cabinets[8] = "8柜";
				cabinets[9] = "9柜";
				cabinets[10] = "10柜";
				cabinets[11] = "11柜";
				cabinets[12] = "12柜";
				cabinets[13] = "13柜";
				cabinets[14] = "14柜";
				cabinets[15] = "A柜";
				cabinets[16] = "B柜";
				cabinets[17] = "C柜";
				cabinets[18] = "D柜";
				cabinets[19] = "E柜";
				cabinets[20] = "F柜";
				cabinets[21] = "冰箱";
				cabinets[22] = "里面";
				cabinets[23] = "其他";
				
				new AlertDialog.Builder(SpkfkDetailActivity.this).setTitle(null)
				.setItems(cabinets, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String cabinet = cabinets[which].toString();
						cabinet = cabinet.replace("柜", "");
						if( "".equals(cabinet)){
							cabinet = null;
						}
						sp.setGh(cabinet);
						Response r = spService.saveOrUpdate(sp);
						if(r.isOk()){
							Toast.makeText(SpkfkDetailActivity.this, "修改柜号成功", Toast.LENGTH_LONG).show();
							notifyDataChanged();
						}else{
							Toast.makeText(SpkfkDetailActivity.this, "修改柜号失败", Toast.LENGTH_LONG)
							.show();
						}
						dialog.dismiss();
					}
				}).show();
			}
		});
		//修改零售价按钮
		Button btnLshj = (Button) findViewById(R.id.btnLshj);
		btnLshj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//弹出数字输入框
				Intent intent = new Intent(SpkfkDetailActivity.this, NumberInputActivity.class);
				String number = "" + sp.getLshj().doubleValue();
				intent.putExtra(NumberInputActivity.NUMBER, number);
				//intent.putExtra(NumberInputActivity.TAG, "" + positionTmp);
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//设置最多只能输入两位小数,因为平安数据库中只存2位
				startActivityForResult(intent, REQUEST_CODE_GET_LSHJ);
			}
			
		});
		//修改条码按钮
		Button btnSptm = (Button) findViewById(R.id.btnSptm);
		btnSptm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*				//启动条码扫描软件BarcodeScanner
				IntentIntegrator integrator = new IntentIntegrator(ChinesePriceActivity.this);//指定当前的activity
				//integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES); //启动扫描二维码
				integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES); //启动扫描所有类型的码,如条形码和二维码
				*/			
				Intent intent = new Intent(SpkfkDetailActivity.this, CaptureActivity.class);
				intent.setAction(Intents.Scan.ACTION);
				startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);//注意请求码一定要给IntentIntegrator.REQUEST_CODE
			}
			
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spkfk_detail, menu);
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		final Intent intent = data;
		//接收零售价输入框返回结果
		if (requestCode == REQUEST_CODE_GET_LSHJ) {
			if (intent != null) {
				String number = intent.getStringExtra(NumberInputActivity.NUMBER);
				sp.setLshj(new BigDecimal(number));
				Response r = spService.saveOrUpdate(sp);
				if(r.isOk()){
					//将修改信息放入发送队列
					msgQueueService.createMsg_Lshj(sp.getSpid(), sp.getLshj());
					Toast.makeText(SpkfkDetailActivity.this, "修改零售价成功", Toast.LENGTH_LONG)
					.show();
					notifyDataChanged();
				}else{
					Toast.makeText(SpkfkDetailActivity.this, "修改零售价失败", Toast.LENGTH_LONG)
					.show();
				}
			}
		}else if(requestCode == IntentIntegrator.REQUEST_CODE){
			//接收条码扫描软件BarcodeScanner的扫码结果
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
			if (result != null) {
				String sptm = result.getContents();
				if (sptm != null) {
					sp.setSptm(sptm);
					Response r = spService.saveOrUpdate(sp);
					if(r.isOk()){
						Toast.makeText(this, "修改条码成功", Toast.LENGTH_LONG).show();
						notifyDataChanged();
					}else{
						Toast.makeText(SpkfkDetailActivity.this, "修改条码失败", Toast.LENGTH_SHORT)
						.show();
					}
					
				} else {
					Toast.makeText(this, "未解析出任何条码", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	private void display(){
		TextView tvSpid = (TextView) findViewById(R.id.tvSpid);
		tvSpid.setText(sp.getSpid());
		TextView tvSpbh = (TextView) findViewById(R.id.tvSpbh);
		tvSpbh.setText(sp.getSpbh());
		TextView tvSpmch = (TextView) findViewById(R.id.tvSpmch);
		tvSpmch.setText(sp.getSpmch());
		TextView tvDw = (TextView) findViewById(R.id.tvDw);
		tvDw.setText(sp.getDw());
		TextView tvShpgg = (TextView) findViewById(R.id.tvShpgg);
		tvShpgg.setText(sp.getShpgg());
		TextView tvShengccj = (TextView) findViewById(R.id.tvShengccj);
		tvShengccj.setText(sp.getShengccj());
		TextView tvCabinetNo = (TextView) findViewById(R.id.tvCabinetNo);
		tvCabinetNo.setText(sp.getGh());
		TextView tvLshj = (TextView) findViewById(R.id.tvLshj);
		tvLshj.setText(sp.getLshj().toString());
		TextView tvSptm = (TextView)findViewById(R.id.tvSptm);
		tvSptm.setText(sp.getSptm());
	}
	
	private void notifyDataChanged(){
		//刷新
		display();
		//设置返回商品列表的结果
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_NAME_SPKFK, sp);
		setResult(RESULT_CODE_SUCCESS, resultIntent);
	}

}
