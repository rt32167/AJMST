package com.ajmst.android.entity;

import com.ajmst.commmon.entity.AjmstGh;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class AdvAjmstGh extends AjmstGh{
	private static final long serialVersionUID = 8267693991418180137L;
	@DatabaseField(id=true)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
