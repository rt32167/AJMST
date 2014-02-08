package com.ajmst.android.service;

import java.sql.SQLException;
import java.util.List;

import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.common.response.Response;
import com.j256.ormlite.stmt.Where;

import android.content.Context;

public class SalesOrderItemService extends BaseService<SalesOrderItem> {

	public SalesOrderItemService(Context context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response saveOrUpdate(SalesOrderItem orderItem) {
		Response r = new Response();
		try {
			this.getDao().create(orderItem);
/*			SalesOrderItem orderItemExist = this.getOrderItem(orderItem.getOrderNo(),orderItem.getSpid(),orderItem.getPihao());
			if (orderItemExist != null) {
				this.getDao().update(orderItem);
			} else {
				this.getDao().create(orderItem);
			}*/
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}
	
/*	*//**
	 * ���ݵ��ź���ƷID�ҵ�Ψһ�����ۼ�¼
	 * @author caijun 2014-1-3
	 * @param orderNo ��Ϊnull
	 * @param spid ��Ϊnull
	 * @param pihao �ɸ�null,��ʾ��ѯpihaoΪnull�ļ�¼
	 * @return
	 *//*
	@SuppressWarnings("rawtypes")
	public SalesOrderItem getOrderItem(String orderNo,String spid,String pihao){
		SalesOrderItem item = null;
		try{
			 Where where = this.getDao().queryBuilder().where().raw("trim(orderNo)='" + orderNo.trim() + "'").and().raw("trim(spid)='" + spid.trim() + "'");
			 if(pihao == null){
				 where.isNull("pihao");
			 }else{
				 where.raw("trim(pihao)='" + pihao.trim() + "'");
			 }
			 item = (SalesOrderItem)where.queryForFirst();
		}catch(Exception e){
			e.printStackTrace();
		}
		return item;
	}*/

	/**
	 * ���ָ�����ŵ������Ӽ�¼
	 * @author caijun 2014-1-3
	 * @param orderNo
	 * @return
	 */
	public List<SalesOrderItem> getByOrderNo(String orderNo) {
		 try {
			return this.getDao().queryBuilder().orderBy("seq", true).where().raw("trim(orderNo)='" + orderNo + "'").query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
