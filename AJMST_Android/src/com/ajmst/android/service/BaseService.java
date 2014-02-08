package com.ajmst.android.service;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;

import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.MsgQueue;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;

import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.common.response.Response;
import com.j256.ormlite.dao.Dao;

public class BaseService<T> {
	private static DatabaseHelper dbHelper;
	private Dao dao;
	private Class<T> clazz;
	static List<Class> tableClasses;
	
	static{
		tableClasses = new ArrayList<Class>();
		tableClasses.add(AjmstMaintain.class);
		tableClasses.add(AdvSpkfk.class);
		tableClasses.add(SalesOrder.class);
		tableClasses.add(SalesOrderItem.class);
		tableClasses.add(MsgQueue.class);
	}
	
	public BaseService(Context context) {
		//得到泛型的类型
		clazz = (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if(dbHelper == null){
			dbHelper = new DatabaseHelper(context,tableClasses);
		}
		try {
			dao = this.getDbHelper().getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DatabaseHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(DatabaseHelper dbHelper) {
		BaseService.dbHelper = dbHelper;
	}
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Response saveOrUpdate(T obj){
		Response r = new Response();
		r.setIsOk(false);
		r.setException(new Exception("各个子类自己实现"));
		return r;
	}
	
	
	public Response saveOrUpdate(List<T> objs){
		Response r = new Response();
		for(T obj : objs){
			r = saveOrUpdate(obj);
			if(r.isOk() == false){
				break;
			}
		}
		return r;
	}
	
	public List<T> getAll(){
		List<T> objs = new ArrayList<T>();
		try {
			objs = this.getDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objs;
	}
	
	public void delete(T obj){
		try {
			this.getDao().delete(obj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Collection objs){
		try {
			this.getDao().delete(objs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int deleteAll(){
		int count = 0;
		try {
			count = this.getDao().deleteBuilder().delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 注意该方法只适合ID为单个字段的类
	 * @author caijun 2013-12-14
	 * @param id
	 * @return
	 */
	public T getById(String id){
		if(id != null){
			id = id.toString().trim();
		}
		try {
			return (T)this.getDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString(T obj){
		if(obj != null){
			return obj.toString();
		}
		return "";
	}
}
