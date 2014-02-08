package com.ajmst.android.ui.spkfk;

import java.math.BigDecimal;
import java.util.List;

import com.ajmst.android.R;
import com.ajmst.android.application.AjmstApplication;
import com.ajmst.android.barcode.client.CaptureActivity;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.salesorder.SalesOrderActivity;
import com.ajmst.android.salesorder.SalesOrderViewController;
import com.ajmst.android.service.SalesOrderService;
import com.ajmst.android.service.SpkfkService;
import com.ajmst.android.ui.NumberInputActivity;
import com.ajmst.common.response.Response;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SpkfkSelectActivity extends Activity implements android.view.GestureDetector.OnGestureListener{
	
	private GestureDetector gestureDetector = null;
	
	private SpkfkService spService;
	private SalesOrderService salesOrderService;
	private SalesOrder salesOrder;
	private List<AdvSpkfk> spkfks;
	private AjmstApplication app;
	public static final int REQUEST_CODE_SPKFK_DETAIL = 3;
	public static final int REQUEST_CODE_GET_QUANTITY = 4;
	public static final int REQUEST_CODE_VIEW_ORDER = 5;
	public static final int FLAG_FIND_SP = 4;// ���������Ʒ ��flag
	public static final String EXTRA_NAME_SELECTED_SPKFK = "Selected_Spkfk";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE); //����Ϊ�ޱ���
		setContentView(R.layout.activity_spkfk_select);
		
		//������ҳ���
		gestureDetector = new GestureDetector(this,this);	// ������������¼�
		final ListView listViewSpkfk = (ListView)findViewById(R.id.listViewSpkfk);
		
		OnTouchListener onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//v.requestFocus();
				gestureDetector.onTouchEvent(event);
				return false;
			}
		};
		//ע�� listView����Ϊfill_parent,����Ŀʱ�ſɲ��񵽴����¼�
		listViewSpkfk.setOnTouchListener(onTouchListener);
		
		app = (AjmstApplication)getApplication();
		this.spService = new SpkfkService(SpkfkSelectActivity.this);
		this.salesOrderService = new SalesOrderService(SpkfkSelectActivity.this);
		
		//����Ƿ����ϴ�δ��ɵĵ���,�������û�ѡ������������
		salesOrder = this.salesOrderService.getUnFinishedOrder();
		app.setCurrSalesOrder(salesOrder);
		
		if(salesOrder != null){
			AlertDialog.Builder builder = new Builder(SpkfkSelectActivity.this);
			builder.setMessage("�Ƿ�����ϴ�δ��ɵĵ���?");
			builder.setTitle("��ʾ");
			builder.setCancelable(false);//ǿ���û�ѡ��
			builder.setPositiveButton("��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){
					//���ϴεĵ��ݸ�Ϊ���
					salesOrderService.finishOrder(salesOrder);
					app.setCurrSalesOrder(null);
					//������ʾ��Ʒ,Ϊ���������Ϊ���ڵ����е�ҩƷ
					refreshSpkfk();
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		
		//��Ʒ���������������
		EditText etZjm = (EditText)findViewById(R.id.etZjm);
		etZjm.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 1){
					searchSpkfk();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		//��ѡ��ť
		final Button btnCabinet = (Button)findViewById(R.id.btnCabinet);
		btnCabinet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "ȫ��";
				cabinets[1] = "1��";
				cabinets[2] = "2��";
				cabinets[3] = "3��";
				cabinets[4] = "4��";
				cabinets[5] = "5��";
				cabinets[6] = "6��";
				cabinets[7] = "7��";
				cabinets[8] = "8��";
				cabinets[9] = "9��";
				cabinets[10] = "10��";
				cabinets[11] = "11��";
				cabinets[12] = "12��";
				cabinets[13] = "13��";
				cabinets[14] = "14��";
				cabinets[15] = "A��";
				cabinets[16] = "B��";
				cabinets[17] = "C��";
				cabinets[18] = "D��";
				cabinets[19] = "E��";
				cabinets[20] = "F��";
				cabinets[21] = "����";
				cabinets[22] = "����";
				cabinets[23] = "����";
				
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(cabinets, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String cabinet = cabinets[which].toString();
						btnCabinet.setText(cabinet);
						String tag;
						if("ȫ��".equals(cabinet)){
							tag = null;
						}else if("����".equals(cabinet)){
							tag = "null";
						}else{
							tag = cabinet.replace("��", "");
						}
						btnCabinet.setTag(tag);
						dialog.dismiss();
					}
				}).show();
			}
		});
	
		//�۸�ѡ��ť
		final Button btnPrice = (Button)findViewById(R.id.btnPrice);
		btnPrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] prices = new CharSequence[16];
				prices[0] = "ȫ��";
				prices[1] = "0-5";
				prices[2] = "6-10";
				prices[3] = "11-15";
				prices[4] = "16-20";
				prices[5] = "21-25";
				prices[6] = "26-30";
				prices[7] = "31-35";
				prices[8] = "36-40";
				prices[9] = "41-50";
				prices[10] = "51-60";
				prices[11] = "61-70";
				prices[12] = "71-80";
				prices[13] = "81-90";
				prices[14] = "91-99";
				prices[15] = "100����";
				
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(prices, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String price = prices[which].toString();
						btnPrice.setText(price);
						if("ȫ��".equals(price)){
							btnPrice.setTag(null);
						}else{
							BigDecimal from;
							BigDecimal end;
							if("100����".equals(price)){
								from = BigDecimal.valueOf(100);
								end = BigDecimal.valueOf(Integer.MAX_VALUE);
							}else{
								
								from = new BigDecimal(price.split("-")[0]);
								end = new BigDecimal(price.split("-")[1]);
							}
							BigDecimal[] priceArray = new BigDecimal[2];
							priceArray[0] = from;
							priceArray[1] = end;
							btnPrice.setTag(priceArray);
						}
						dialog.dismiss();
					}
				}).show();
			}
		});
		
		//ҩƷ����ѡ��ť
		final Button btnType = (Button)findViewById(R.id.btnType);
		btnType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] types = new CharSequence[3];
				types[0] = "ȫ��";
				types[1] = "��ҩ";
				types[2] = "��ҩ";
				
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(types, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String type = types[which].toString();
						btnType.setText(type);
						dialog.dismiss();
					}
				}).show();
			}
		});
		//Ĭ��ѡ����ҩ
		btnType.setText("��ҩ");
		
		//��ѯ��ť
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchSpkfk();
			}
		});
		
		//final ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		
		//������activity����ѡ����Ʒʱ,����list item����¼�
		Intent externalIntent = getIntent();
		if(externalIntent != null && externalIntent.getFlags() == FLAG_FIND_SP){
			final String sptm = externalIntent.getStringExtra(CaptureActivity.EXTRA_NAME_SPTM);
			listViewSpkfk.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					//���÷��ؽ��
					AdvSpkfk sp = (AdvSpkfk)listViewSpkfk.getItemAtPosition(position);
					sp.setSptm(sptm);
					Response r = spService.saveOrUpdate(sp);
					if(r.isOk()){
						Toast.makeText(SpkfkSelectActivity.this, "�����޸ĳɹ�", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(SpkfkSelectActivity.this, "�����޸�ʧ��:" + r.getExceptionStr(), Toast.LENGTH_LONG).show();
					}
					finish();
				}
			});
		}

		//����
		listViewSpkfk.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long id) {
				final CharSequence[] choices = new CharSequence[2];
				choices[0] = "�鿴����";
				choices[1] = "�鿴���۵�";
				ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
				final AdvSpkfk sp = (AdvSpkfk)lvSpkfk.getItemAtPosition(position);
				final int positionTmp= position;
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(choices, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String choice = choices[which].toString();
						switch(which){
							case 0:
								//�鿴����
								//������Ʒ����activity
								Intent intentViewDetail = new Intent(SpkfkSelectActivity.this, SpkfkDetailActivity.class);
								intentViewDetail.putExtra(SpkfkDetailActivity.EXTRA_NAME_SPKFK, sp);
								intentViewDetail.putExtra(NumberInputActivity.TAG, "" + positionTmp);
								startActivityForResult(intentViewDetail, SpkfkSelectActivity.REQUEST_CODE_SPKFK_DETAIL);
								break;
							case 1:
								//��ʾ���۵�
								showOrderDetail();
								break;
							case 2:
								break;
						}
						Toast.makeText(SpkfkSelectActivity.this, "ѡ���� " + choice, Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				}).show();
				return false;
			}
		});
		
