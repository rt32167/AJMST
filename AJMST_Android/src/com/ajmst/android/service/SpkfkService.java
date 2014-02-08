package com.ajmst.android.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.commmon.entity.Spkfk;
import com.ajmst.commmon.util.BeanUtils;
import com.ajmst.common.response.Response;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class SpkfkService extends BaseService<AdvSpkfk> {
	private final static int MAX_SELECT_ROW_NUM = 100;//每次查询最大数量
	//private final static String LOG_TAG = SpkfkService.class.getSimpleName();
	//本店维护中药的商品编号前缀,系统以此判断是否为中药,且计算处方时,中药的价格为10g的价格,需要进行转换
	public final static String SELF_CN_SP_SPBH_PRE = "8";
	public final static int SELF_CN_SP_UNIT_QUANTITY = 10;//本店维护中药的商品存储的价格是10g的价格
	
	public SpkfkService(Context context) {
		super(context);
	}
	
	/**
	 * 不包含柜号信息
	 * @author caijun 2013-12-19
	 * @param sp
	 * @return
	 * @throws Exception 
	 */
	public AdvSpkfk toAdvSpkfk(Spkfk sp) throws Exception{
		AdvSpkfk advSp = new AdvSpkfk();
		BeanUtils.fatherToChild(sp, advSp);
		return advSp;
	}
	
	/**
	 * 不包含柜号信息
	 * @author caijun 2013-12-19
	 * @param sps
	 * @return
	 */
	public List<AdvSpkfk> toAdvSpkfk(List<Spkfk> sps)throws Exception{
		List<AdvSpkfk> advSps = new ArrayList<AdvSpkfk>();
		for(Spkfk sp:sps){
			advSps.add(toAdvSpkfk(sp));
		}
		return advSps;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response saveOrUpdate(AdvSpkfk sp) {
		Response r = new Response();
		try {
			AdvSpkfk spExist = this.getById(sp.getSpid());
			if (spExist != null) {
				this.getDao().update(sp);
			} else {
				this.getDao().create(sp);
			}
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}

	public AdvSpkfk getByBarcode(String barcode) {
		if (barcode != null) {
			barcode = barcode.trim();
		}
		
		AdvSpkfk sp = null;
		try {
/*			List<String[]> results = this.getDao().queryRaw("select spid from AdvSpkfk where trim(sptm)='" + barcode + "'").getResults();
			if(results.size() > 0){
				sp = this.getById(results.get(0)[0]);
			}*/
			sp = (AdvSpkfk) this.getDao().queryBuilder().where().raw("trim(sptm)='" + barcode + "'").queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sp;
	}

	/**
	 * 得到自定义的中药列表
	 * 
	 * @author caijun 2013-12-14
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AdvSpkfk> getSelfCnSp() {
		List<AdvSpkfk> sps = new ArrayList<AdvSpkfk>();
		try {
			sps = this.getDao().queryBuilder().orderBy("spbh", true).where()
					.like("spbh", "8%").query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sps;
	}
	
	public Response updateCabinet(List<AjmstGh> ghs){
		Response r = new Response();
		for(AjmstGh gh : ghs){
			r = this.updateCabinet(gh);
			if(!r.isOk()){
				break;
			}
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	public Response updateCabinet(AjmstGh gh){
		Response r = new Response();
		String spid = gh.getSpid();
		String spbh = gh.getSpbh();
		String cabinet = gh.getGh();
		AdvSpkfk sp = null;
		if(spid != null && !"".equals(spid.trim())){
			sp = this.getById(spid);
		}else{
			sp = this.getBySpbh(spbh);
		}
		if(sp != null){
			sp.setGh(cabinet);
			try {
				this.getDao().update(sp);
			} catch (SQLException e) {
				r.setException(e);
			}
		}else{
			r.setException(new Exception("No such spkfk,spid:" + spid + ",spbh:" + spbh));
		}
		return r;
	}
	
	public AdvSpkfk getBySpbh(String spbh){
		AdvSpkfk sp = null;
		if(spbh != null){
			spbh = spbh.trim();
		}
		try {
			sp = (AdvSpkfk) this.getDao().queryBuilder().where().raw("trim(spbh)='" + spbh + "'").queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sp;
	}

	@Override
	public String toString(AdvSpkfk sp) {
		StringBuilder sb = new StringBuilder();
		sb.append(sp.getSpbh().trim()).append("\n")
				.append(sp.getZjm()).append("\n")
				.append(sp.getSpmch().trim()).append("\n")
				.append("规格:").append(sp.getShpgg())
				.append(", 单位:").append(sp.getDw()).append("\n")
				.append(sp.getShengccj()).append("\n")
				.append("售价:").append(sp.getLshj());
		return sb.toString();
	}
	
	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public List<AdvSpkfk> query(String zjm,String cabinet,BigDecimal priceFrom,BigDecimal priceEnd,Boolean isSelfCnSp){
		List<AdvSpkfk> sps = new ArrayList<AdvSpkfk>();
		try {
			QueryBuilder queryBuilder = this.getDao().queryBuilder();
			Where where = queryBuilder.where().isNotNull("spid");
			if(zjm != null && "".equals(zjm) == false){
				zjm = zjm.toUpperCase();
				where.and().like("zjm", "%"+zjm+"%");
			}
			if(cabinet != null){
				if("NULL".equalsIgnoreCase(cabinet)){
					where.and().isNull("gh");
				}else{
					cabinet = cabinet.trim();
					where = where.and().raw("trim(gh)='" + cabinet + "'");
				}
			}
			if(priceFrom != null){
				where.and().raw("CAST(lshj as double) >=" + priceFrom);//注意bigdecimal在sqlite中存的是字符串,需转换后再比较,不能直接用eq,le,ge等方法
			}
			if(priceEnd != null){
				 where.and().raw("CAST(lshj as double) <=" + priceEnd);
			}
			if(isSelfCnSp != null){
				if(isSelfCnSp){
					where.and().raw("trim(spbh) like'" + SELF_CN_SP_SPBH_PRE + "%'");
				}else{
					where.and().raw("trim(spbh) not like'" + SELF_CN_SP_SPBH_PRE + "%'");
				}
			}
			String sql = where.getStatement();
			System.out.println(sql);
			sps = queryBuilder.orderBy("gh", false).limit(MAX_SELECT_ROW_NUM).query();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sps;
	}
	
	/**
	 * 是否为本店维护中药
	 * @author caijun 2014-1-6
	 * @param spbh
	 * @return
	 */
	public static boolean isSelfCnSp(String spbh){
		boolean isSelf = false;
		if(spbh.startsWith(SELF_CN_SP_SPBH_PRE)){
			isSelf = true;
		}
		return isSelf;
	}
	
	/**
	 * 是否为本店维护中药
	 * @author caijun 2014-1-6
	 * @param spbh
	 * @return
	 */
	public static boolean isSelfCnSp(AdvSpkfk sp){
		return isSelfCnSp(sp.getSpbh());
	}
}
