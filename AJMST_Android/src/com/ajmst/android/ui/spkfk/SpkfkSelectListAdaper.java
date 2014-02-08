package com.ajmst.android.ui.spkfk;

import java.util.Hashtable;
import java.util.List;

import com.ajmst.android.R;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.android.ui.NumberInputActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SpkfkSelectListAdaper extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private List<AdvSpkfk> spkfks;
	private SalesOrder salesOrder;
	private Hashtable<String,Boolean> spkfkInOrder;
	
	public SpkfkSelectListAdaper(Activity activity,List<AdvSpkfk> spkfks) {
		super();
		this.activity = activity;
		this.spkfks = spkfks;
		this.inflater = LayoutInflater.from(activity);
		spkfkInOrder = new Hashtable<String,Boolean>();
	}
	

	public List<AdvSpkfk> getSpkfks() {
		return spkfks;
	}


	public void setSpkfks(List<AdvSpkfk> spkfks) {
		this.spkfks = spkfks;
	}
	
	public void setSpkfk(AdvSpkfk sp){
		for(int i =0; i < getSpkfks().size(); i++){
			AdvSpkfk spTmp = this.spkfks.get(i);
			if(sp.getSpid().equals(spTmp.getSpid())){
				this.spkfks.set(i, sp);
				break;
			}
		}
	}


	@Override
	public int getCount() {
		return spkfks.size();
	}

	@Override
	public Object getItem(int position) {
		return spkfks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final AdvSpkfk spkfk = spkfks.get(position);
		convertView = inflater.inflate(R.layout.spkfk_list_item, null);
		TextView textViewSeq = (TextView)convertView.findViewById(R.id.textViewSeq);
		textViewSeq.setText("" + (position + 1));
		TextView tvSpmch = (TextView)convertView.findViewById(R.id.tvSpmch);
		tvSpmch.setText(spkfk.getSpmch());
		TextView tvShengccj = (TextView)convertView.findViewById(R.id.tvShengccj);
		tvShengccj.setText(spkfk.getShengccj());
		TextView tvDw = (TextView)convertView.findViewById(R.id.tvDw);
		tvDw.setText(spkfk.getDw());
		TextView tvShpgg = (TextView)convertView.findViewById(R.id.tvShpgg);
		tvShpgg.setText(spkfk.getShpgg());
		TextView tvCabinetNo = (TextView)convertView.findViewById(R.id.tvCabinetNo);
		tvCabinetNo.setText(spkfk.getGh());
		TextView tvLshj = (TextView)convertView.findViewById(R.id.tvLshj);
		tvLshj.setText(spkfk.getLshj().toString());
		
		Button btnAddToOrder = (Button)convertView.findViewById(R.id.btnAddToOrder);
		//添加到销售单
		btnAddToOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SpkfkSelectActivity spkfkSelectActivity = (SpkfkSelectActivity)activity;
				spkfkSelectActivity.addToOrderBtnClick(position);
/*				//启动商品详情activity
				Intent intent = new Intent(inflater.getContext(), SpkfkDetailActivity.class);
				intent.putExtra(SpkfkDetailActivity.EXTRA_NAME_SPKFK, spkfk);
				intent.putExtra(NumberInputActivity.TAG, "" + position);
				activity.startActivityForResult(intent, SpkfkSelectActivity.REQUEST_CODE_SPKFK_DETAIL);*/
			}
		});
		convertView.setTag(spkfk);
		
		//若在单据中,标记颜色
		if(spkfkInOrder.get(spkfk.getSpid()) == Boolean.TRUE){
			convertView.setBackgroundColor(Color.GREEN);
		}else{
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		
		
		return convertView;
	}


	public SalesOrder getSalesOrder() {
		return salesOrder;
	}


	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
		spkfkInOrder.clear();
		if(this.salesOrder != null && salesOrder.getItems() != null){
			for(int i = 0; i < salesOrder.getItems().size();i++){
				SalesOrderItem item = salesOrder.getItems().get(i);
				spkfkInOrder.put(item.getSpid(), Boolean.TRUE);
			}
		}
		this.notifyDataSetChanged();
	}
	

}
