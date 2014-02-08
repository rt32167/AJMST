package com.ajmst.android.ui;

import com.ajmst.android.R;
import com.ajmst.android.util.StringUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NumberInputActivity extends Activity {
	public static final int REQUEST_CODE_GET_INPUT = 0;//请求码,用于接收调用该activity的activity接收结果时进行识别
	public static final int RESULT_CODE_GET_INPUT = 1;
	public static final String NUMBER = "Number";
	public static final String TAG = "Tag";
	public static final String DECIMAL_COUT = "DecimalCount";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_input);
		// 显示传入的数字
		Intent intent = getIntent();
		String existNumber = intent.getStringExtra(NUMBER);
		final String tag = intent.getStringExtra(TAG);
		final int decimalCount = intent.getIntExtra(DECIMAL_COUT, Integer.MAX_VALUE);
		final EditText editTextInputNumber = (EditText) findViewById(R.id.editTextInputNumber);
		editTextInputNumber.setText(existNumber);

		// 设置按钮事件
		Button buttonNum0 = (Button) findViewById(R.id.buttonNum0);
		Button buttonNum1 = (Button) findViewById(R.id.buttonNum1);
		Button buttonNum2 = (Button) findViewById(R.id.buttonNum2);
		Button buttonNum3 = (Button) findViewById(R.id.buttonNum3);
		Button buttonNum4 = (Button) findViewById(R.id.buttonNum4);
		Button buttonNum5 = (Button) findViewById(R.id.buttonNum5);
		Button buttonNum6 = (Button) findViewById(R.id.buttonNum6);
		Button buttonNum7 = (Button) findViewById(R.id.buttonNum7);
		Button buttonNum8 = (Button) findViewById(R.id.buttonNum8);
		Button buttonNum9 = (Button) findViewById(R.id.buttonNum9);
		Button buttonDecimal = (Button) findViewById(R.id.buttonDecimalPoint);
		Button buttonClear = (Button) findViewById(R.id.buttonClear);
		Button buttonBackSpace = (Button) findViewById(R.id.buttonBackSpace);
		final Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
		final Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
		
		OnClickListener buttonNumClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button buttonNum = (Button) v;
				String oldText = editTextInputNumber.getText().toString();
				String inputNunber = buttonNum.getTag().toString();
				String newText = null;
				boolean confirmEnable = true;
				if ("Clear".equals(inputNunber)) {
					newText = "";
				}else if("Backspace".equals(inputNunber)){
					if(oldText.length() > 0){
						newText = oldText.substring(0, oldText.length() - 1);
					}
				}else if("Confirm".equals(inputNunber)){
					Intent resultIntent = new Intent();
					resultIntent.putExtra(NUMBER, editTextInputNumber.getText().toString());
					resultIntent.putExtra(TAG,tag);
					setResult(RESULT_CODE_GET_INPUT, resultIntent);
					finish();
				}else if("Cancel".equals(inputNunber)){
					finish();
				} else {
					newText = oldText + inputNunber;
				}
				if(newText == null || "".equals(newText) || StringUtils.isDouble(newText)){
					//buttonConfirm.setEnabled(true);
					confirmEnable = true;
				}else{
					//buttonConfirm.setEnabled(false);
					confirmEnable = false;
				}
				editTextInputNumber.setText(newText);
				//若超出限制的小数位数,则变红提示
				if(newText != null && newText.contains(".") && newText.length() - 1 - newText.indexOf(".") > decimalCount){
					if(newText.length() - 1 - newText.indexOf(".") > decimalCount){
						editTextInputNumber.setBackgroundColor(Color.RED);
						//buttonConfirm.setEnabled(false);
						confirmEnable = false;
					}
				}else{
					editTextInputNumber.setBackgroundColor(Color.WHITE);
					//buttonConfirm.setEnabled(true);
					confirmEnable = true;
				}
				buttonConfirm.setEnabled(confirmEnable);
			}
		};
		
		buttonNum0.setOnClickListener(buttonNumClickListener);
		buttonNum1.setOnClickListener(buttonNumClickListener);
		buttonNum2.setOnClickListener(buttonNumClickListener);
		buttonNum3.setOnClickListener(buttonNumClickListener);
		buttonNum4.setOnClickListener(buttonNumClickListener);
		buttonNum5.setOnClickListener(buttonNumClickListener);
		buttonNum6.setOnClickListener(buttonNumClickListener);
		buttonNum7.setOnClickListener(buttonNumClickListener);
		buttonNum8.setOnClickListener(buttonNumClickListener);
		buttonNum9.setOnClickListener(buttonNumClickListener);
		buttonDecimal.setOnClickListener(buttonNumClickListener);
		buttonClear.setOnClickListener(buttonNumClickListener);
		buttonBackSpace.setOnClickListener(buttonNumClickListener);
		buttonConfirm.setOnClickListener(buttonNumClickListener);
		buttonCancel.setOnClickListener(buttonNumClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.number_input, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
