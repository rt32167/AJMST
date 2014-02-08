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
	private String[] areas = new String[] { "ȫ��", "������Է", "�Ž�����վ", "���·",
			"�Ž�·", "��ޱ·", "���С��" };
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
	 * �˵���������
	 * 
	 * @author xmz
	 * 
	 */
	class AlertClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(PopSelectActivity.this).setTitle("ѡ������")
					.setItems(areas, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(PopSelectActivity.this,
									"���Ѿ�ѡ����: " + which + ":" + areas[which],
									Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}
					}).show();
		}
	}

	/**
	 * ��ѡ�򵯳��˵�����
	 * 
	 * @author xmz
	 * 
	 */
	class CheckBoxClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(PopSelectActivity.this)
					.setTitle("ѡ������")
					.setMultiChoiceItems(areas, areaState,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
									// ���ĳ������
								}
							})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String s = "��ѡ����:";
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
										// û��ѡ��
									}
									dialog.dismiss();
								}
							}).setNegativeButton("ȡ��", null).create();
			areaCheckListView = ad.getListView();
			ad.show();
		}
	}

	/**
	 * ��ѡ�����˵�����
	 * 
	 * @author xmz
	 * 
	 */
	class RadioClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog ad = new AlertDialog.Builder(PopSelectActivity.this)
					.setTitle("ѡ������")
					.setSingleChoiceItems(areas, radioOnClick.getIndex(),
							radioOnClick).create();
			areaRadioListView = ad.getListView();
			ad.show();
		}
	}

	/**
	 * �����ѡ���¼�
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
					"���Ѿ�ѡ���ˣ� " + index + ":" + areas[index], Toast.LENGTH_LONG)
					.show();
			dialog.dismiss();
		}
	}
}
