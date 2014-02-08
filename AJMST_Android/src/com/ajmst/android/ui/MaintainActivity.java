package com.ajmst.android.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajmst.android.R;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.android.service.MaintainService;
import com.ajmst.android.ui.maintain.MaintainItemListAdapter;
import com.ajmst.android.util.StringUtils;

public class MaintainActivity extends Activity {
	ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	Context mContext;
	private MaintainItemListAdapter listAdapter;
	List<AjmstMaintain> maintainItemList;
	private MaintainService maintainService;
	private SharedPreferences preferences;
	private String lastImportTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.maintain);
		mContext = this;
		maintainService = new MaintainService(mContext);
		
		String preferencesName = this.getString(R.string.preferences_of_maintain);
		preferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);

		Button buttonGH = (Button) findViewById(R.id.buttonGH);
		//��ʾΪ���һ�α༭ʱ�Ĺ��
		String lastGH = preferences.getString("lastGH", "ȫ��");
		buttonGH.setText(lastGH);
		//���ѡ��ť
		buttonGH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//changPopState(v);
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "ȫ��";
				cabinets[1] = "δ���";
				cabinets[2] = "1��";
				cabinets[3] = "2��";
				cabinets[4] = "3��";
				cabinets[5] = "4��";
				cabinets[6] = "5��";
				cabinets[7] = "6��";
				cabinets[8] = "7��";
				cabinets[9] = "8��";
				cabinets[10] = "9��";
				cabinets[11] = "10��";
				cabinets[12] = "11��";
				cabinets[13] = "12��";
				cabinets[14] = "13��";
				cabinets[15] = "14��";
				cabinets[16] = "A��";
				cabinets[17] = "B��";
				cabinets[18] = "C��";
				cabinets[19] = "D��";
				cabinets[20] = "E��";
				cabinets[21] = "F��";
				cabinets[22] = "����";
				cabinets[23] = "����";
				
				new AlertDialog.Builder(MaintainActivity.this).setTitle(null)
				.setItems(cabinets, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String cabinet = cabinets[which].toString();
						Button buttonGH = (Button) findViewById(R.id.buttonGH);
						buttonGH.setText(cabinet);
						showData(cabinet);
						dialog.dismiss();
					}
				}).show();
			}
		});
		//�����б���������,�����������봰��
		ListView maintainList = (ListView) findViewById(R.id.listView1);
		maintainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				//�������������
				Intent intent = new Intent(MaintainActivity.this, NumberInputActivity.class);
				TextView tvShl = (TextView) v.findViewById(R.id.tvShl);
				String number = tvShl.getText().toString();
				intent.putExtra(NumberInputActivity.NUMBER, number);
				intent.putExtra(NumberInputActivity.TAG, "" + position);
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//�������ֻ��������λС��,��Ϊƽ�����ݿ���ֻ��2λ
				startActivityForResult(intent, NumberInputActivity.REQUEST_CODE_GET_INPUT);
			}

		});

		//��¼�����ʱ��,�Ա�ָ�activity���ж������Ƿ����仯
		lastImportTime = preferences.getString("lastImportTime", null);
		//������һ��ѡ�еĹ����ʾ������Ŀ
		showData(lastGH);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NumberInputActivity.RESULT_CODE_GET_INPUT) {
			final Intent intent = data;
			if (intent != null) {
				String number = intent.getStringExtra(NumberInputActivity.NUMBER);
				int position = Integer.valueOf(intent
						.getStringExtra(NumberInputActivity.TAG));
				if (number != null && "".equals(number) == false) {
					ListView lvMaintain = (ListView) findViewById(R.id.listView1);
					AjmstMaintain mt = (AjmstMaintain) lvMaintain.getItemAtPosition(position);
					mt.setShl(StringUtils.stringtodouble(number));
					
					boolean result = maintainService.updateQuantity(mt);
					if(result == true){
						//��¼��ǰ�༭�Ĺ�š��ڼ����Ա��»�ֱ����ת������Ŀ
						int idx = maintainItemList.indexOf(mt);
						Editor editor = preferences.edit();
						editor.putInt("lastInx", idx);
						Button buttonGH = (Button) findViewById(R.id.buttonGH);
						editor.putString("lastGH", buttonGH.getText().toString());
						editor.commit();
						//ˢ����ʾ����
						listAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(mContext, "����ʧ��", Toast.LENGTH_SHORT)
						.show();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ��ʾ����
	 * @author caijun
	 * @param gh
	 * @return ��ǰ��ʾ����
	 */
	private int showData(String gh){
		String lastGH = preferences.getString("lastGH", "ȫ��");
		int selectIdx = preferences.getInt("lastInx", 0);
		
		maintainItemList = maintainService.getMaintainItemsByGH(gh);
		
		System.out.println("size:" + maintainItemList.size());
		
		listAdapter = new MaintainItemListAdapter(MaintainActivity.this,
				maintainItemList);
		ListView maintainListView = (ListView) findViewById(R.id.listView1);
		maintainListView.setAdapter(listAdapter);
		//���һ�α༭ʱ�Ĺ�ŵ��ڵ�ǰҪ��ʾ�Ĺ��ʱ,ѡ�����༭����Ŀ
		if(lastGH.endsWith(gh)){
			maintainListView.setSelection(selectIdx);
		}
		Toast.makeText(mContext, gh + ":" + maintainItemList.size() + " ��", Toast.LENGTH_SHORT)
		.show();
		return maintainItemList.size();
	}
	
	@Override
	protected void onResume() {
		//�л������������������ѷ�������,��ˢ������,��Ϊ���������ý�������˵������
		String newLastImportTime = preferences.getString("lastImportTime", null);
		if(lastImportTime != newLastImportTime){
			Toast.makeText(this, "ע�⵼����������,ϵͳ�Զ�ˢ��", Toast.LENGTH_LONG).show();
			Button buttonGH = (Button) findViewById(R.id.buttonGH);
			buttonGH.setText("ȫ��");
			showData("ȫ��");
			lastImportTime = newLastImportTime;
		}
		super.onResume();
	}
	}