/*		//��ӻ���ҳ��
		viewFlipper.addView(spkfkView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		viewFlipper.addView(orderView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		viewFlipper.setAutoStart(false);*/
/*		viewFlipper.setAutoStart(true);// �����Զ����Ź��ܣ�����¼���ǰ�Զ����ţ�
 		viewFlipper.setFlipInterval(3000);
		if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
			viewFlipper.startFlipping();
		}*/
		
		//��ʼ��ʱ��ѯһ��
		searchSpkfk();
	}

	/**
	 * ��ӵ����۵���ť,SpkfkSelectListAdaper����
	 * 
	 * @author caijun 2014-1-5
	 * @param position
	 */
	public void addToOrderBtnClick(final int position) {
		ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		final AdvSpkfk sp = (AdvSpkfk) lvSpkfk.getItemAtPosition(position);
		// ��ӵ�����
		final CharSequence[] quantitys = new CharSequence[13];
		//Ƶ�����ֵ���5\9\10\12\15\20\25\30\60
		quantitys[0] = "4";
		quantitys[1] = "5";
		quantitys[2] = "6";
		quantitys[3] = "8";
		quantitys[4] = "9";
		quantitys[5] = "10";
		quantitys[6] = "12";
		quantitys[7] = "15";
		quantitys[8] = "20";
		quantitys[9] = "25";
		quantitys[10] = "30";
		quantitys[11] = "60";
		quantitys[12] = "����";
		//ע��Ի��������������СR.style.dialog
		Builder builder = new AlertDialog.Builder(SpkfkSelectActivity.this,R.style.dialog).setTitle(null)
				.setItems(quantitys, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ������������
						if (which == (quantitys.length - 1)) {
							// �������������
							Intent intent = new Intent(
									SpkfkSelectActivity.this,
									NumberInputActivity.class);
							String number = "";
							intent.putExtra(NumberInputActivity.NUMBER, number);
							intent.putExtra(NumberInputActivity.TAG, ""
									+ position);
							intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);// �������ֻ��������λС��,��Ϊƽ�����ݿ���ֻ��2λ
							startActivityForResult(intent,
									REQUEST_CODE_GET_QUANTITY);
						} else {
							// ֱ��ѡ������
							Integer shl = Integer.valueOf(quantitys[which]
									.toString());
							addItem(sp, null, shl);
						}
						dialog.dismiss();
					}
				});
		builder.show(); 
	}

	/**
	 * ��ӵ����۵�
	 * 
	 * @author caijun 2014-1-4
	 * @param sp
	 * @param pihao
	 * @param shl
	 */
	public void addItem(AdvSpkfk sp, String pihao, Integer shl) {
		addItem(sp, pihao, Double.valueOf(shl));
	}

	/**
	 * ��ӵ����۵�
	 * 
	 * @author caijun 2014-1-4
	 * @param sp
	 * @param pihao
	 * @param shl
	 */
	public void addItem(AdvSpkfk sp, String pihao, Double shl) {
		//��ǰ�޵���ʱ
		if (salesOrder == null) {
			String orderNo = salesOrderService.generateOrderNo();
			salesOrder = new SalesOrder(orderNo);
			app.setCurrSalesOrder(salesOrder);
		}
		salesOrder.addItem(sp, null, shl);
		salesOrderService.saveOrUpdate(salesOrder);
		refreshSpkfk();//ˢ��,�Ա���Ѽ��뵥�ݵ���Ʒ
		Toast.makeText(SpkfkSelectActivity.this, "��ӳɹ�,����:" + shl,
				Toast.LENGTH_SHORT).show();
		EditText etZjm = (EditText)findViewById(R.id.etZjm);
		etZjm.setText(null);
	}

	private void displaySpkfk() {
		ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		SpkfkSelectListAdaper lvAdapter = new SpkfkSelectListAdaper(
				SpkfkSelectActivity.this, this.spkfks);
		lvAdapter.setSalesOrder(salesOrder);
		listViewSpkfk.setAdapter(lvAdapter);
	}

	private void refreshSpkfk() {
		ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		SpkfkSelectListAdaper lvAdapter = ((SpkfkSelectListAdaper) lvSpkfk
				.getAdapter());
		//����󲻲�ѯ,ֱ�ӻ��������ݽ���,�ٻ�������ʱ,lvAdapterΪnull,��Ϊ��δ���ù�Adapter
		if(lvAdapter != null){
			salesOrder = app.getCurrSalesOrder();
			lvAdapter.setSalesOrder(salesOrder);
			lvAdapter.notifyDataSetChanged();
		}
	}

	public void searchSpkfk() {
		EditText etZjm = (EditText) findViewById(R.id.etZjm);
		Button btnCabinet = (Button) findViewById(R.id.btnCabinet);
		Button btnPrice = (Button) findViewById(R.id.btnPrice);
		Button btnType = (Button) findViewById(R.id.btnType);
		String zjm = etZjm.getText().toString();
		String cabinet = null;
		BigDecimal priceFrom = null;
		BigDecimal priceEnd = null;
		if (btnCabinet.getTag() != null) {
			cabinet = btnCabinet.getTag().toString();
		}
		if (btnPrice.getTag() != null) {
			BigDecimal[] priceSection = (BigDecimal[]) btnPrice.getTag();
			priceFrom = priceSection[0];
			priceEnd = priceSection[1];
		}
		Boolean isSelfCnSp = null;
		String type = btnType.getText().toString();
		if ("��ҩ".equals(type)) {
			isSelfCnSp = true;
		} else if ("��ҩ".equals(type)) {
			isSelfCnSp = false;
		}
		this.spkfks = spService.query(zjm, cabinet, priceFrom, priceEnd,
				isSelfCnSp);
		displaySpkfk();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		final Intent intent = data;
		if (intent == null) {
			return;
		}
		ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		SpkfkSelectListAdaper lvAdapter = ((SpkfkSelectListAdaper) lvSpkfk
				.getAdapter());

		if (requestCode == REQUEST_CODE_GET_QUANTITY
				&& resultCode == NumberInputActivity.RESULT_CODE_GET_INPUT) {
			// ���ռ������۵�ʱ������
			String number = intent.getStringExtra(NumberInputActivity.NUMBER);
			int position = Integer.valueOf(intent
					.getStringExtra(NumberInputActivity.TAG));
			if (number != null && "".equals(number) == false) {
				lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
				AdvSpkfk sp = (AdvSpkfk) lvSpkfk.getItemAtPosition(position);
				Double shl = Double.valueOf(number);
				addItem(sp, null, shl);
			}
		} else if (requestCode == REQUEST_CODE_SPKFK_DETAIL
				&& resultCode == SpkfkDetailActivity.RESULT_CODE_SUCCESS) {
			// �޸���ϸ���Ϻ󷵻�
			AdvSpkfk sp = (AdvSpkfk) intent
					.getSerializableExtra(SpkfkDetailActivity.EXTRA_NAME_SPKFK);
			if (sp != null) {
				lvAdapter.setSpkfk(sp);
				refreshSpkfk();
			}
		}else if(requestCode == REQUEST_CODE_VIEW_ORDER){
			//������ʾ��Ʒ,Ϊ���������Ϊ���ڵ����е�ҩƷ
			refreshSpkfk();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event); 		// ע�������¼�
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e2.getX() - e1.getX() > 250) {			 // �������һ���������ҳ���
/*			Animation rInAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_right_in); 	// ���һ���������Ľ���Ч����alpha  0.1 -> 1.0��
			Animation rOutAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_right_out); // ���һ����Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��
			
			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			switchView = true;*/
		} else if (e2.getX() - e1.getX() < -250) {		 // �������󻬶����ҽ������
/*			Animation lInAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_left_in);		// ���󻬶�������Ľ���Ч����alpha 0.1  -> 1.0��
			Animation lOutAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_left_out); 	// ���󻬶��Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			switchView = true;*/
			//��ʾ���۵�
			showOrderDetail();
		}
		return true;
	}
	
	//��ʾ���۵�
	public void showOrderDetail(){
		//�������۵�activity
		Intent intentViewOrder = new Intent(SpkfkSelectActivity.this, SalesOrderActivity.class);
		startActivityForResult(intentViewOrder, REQUEST_CODE_VIEW_ORDER);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	
	

}
