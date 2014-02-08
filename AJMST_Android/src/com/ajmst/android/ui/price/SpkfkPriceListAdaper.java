package com.ajmst.android.ui.price;

import java.util.List;

import com.ajmst.android.R;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.commmon.entity.Spkfk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpkfkPriceListAdaper extends BaseAdapter{
	private LayoutInflater inflater;
	private List<AdvSpkfk> spkfks;
	
	public SpkfkPriceListAdaper(Context context,List<AdvSpkfk> spkfks) {
		super();
		this.spkfks = spkfks;
		this.inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Spkfk spkfk = spkfks.get(position);
		convertView = inflater.inflate(R.layout.chinese_price_item, null);
		TextView textViewSeq = (TextView)convertView.findViewById(R.id.textViewSeq);
		textViewSeq.setText("" + (position + 1));
		TextView textViewDesc = (TextView)convertView.findViewById(R.id.textViewDesc);
		textViewDesc.setText(spkfk.getSpmch());
		TextView textViewUnit = (TextView)convertView.findViewById(R.id.textViewUnit);
		textViewUnit.setText(spkfk.getDw());
		TextView textViewFactory = (TextView)convertView.findViewById(R.id.textViewFactory);
		textViewFactory.setText(spkfk.getShengccj());
		TextView editTextPrice = (TextView)convertView.findViewById(R.id.editTextPrice);
		editTextPrice.setText(spkfk.getLshj().toString());
		
		convertView.setTag(spkfk);
		return convertView;
	}

}
