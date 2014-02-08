package com.ajmst.android.salesorder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.ajmst.android.R;
import com.ajmst.android.application.AjmstApplication;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.android.service.SalesOrderService;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SalesOrderActivity extends Activity implements android.view.GestureDetector.OnGestureListener{
	private AjmstApplication app;
	private SalesOrder salesOrder;
	private SalesOrderService salesOrderService;
	private GestureDetector gestureDetector = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sales_order);
		
		app = (AjmstApplication)getApplication();
		salesOrder = app.getCurrSalesOrder();
		this.salesOrderService = new SalesOrderService(SalesOrderActivity.this);
		final ListView lvOrderItem = (ListView) findViewById(R.id.lvOrderItem);
		
		//声明检测手势事件
		gestureDetector = new GestureDetector(this,this);	
		OnTouchListener onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//v.requestFocus();
				gestureDetector.onTouchEvent(event);
				return false;
			}
		};
		lvOrderItem.setOnTouchListener(onTouchListener);

		//结算按钮
		Button btnFinish = (Button) findViewById(R.id.btnFinish);
		btnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(salesOrder != null){
					EditText edtCustomer = (EditText)findViewById(R.id.edtCustomer);
					salesOrder.setCustomer(edtCustomer.getText().toString().trim());
					salesOrderService.finishOrder(salesOrder);
					app.setCurrSalesOrder(null);
					salesOrder = app.getCurrSalesOrder();
					displaySalesOrder();
					setReturnResult();
				}else{
					Toast.makeText(SalesOrderActivity.this, "无单据可以结算", Toast.LENGTH_LONG).show();
				}

			}
		});
		
		//列表长按事件
		lvOrderItem.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(
						SalesOrderActivity.this);
				// builder.setMessage("是否继续上次未完成的单据?");
				// builder.setTitle("提示");
				final CharSequence[] choices = new CharSequence[1];
				choices[0] = "删除";
				builder.setItems(choices,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									SalesOrderItem orderItem = (SalesOrderItem) lvOrderItem
											.getItemAtPosition(position);
									salesOrder.deleteItem(orderItem);
									salesOrderService.saveOrUpdate(salesOrder);
									setReturnResult();
									refreshSalesOrder();
									break;
								}
								dialog.dismiss();
							}
						});
				builder.create().show();
				return false;
			}
		});
		
		displaySalesOrder();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sales_order, menu);
		return true;
	}

	private void displaySalesOrder() {
		ListView lvOrderItem = (ListView) findViewById(R.id.lvOrderItem);
		List<SalesOrderItem> items = new ArrayList<SalesOrderItem>();
		if (this.salesOrder != null) {
			items = this.salesOrder.getItems();
		}
		OrderItemListAdaper adapter = new OrderItemListAdaper(
				SalesOrderActivity.this, items);
		lvOrderItem.setAdapter(adapter);
		showOrderInfo();
/*		if (this.salesOrder != null) {
			List<SalesOrderItem> items = this.salesOrder.getItems();
			ListView lvOrderItem = (ListView) findViewById(R.id.lvOrderItem);
			OrderItemListAdaper adapter = new OrderItemListAdaper(
					SalesOrderActivity.this, items);
			lvOrderItem.setAdapter(adapter);
			showOrderInfo();
		}*/
	}

	private void refreshSalesOrder() {
		ListView lvOrderItem = (ListView) findViewById(R.id.lvOrderItem);
		OrderItemListAdaper lvAdapter = ((OrderItemListAdaper) lvOrderItem
				.getAdapter());
		lvAdapter.notifyDataSetChanged();
		showOrderInfo();
	}

	/**
	 * 显示单据主信息
	 * 
	 * @author caijun 2014-1-6
	 */
	private void showOrderInfo() {
		int count = 0;
		Double amount = 0.0;

		TextView tvOrderNo = (TextView) findViewById(R.id.tvOrderNo);
		EditText edtCustomer = (EditText) findViewById(R.id.edtCustomer);
		TextView tvAmount = (TextView) findViewById(R.id.tvAmount);
		TextView tvCount = (TextView) findViewById(R.id.tvCount);

		
		if (this.salesOrder != null) {
			tvOrderNo.setText(this.salesOrder.getOrderNo());
			edtCustomer.setText(this.salesOrder.getCustomer());
			count = this.salesOrder.getItems().size();
			amount = SalesOrderService.getOrderAmount(this.salesOrder);
		}
		tvCount.setText(String.valueOf(count));
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(3);
		tvAmount.setText(nf.format(amount));//显示时保留3位小数
	}

	private void setReturnResult() {
		// 设置返回商品列表的结果
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event); 		// 注册手势事件
	}
	
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		boolean switchView = false;
		if (e2.getX() - e1.getX() > 250) {			 // 从左向右滑动（左进右出）
			finish();
		} else if (e2.getX() - e1.getX() < -250) {		 // 从右向左滑动（右进左出）

		}
		return true;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
