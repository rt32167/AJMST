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
	public static final int FLAG_FIND_SP = 4;// 请求查找商品 的flag
	public static final String EXTRA_NAME_SELECTED_SPKFK = "Selected_Spkfk";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE); //设置为无标题
		setContentView(R.layout.activity_spkfk_select);
		
		//滑动翻页相关
		gestureDetector = new GestureDetector(this,this);	// 声明检测手势事件
		final ListView listViewSpkfk = (ListView)findViewById(R.id.listViewSpkfk);
		
		OnTouchListener onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//v.requestFocus();
				gestureDetector.onTouchEvent(event);
				return false;
			}
		};
		//注意 listView设置为fill_parent,无条目时才可捕获到触摸事件
		listViewSpkfk.setOnTouchListener(onTouchListener);
		
		app = (AjmstApplication)getApplication();
		this.spService = new SpkfkService(SpkfkSelectActivity.this);
		this.salesOrderService = new SalesOrderService(SpkfkSelectActivity.this);
		
		//检查是否有上次未完成的单据,若有让用户选择继续或者完成
		salesOrder = this.salesOrderService.getUnFinishedOrder();
		app.setCurrSalesOrder(salesOrder);
		
		if(salesOrder != null){
			AlertDialog.Builder builder = new Builder(SpkfkSelectActivity.this);
			builder.setMessage("是否继续上次未完成的单据?");
			builder.setTitle("提示");
			builder.setCancelable(false);//强制用户选择
			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which){
					//将上次的单据改为完成
					salesOrderService.finishOrder(salesOrder);
					app.setCurrSalesOrder(null);
					//重新显示商品,为了消除标记为已在单据中的药品
					refreshSpkfk();
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		
		//商品名称助记码输入框
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
		
		//柜选择按钮
		final Button btnCabinet = (Button)findViewById(R.id.btnCabinet);
		btnCabinet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] cabinets = new CharSequence[24];
				cabinets[0] = "全柜";
				cabinets[1] = "1柜";
				cabinets[2] = "2柜";
				cabinets[3] = "3柜";
				cabinets[4] = "4柜";
				cabinets[5] = "5柜";
				cabinets[6] = "6柜";
				cabinets[7] = "7柜";
				cabinets[8] = "8柜";
				cabinets[9] = "9柜";
				cabinets[10] = "10柜";
				cabinets[11] = "11柜";
				cabinets[12] = "12柜";
				cabinets[13] = "13柜";
				cabinets[14] = "14柜";
				cabinets[15] = "A柜";
				cabinets[16] = "B柜";
				cabinets[17] = "C柜";
				cabinets[18] = "D柜";
				cabinets[19] = "E柜";
				cabinets[20] = "F柜";
				cabinets[21] = "冰箱";
				cabinets[22] = "里面";
				cabinets[23] = "其他";
				
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(cabinets, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String cabinet = cabinets[which].toString();
						btnCabinet.setText(cabinet);
						String tag;
						if("全柜".equals(cabinet)){
							tag = null;
						}else if("其他".equals(cabinet)){
							tag = "null";
						}else{
							tag = cabinet.replace("柜", "");
						}
						btnCabinet.setTag(tag);
						dialog.dismiss();
					}
				}).show();
			}
		});
	
		//价格选择按钮
		final Button btnPrice = (Button)findViewById(R.id.btnPrice);
		btnPrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] prices = new CharSequence[16];
				prices[0] = "全价";
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
				prices[15] = "100以上";
				
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(prices, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String price = prices[which].toString();
						btnPrice.setText(price);
						if("全价".equals(price)){
							btnPrice.setTag(null);
						}else{
							BigDecimal from;
							BigDecimal end;
							if("100以上".equals(price)){
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
		
		//药品类型选择按钮
		final Button btnType = (Button)findViewById(R.id.btnType);
		btnType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] types = new CharSequence[3];
				types[0] = "全类";
				types[1] = "中药";
				types[2] = "西药";
				
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
		//默认选中中药
		btnType.setText("中药");
		
		//查询按钮
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchSpkfk();
			}
		});
		
		//final ListView listViewSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		
		//当其他activity请求选择商品时,设置list item点击事件
		Intent externalIntent = getIntent();
		if(externalIntent != null && externalIntent.getFlags() == FLAG_FIND_SP){
			final String sptm = externalIntent.getStringExtra(CaptureActivity.EXTRA_NAME_SPTM);
			listViewSpkfk.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					//设置返回结果
					AdvSpkfk sp = (AdvSpkfk)listViewSpkfk.getItemAtPosition(position);
					sp.setSptm(sptm);
					Response r = spService.saveOrUpdate(sp);
					if(r.isOk()){
						Toast.makeText(SpkfkSelectActivity.this, "条码修改成功", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(SpkfkSelectActivity.this, "条码修改失败:" + r.getExceptionStr(), Toast.LENGTH_LONG).show();
					}
					finish();
				}
			});
		}

		//长按
		listViewSpkfk.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long id) {
				final CharSequence[] choices = new CharSequence[2];
				choices[0] = "查看详情";
				choices[1] = "查看销售单";
				ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
				final AdvSpkfk sp = (AdvSpkfk)lvSpkfk.getItemAtPosition(position);
				final int positionTmp= position;
				new AlertDialog.Builder(SpkfkSelectActivity.this).setTitle(null)
				.setItems(choices, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String choice = choices[which].toString();
						switch(which){
							case 0:
								//查看详情
								//启动商品详情activity
								Intent intentViewDetail = new Intent(SpkfkSelectActivity.this, SpkfkDetailActivity.class);
								intentViewDetail.putExtra(SpkfkDetailActivity.EXTRA_NAME_SPKFK, sp);
								intentViewDetail.putExtra(NumberInputActivity.TAG, "" + positionTmp);
								startActivityForResult(intentViewDetail, SpkfkSelectActivity.REQUEST_CODE_SPKFK_DETAIL);
								break;
							case 1:
								//显示销售单
								showOrderDetail();
								break;
							case 2:
								break;
						}
						Toast.makeText(SpkfkSelectActivity.this, "选择了 " + choice, Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				}).show();
				return false;
			}
		});
		
