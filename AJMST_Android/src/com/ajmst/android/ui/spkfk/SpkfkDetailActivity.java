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
		//���ذ�ť
		Button btnReturn = (Button) findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//�޸Ĺ�Ű�ť
		Button btnCabinet = (Button) findViewById(R.id.btnCabinet);
		btnCabinet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "";
				cabinets[1] = "1��";
				cabinets[2] = "2��";
				cabinets[3] = "3��";
				cabinets[4] = "4��";
				cabinets[5] = "5��";
				cabinets[6] = "6��";
				cabinets[7] = "7��";
				cabinets[8] = "8��";
				cabinets[9] = "9��";
				cabinets[10] = "10��";
				cabinets[11] = "11��";
				cabinets[12] = "12��";
				cabinets[13] = "13��";
				cabinets[14] = "14��";
				cabinets[15] = "A��";
				cabinets[16] = "B��";
				cabinets[17] = "C��";
				cabinets[18] = "D��";
				cabinets[19] = "E��";
				cabinets[20] = "F��";
				cabinets[21] = "����";
				cabinets[22] = "����";
				cabinets[23] = "����";
				
				new AlertDialog.Builder(SpkfkDetailActivity.this).setTitle(null)
				.setItems(cabinets, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String cabinet = cabinets[which].toString();
						cabinet = cabinet.replace("��", "");
						if( "".equals(cabinet)){
							cabinet = null;
						}
						sp.setGh(cabinet);
						Response r = spService.saveOrUpdate(sp);
						if(r.isOk()){
							Toast.makeText(SpkfkDetailActivity.this, "�޸Ĺ�ųɹ�", Toast.LENGTH_LONG).show();
							notifyDataChanged();
						}else{
							Toast.makeText(SpkfkDetailActivity.this, "�޸Ĺ��ʧ��", Toast.LENGTH_LONG)
							.show();
						}
						dialog.dismiss();
					}
				}).show();
			}
		});
		//�޸����ۼ۰�ť
		Button btnLshj = (Button) findViewById(R.id.btnLshj);
		btnLshj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�������������
				Intent intent = new Intent(SpkfkDetailActivity.this, NumberInputActivity.class);
				String number = "" + sp.getLshj().doubleValue();
				intent.putExtra(NumberInputActivity.NUMBER, number);
				//intent.putExtra(NumberInputActivity.TAG, "" + positionTmp);
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//�������ֻ��������λС��,��Ϊƽ�����ݿ���ֻ��2λ
				startActivityForResult(intent, REQUEST_CODE_GET_LSHJ);
			}
			
		});
		//�޸����밴ť
		Button btnSptm = (Button) findViewById(R.id.btnSptm);
		btnSptm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*				//��������ɨ�����BarcodeScanner
				IntentIntegrator integrator = new IntentIntegrator(ChinesePriceActivity.this);//ָ����ǰ��activity
				//integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES); //����ɨ���ά��
				integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES); //����ɨ���������͵���,��������Ͷ�ά��
				*/			
				Intent intent = new Intent(SpkfkDetailActivity.this, CaptureActivity.class);
				intent.setAction(Intents.Scan.ACTION);
				startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);//ע��������һ��Ҫ��IntentIntegrator.REQUEST_CODE
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
		//�������ۼ�����򷵻ؽ��
		if (requestCode == REQUEST_CODE_GET_LSHJ) {
			if (intent != null) {
				String number = intent.getStringExtra(NumberInputActivity.NUMBER);
				sp.setLshj(new BigDecimal(number));
				Response r = spService.saveOrUpdate(sp);
				if(r.isOk()){
					//���޸���Ϣ���뷢�Ͷ���
					msgQueueService.createMsg_Lshj(sp.getSpid(), sp.getLshj());
					Toast.makeText(SpkfkDetailActivity.this, "�޸����ۼ۳ɹ�", Toast.LENGTH_LONG)
					.show();
					notifyDataChanged();
				}else{
					Toast.makeText(SpkfkDetailActivity.this, "�޸����ۼ�ʧ��", Toast.LENGTH_LONG)
					.show();
				}
			}
		}else if(requestCode == IntentIntegrator.REQUEST_CODE){
			//��������ɨ�����BarcodeScanner��ɨ����
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
			if (result != null) {
				String sptm = result.getContents();
				if (sptm != null) {
					sp.setSptm(sptm);
					Response r = spService.saveOrUpdate(sp);
					if(r.isOk()){
						Toast.makeText(this, "�޸�����ɹ�", Toast.LENGTH_LONG).show();
						notifyDataChanged();
					}else{
						Toast.makeText(SpkfkDetailActivity.this, "�޸�����ʧ��", Toast.LENGTH_SHORT)
						.show();
					}
					
				} else {
					Toast.makeText(this, "δ�������κ�����", Toast.LENGTH_LONG).show();
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
		//ˢ��
		display();
		//���÷�����Ʒ�б�Ľ��
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EXTRA_NAME_SPKFK, sp);
		setResult(RESULT_CODE_SUCCESS, resultIntent);
	}

}
