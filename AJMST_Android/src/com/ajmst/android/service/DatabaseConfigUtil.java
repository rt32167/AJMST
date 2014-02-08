package com.ajmst.android.service;

import com.ajmst.android.entity.AdvAjmstGh;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.MsgQueue;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.commmon.entity.Spkfk;
import com.ajmst.commmon.util.BeanUtils;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	private static final Class<?>[] classes = new Class[] { AdvSpkfk.class,AjmstMaintain.class,SalesOrder.class,SalesOrderItem.class,MsgQueue.class};

	public static void main(String[] args) throws Exception {
		System.out.println("### begin...");
		writeConfigFile("ormlite_config.txt", classes);
		System.out.println("### end...");
		
	}
}
