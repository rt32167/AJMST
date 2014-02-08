package com.ajmst.android.salesorder;

import java.util.List;

import com.ajmst.android.R;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.android.ui.NumberInputActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OrderItemListAdaper extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private List<SalesOrderItem> orderItems;
	
	public OrderItemListAdaper(Activity activity,List<SalesOrderItem> orderItems) {
		super();
		this.activity = activity;
		this.orderItems = orderItems;
		this.inflater = LayoutInflater.from(activity);
	}
	

	public List<SalesOrderItem> getOrderItems() {
		return orderItems;
	}


	public void setOrderItems(List<SalesOrderItem> orderItems) {
		this.orderItems = orderItems;
	}


	@Override
	public int getCount() {
		return orderItems.size();
	}

	@Override
	public Object getItem(int position) {
		return orderItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SalesOrderItem orderItem = orderItems.get(position);
		convertView = inflater.inflate(R.layout.sales_order_item, null);
		TextView textViewSeq = (TextView)convertView.findViewById(R.id.textViewSeq);
		textViewSeq.setText("" + (position + 1));
		TextView tvSpmch = (TextView)convertView.findViewById(R.id.tvSpmch);
		tvSpmch.setText(orderItem.getSpmch());
		TextView tvShengccj = (TextView)convertView.findViewById(R.id.tvShengccj);
		tvShengccj.setText(orderItem.getShengccj());
		TextView tvDw = (TextView)convertView.findViewById(R.id.tvDw);
		tvDw.setText(orderItem.getDw());
		TextView tvShpgg = (TextView)convertView.findViewById(R.id.tvShpgg);
		tvShpgg.setText(orderItem.getShpgg());
/*		TextView tvCabinetNo = (TextView)convertView.findViewById(R.id.tvCabinetNo);
		tvCabinetNo.setText(spkfk.getGh());*/
		TextView tvLshj = (TextView)convertView.findViewById(R.id.tvLshj);
		tvLshj.setText(orderItem.getLshj().toString());
		
		TextView tvShl = (TextView)convertView.findViewById(R.id.tvShl);
		if(orderItem.getShl()%1 == 0){
			tvShl.setText(String.valueOf(orderItem.getShl().intValue()));
		}else{
			tvShl.setText(orderItem.getShl().toString());
		}
		
		convertView.setTag(orderItem);
		return convertView;
	}

}
