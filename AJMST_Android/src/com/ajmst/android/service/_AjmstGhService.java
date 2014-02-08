package com.ajmst.android.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ajmst.android.entity.AdvAjmstGh;
import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.common.response.Response;

public class _AjmstGhService extends BaseService<AdvAjmstGh> {

	public _AjmstGhService(Context context) {
		super(context);
	}
	
	public AdvAjmstGh toAdvAjmstGh(AjmstGh ajmstGh){
		AdvAjmstGh advGh = new AdvAjmstGh();
		advGh.setId(ajmstGh.getSpbh().trim()+ "_" + ajmstGh.getGh().trim());
		advGh.setGh(ajmstGh.getGh());
		advGh.setSpbh(ajmstGh.getSpbh());
		advGh.setSpid(ajmstGh.getSpid());
		return advGh;
	}
	
	public List<AdvAjmstGh> toAdvAjmstGh(List<AjmstGh> ajmstGhs){
		List<AdvAjmstGh> advGhs = new ArrayList<AdvAjmstGh>();
		for(AjmstGh gh:ajmstGhs){
			advGhs.add(toAdvAjmstGh(gh));
		}
		return advGhs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Response saveOrUpdate(AdvAjmstGh ajmstGh) {
		Response r = new Response();
		try {
			AdvAjmstGh ghExist = getAjmstGh(ajmstGh.getSpbh(),ajmstGh.getGh());
			if (ghExist != null) {
				this.getDao().update(toAdvAjmstGh(ajmstGh));
			} else {
				this.getDao().create(toAdvAjmstGh(ajmstGh));
			}
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}
	
	public AdvAjmstGh getAjmstGh(String spbh,String gh){
		AdvAjmstGh ajmstGh = null;
		if(spbh != null){
			spbh = spbh.trim();
		}
		if(gh != null){
			gh = gh.trim();
		}
		try {
			List<String[]> results = this.getDao().queryRaw("select spbh,gh from " + AdvAjmstGh.class.getSimpleName() + " where trim(spbh)='" + spbh + "' and trim(gh)='" + gh + "'").getResults();
			if(results.size() > 0){
				String tmpSpbh = results.get(0)[0];
				String tmpGh = results.get(0)[1];
				ajmstGh = (AdvAjmstGh) this.getDao().queryBuilder().where().eq("spbh", tmpSpbh).and().eq("gh", tmpGh).queryForFirst();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ajmstGh;
	}

}
