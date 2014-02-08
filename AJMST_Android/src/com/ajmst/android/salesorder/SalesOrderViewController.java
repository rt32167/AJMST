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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @deprecated 已经无用,20140204
 * @author caijun
 *
 */
public class SalesOrderViewController{
	private AjmstApplication app;
	private SalesOrder salesOrder;
	private SalesOrderService salesOrderService;
	private LayoutInflater inflater;
	private View contentView;
	private Activity activity;
	
	
	public SalesOrderViewController(Activity activity) {
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
		contentView = inflater.inflate(R.layout.activity_sales_order, null);
		this.salesOrderService = new SalesOrderService(activity);
		showData();
	}

	public void showData() {
/*		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sales_order);*/

		app = (AjmstApplication)activity.getApplication();
		salesOrder = app.getCurrSalesOrder();
		displaySalesOrder();

		Button btnFinish = (Button) contentView.findViewById(R.id.btnFinish);
		btnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edtCustomer = (EditText)contentView.findViewById(R.id.edtCustomer);
				salesOrder.setCustomer(edtCustomer.getText().toString().trim());
				salesOrderService.finishOrder(salesOrder);
				app.setCurrSalesOrder(null);
				showData();
			}
		});
		
		final ListView lvOrderItem = (ListView) contentView.findViewById(R.id.lvOrderItem);
		lvOrderItem.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					final int position, long id) {
				AlertDialog.Builder builder = new Builder(
						activity);
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
									//setReturnResult();
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
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sales_order, menu);
		return true;
	}*/

	private void displaySalesOrder() {
		ListView lvOrderItem = (ListView) contentView.findViewById(R.id.lvOrderItem);
		List<SalesOrderItem> items = new ArrayList<SalesOrderItem>();
		if (this.salesOrder != null) {
			items = this.salesOrder.getItems();
		}
		OrderItemListAdaper adapter = new OrderItemListAdaper(
				activity, items);
		lvOrderItem.setAdapter(adapter);
		showOrderInfo();
	}

	private void refreshSalesOrder() {
		ListView lvOrderItem = (ListView) contentView.findViewById(R.id.lvOrderItem);
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

		TextView tvOrderNo = (TextView) contentView.findViewById(R.id.tvOrderNo);
		EditText edtCustomer = (EditText) contentView.findViewById(R.id.edtCustomer);
		TextView tvAmount = (TextView) contentView.findViewById(R.id.tvAmount);
		TextView tvCount = (TextView) contentView.findViewById(R.id.tvCount);

		
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

	public View getContentView() {
		return contentView;
	}

	public void setContentView(View contentView) {
		this.contentView = contentView;
	}

/*	private void setReturnResult() {
		// 设置返回商品列表的结果
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
	}*/
	
}