/*		//添加滑动页面
		viewFlipper.addView(spkfkView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		viewFlipper.addView(orderView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		viewFlipper.setAutoStart(false);*/
/*		viewFlipper.setAutoStart(true);// 设置自动播放功能（点击事件，前自动播放）
 		viewFlipper.setFlipInterval(3000);
		if(viewFlipper.isAutoStart() && !viewFlipper.isFlipping()){
			viewFlipper.startFlipping();
		}*/
		
		//初始化时查询一次
		searchSpkfk();
	}

	/**
	 * 添加到销售单按钮,SpkfkSelectListAdaper调用
	 * 
	 * @author caijun 2014-1-5
	 * @param position
	 */
	public void addToOrderBtnClick(final int position) {
		ListView lvSpkfk = (ListView) findViewById(R.id.listViewSpkfk);
		final AdvSpkfk sp = (AdvSpkfk) lvSpkfk.getItemAtPosition(position);
		// 添加到处方
		final CharSequence[] quantitys = new CharSequence[13];
		//频繁出现的有5\9\10\12\15\20\25\30\60
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
		quantitys[12] = "其他";
		//注意对话框设置了字体大小R.style.dialog
		Builder builder = new AlertDialog.Builder(SpkfkSelectActivity.this,R.style.dialog).setTitle(null)
				.setItems(quantitys, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 输入其他数量
						if (which == (quantitys.length - 1)) {
							// 弹出数字输入框
							Intent intent = new Intent(
									SpkfkSelectActivity.this,
									NumberInputActivity.class);
							String number = "";
							intent.putExtra(NumberInputActivity.NUMBER, number);
							intent.putExtra(NumberInputActivity.TAG, ""
									+ position);
							intent.putExtra(NumberInputActivity.DECIMAL_COUT, 2);// 设置最多只能输入两位小数,因为平安数据库中只存2位
							startActivityForResult(intent,
									REQUEST_CODE_GET_QUANTITY);
						} else {
							// 直接选择数量
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
	 * 添加到销售单
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
	 * 添加到销售单
	 * 
	 * @author caijun 2014-1-4
	 * @param sp
	 * @param pihao
	 * @param shl
	 */
	public void addItem(AdvSpkfk sp, String pihao, Double shl) {
		//当前无单据时
		if (salesOrder == null) {
			String orderNo = salesOrderService.generateOrderNo();
			salesOrder = new SalesOrder(orderNo);
			app.setCurrSalesOrder(salesOrder);
		}
		salesOrder.addItem(sp, null, shl);
		salesOrderService.saveOrUpdate(salesOrder);
		refreshSpkfk();//刷新,以标记已加入单据的商品
		Toast.makeText(SpkfkSelectActivity.this, "添加成功,数量:" + shl,
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
		//进入后不查询,直接滑动到单据界面,再滑动回来时,lvAdapter为null,因为还未设置过Adapter
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
		if ("中药".equals(type)) {
			isSelfCnSp = true;
		} else if ("西药".equals(type)) {
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
			// 接收加入销售单时的数量
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
			// 修改详细资料后返回
			AdvSpkfk sp = (AdvSpkfk) intent
					.getSerializableExtra(SpkfkDetailActivity.EXTRA_NAME_SPKFK);
			if (sp != null) {
				lvAdapter.setSpkfk(sp);
				refreshSpkfk();
			}
		}else if(requestCode == REQUEST_CODE_VIEW_ORDER){
			//重新显示商品,为了消除标记为已在单据中的药品
			refreshSpkfk();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event); 		// 注册手势事件
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e2.getX() - e1.getX() > 250) {			 // 从左向右滑动（左进右出）
/*			Animation rInAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_right_in); 	// 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
			Animation rOutAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）
			
			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			switchView = true;*/
		} else if (e2.getX() - e1.getX() < -250) {		 // 从右向左滑动（右进左出）
/*			Animation lInAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_left_in);		// 向左滑动左侧进入的渐变效果（alpha 0.1  -> 1.0）
			Animation lOutAnim = AnimationUtils.loadAnimation(SpkfkSelectActivity.this, R.anim.push_left_out); 	// 向左滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			switchView = true;*/
			//显示销售单
			showOrderDetail();
		}
		return true;
	}
	
	//显示销售单
	public void showOrderDetail(){
		//启动销售单activity
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
