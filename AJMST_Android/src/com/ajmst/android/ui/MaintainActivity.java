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
		//显示为最后一次编辑时的柜号
		String lastGH = preferences.getString("lastGH", "全部");
		buttonGH.setText(lastGH);
		//柜号选择按钮
		buttonGH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//changPopState(v);
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "全部";
				cabinets[1] = "未完成";
				cabinets[2] = "1柜";
				cabinets[3] = "2柜";
				cabinets[4] = "3柜";
				cabinets[5] = "4柜";
				cabinets[6] = "5柜";
				cabinets[7] = "6柜";
				cabinets[8] = "7柜";
				cabinets[9] = "8柜";
				cabinets[10] = "9柜";
				cabinets[11] = "10柜";
				cabinets[12] = "11柜";
				cabinets[13] = "12柜";
				cabinets[14] = "13柜";
				cabinets[15] = "14柜";
				cabinets[16] = "A柜";
				cabinets[17] = "B柜";
				cabinets[18] = "C柜";
				cabinets[19] = "D柜";
				cabinets[20] = "E柜";
				cabinets[21] = "F柜";
				cabinets[22] = "冰箱";
				cabinets[23] = "里面";
				
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
		//养护列表单击监听器,弹出输入输入窗口
		ListView maintainList = (ListView) findViewById(R.id.listView1);
		maintainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				//弹出数字输入框
				Intent intent = new Intent(MaintainActivity.this, NumberInputActivity.class);
				TextView tvShl = (TextView) v.findViewById(R.id.tvShl);
				String number = tvShl.getText().toString();
				intent.putExtra(NumberInputActivity.NUMBER, number);
				intent.putExtra(NumberInputActivity.TAG, "" + position);
				intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);//设置最多只能输入两位小数,因为平安数据库中只存2位
				startActivityForResult(intent, NumberInputActivity.REQUEST_CODE_GET_INPUT);
			}

		});

		//记录最后导入时间,以便恢复activity后判断数据是否发生变化
		lastImportTime = preferences.getString("lastImportTime", null);
		//根据上一次选中的柜号显示养护条目
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
						//记录当前编辑的柜号、第几行以便下回直接跳转到该条目
						int idx = maintainItemList.indexOf(mt);
						Editor editor = preferences.edit();
						editor.putInt("lastInx", idx);
						Button buttonGH = (Button) findViewById(R.id.buttonGH);
						editor.putString("lastGH", buttonGH.getText().toString());
						editor.commit();
						//刷新显示数量
						listAdapter.notifyDataSetChanged();
					}else{
						Toast.makeText(mContext, "更新失败", Toast.LENGTH_SHORT)
						.show();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 显示数据
	 * @author caijun
	 * @param gh
	 * @return 当前显示个数
	 */
	private int showData(String gh){
		String lastGH = preferences.getString("lastGH", "全部");
		int selectIdx = preferences.getInt("lastInx", 0);
		
		maintainItemList = maintainService.getMaintainItemsByGH(gh);
		
		System.out.println("size:" + maintainItemList.size());
		
		listAdapter = new MaintainItemListAdapter(MaintainActivity.this,
				maintainItemList);
		ListView maintainListView = (ListView) findViewById(R.id.listView1);
		maintainListView.setAdapter(listAdapter);
		//最后一次编辑时的柜号等于当前要显示的柜号时,选中最后编辑的条目
		if(lastGH.endsWith(gh)){
			maintainListView.setSelection(selectIdx);
		}
		Toast.makeText(mContext, gh + ":" + maintainItemList.size() + " 个", Toast.LENGTH_SHORT)
		.show();
		return maintainItemList.size();
	}
	
	@Override
	protected void onResume() {
		//切换回来后若发现数据已发生更改,则刷新数据,因为可能在配置界面进行了导入操作
		String newLastImportTime = preferences.getString("lastImportTime", null);
		if(lastImportTime != newLastImportTime){
			Toast.makeText(this, "注意导入了新数据,系统自动刷新", Toast.LENGTH_LONG).show();
			Button buttonGH = (Button) findViewById(R.id.buttonGH);
			buttonGH.setText("全部");
			showData("全部");
			lastImportTime = newLastImportTime;
		}
		super.onResume();
	}
	}
