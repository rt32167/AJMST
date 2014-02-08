package com.ajmst.android.ui.maintain;

import java.util.List;

import com.ajmst.android.R;
import com.ajmst.commmon.entity.AjmstMaintain;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MaintainItemListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<AjmstMaintain> maintainItemList;
	private int selectedPosition = -1;
	private AjmstMaintain selectedMaintainItem;
	
	public MaintainItemListAdapter(Context context,List<AjmstMaintain> maintainItemList) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.maintainItemList = maintainItemList;
	}

	@Override
	public int getCount() {
		if(maintainItemList != null){
			return maintainItemList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(maintainItemList != null){
			return maintainItemList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			AjmstMaintain sp = this.maintainItemList.get(position);
			convertView = inflater.inflate(R.layout.maintain_item, null);
			TextView textSeq = (TextView)convertView.findViewById(R.id.seq);
			textSeq.setText(String.valueOf(position + 1));
			TextView textDesc = (TextView)convertView.findViewById(R.id.textViewDesc);
			textDesc.setText(sp.getSpmch());
			TextView textViewFactory = (TextView)convertView.findViewById(R.id.textViewFactory);
			textViewFactory.setText(sp.getShengccj());
			TextView textViewSpecification = (TextView)convertView.findViewById(R.id.textViewSpecification);
			textViewSpecification.setText(sp.getShpgg());
			TextView textViewUnit = (TextView)convertView.findViewById(R.id.textViewUnit);
			textViewUnit.setText(sp.getDw());
			TextView textViewBatchcode = (TextView)convertView.findViewById(R.id.textViewBatchcode);
			textViewBatchcode.setText(sp.getPihao());
			TextView textViewSuggestQuantity = (TextView)convertView.findViewById(R.id.textViewSuggestQuantity);
			if(sp.getSuggestQuantity() % 1.0 == 0){
				textViewSuggestQuantity.setText(String.valueOf(sp.getSuggestQuantity().intValue()));
			}else{
				textViewSuggestQuantity.setText(sp.getSuggestQuantity().toString());
			}
			
			TextView textViewCabinetNo = (TextView)convertView.findViewById(R.id.textViewCabinetNo);
			String cabinetNo = sp.getCabinetNo();
			if(cabinetNo != null){
				cabinetNo = cabinetNo.trim();
			}
			textViewCabinetNo.setText(cabinetNo);
			//EditText始终不弹出软件键盘
			TextView tvShl = (TextView)convertView.findViewById(R.id.tvShl);
			if(sp.getShl() != null){
				if(sp.getShl() % 1.0 == 0){
					tvShl.setText(String.valueOf(sp.getShl().intValue()));
				}else{
					tvShl.setText(sp.getShl().toString());
				}
			}else{
				tvShl.setText(null);
			}
			//editTextQuantity.setInputType(InputType.TYPE_NULL);
			
			if(selectedPosition == position){
				convertView.setBackgroundColor(Color.RED);
				selectedMaintainItem = sp;
			}else{
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}
	
	public void setSelectedPosition(int position){
		this.selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public AjmstMaintain getSelectedMaintainItem() {
		return selectedMaintainItem;
	}


}
