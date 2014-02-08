package com.ajmst.android.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.commmon.util.DateTimeUtils;
import com.ajmst.common.response.Response;

import android.content.Context;

public class SalesOrderService extends BaseService<SalesOrder> {

	private SalesOrderItemService itemService;
	public SalesOrderService(Context context) {
		super(context);
		itemService = new SalesOrderItemService(context);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Response saveOrUpdate(SalesOrder order) {
		Response r = new Response();
		try {
			SalesOrder orderExist = this.getById(order.getOrderNo());
			List<SalesOrderItem> itemsToDel = new ArrayList<SalesOrderItem>();
			if (orderExist != null) {
				this.getDao().update(order);
				itemService.delete(orderExist.getItems());
			} else {
				this.getDao().create(order);
			}
			for(int i = 0; i < order.getItems().size(); i++){
				SalesOrderItem item = order.getItems().get(i);
				item.setSeq(i + 1);
				itemService.saveOrUpdate(item);
			}
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}
	
	@Override
	public SalesOrder getById(String orderNo){
		SalesOrder order = super.getById(orderNo);
		this.fetchItems(order);
		return order;
	}
	
	/**
	 * 给单据加上详细信息
	 * @author caijun 2014-1-6
	 * @param order
	 */
	private void fetchItems(SalesOrder order){
		if(order != null){
			List<SalesOrderItem> items = itemService.getByOrderNo(order.getOrderNo());
			order.setItems(items);
		}
	}
	
	public String generateOrderNo(){
		String orderNo = DateTimeUtils.formatDate(new Date(), "yyyy-MM-dd_HH:mm:ss");
		return orderNo;
	}
	
	/**
	 * 查询未完成的单据
	 * @author caijun 2014-1-6
	 * @return
	 */
	public SalesOrder getUnFinishedOrder(){
		SalesOrder order = null;
		try {
			order = (SalesOrder) this.getDao().queryBuilder().where().raw("state=" + SalesOrder.ORDER_STATE_UNFINISH + "").queryForFirst();
			this.fetchItems(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * 完成单据
	 * @author caijun 2014-1-6
	 * @param order
	 */
	public void finishOrder(SalesOrder order) {
		order.setState(SalesOrder.ORDER_STATE_FINISHED);
		order.setFinishTime(new Date());
		this.saveOrUpdate(order);
	}
	
	/**
	 * 计算总价
	 * @author caijun 2014-1-6
	 * @param order
	 * @return
	 */
	public static Double getOrderAmount(SalesOrder order){
		Double sum = 0.0;
		for(int i = 0; i < order.getItems().size(); i++){
			SalesOrderItem item = order.getItems().get(i);
			Double shl = item.getShl();
			Double lshj = item.getLshj();
			//中药的价格为10g的价格,需要进行转换
			if(SpkfkService.isSelfCnSp(item.getSpbh())){
				lshj = lshj / SpkfkService.SELF_CN_SP_UNIT_QUANTITY;
			}
			sum = sum + shl*lshj;
		}
		return sum;
	}
	
}
