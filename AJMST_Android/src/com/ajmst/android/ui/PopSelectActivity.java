package com.ajmst.android.ui;

import com.ajmst.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PopSelectActivity extends Activity {
	private String[] areas = new String[] { "全部", "玉兰香苑", "张江地铁站", "金科路",
			"张江路", "紫薇路", "香楠小区" };
	private boolean[] areaState = new boolean[] { true, false, false, false,
			false, false, false };
	private RadioOnClick radioOnClick = new RadioOnClick(1);
	private ListView areaCheckListView;
	private ListView areaRadioListView;
	private Button alertButton;
	private Button checkBoxButton;
	private Button radioButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_select);

		alertButton = (Button) findViewById(R.id.alertButton);
		checkBoxButton = (Button) findViewById(R.id.checkBoxButton);
		radioButton = (Button) findViewById(R.id.radioButton);

		alertButton.setOnClickListener(new AlertClickListener());
		checkBoxButton.setOnClickListener(new CheckBoxClickListener());
		radioButton.setOnClickListener(new RadioClickListener());
	}

	/**
	 * 菜单弹出窗口
	 * 
	 * @author xmz
	 * 
	 */
	class AlertClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(PopSelectActivity.this).setTitle("选择区域")
					.setItems(areas, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(PopSelectActivity.this,
									"您已经选择了: " + which + ":" + areas[which],
									Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}
					}).show();
		}
	}

	/**
	 * 多选框弹出菜单窗口
	 * 
	 * @author xmz
	 * 
	 */
	class CheckBoxClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(PopSelectActivity.this)
					.setTitle("选择区域")
					.setMultiChoiceItems(areas, areaState,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
									// 点击某个区域
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String s = "您选择了:";
									for (int i = 0; i < areas.length; i++) {
										if (areaCheckListView
												.getCheckedItemPositions().get(
														i)) {
											s += i
													+ ":"
													+ areaCheckListView
															.getAdapter()
															.getItem(i) + "  ";
										} else {
											areaCheckListView
													.getCheckedItemPositions()
													.get(i, false);
										}
									}
									if (areaCheckListView
											.getCheckedItemPositions().size() > 0) {
										Toast.makeText(PopSelectActivity.this,
												s, Toast.LENGTH_LONG).show();
									} else {
										// 没有选择
									}
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).create();
			areaCheckListView = ad.getListView();
			ad.show();
		}
	}

	/**
	 * 单选弹出菜单窗口
	 * 
	 * @author xmz
	 * 
	 */
	class RadioClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(PopSelectActivity.this)
					.setTitle("选择区域")
					.setSingleChoiceItems(areas, radioOnClick.getIndex(),
							radioOnClick).create();
			areaRadioListView = ad.getListView();
			ad.show();
		}
	}

	/**
	 * 点击单选框事件
	 * 
	 * @author xmz
	 * 
	 */
	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			Toast.makeText(PopSelectActivity.this,
					"您已经选择了： " + index + ":" + areas[index], Toast.LENGTH_LONG)
					.show();
			dialog.dismiss();
		}
	}
}
