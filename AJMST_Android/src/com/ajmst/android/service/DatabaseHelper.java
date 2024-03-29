package com.ajmst.android.service;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.ajmst.android.R;
import com.ajmst.android.entity.AdvAjmstGh;
import com.ajmst.android.entity.AdvSpkfk;
import com.ajmst.android.entity.MsgQueue;
import com.ajmst.android.entity.SalesOrder;
import com.ajmst.android.entity.SalesOrderItem;
import com.ajmst.commmon.entity.AjmstGh;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.commmon.entity.Spkfk;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private List<Class> tableClasses;
	// name of the database file for your application -- change to something appropriate for your app
	private final static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/AJMST.db";//��ͬ"/sdcard/AJMST.db";
	//private static final String DATABASE_NAME = "helloAndroid.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 14;

	// the DAO object we use to access the SimpleData table
	//private Dao<T, Integer> simpleDao = null;
	//private RuntimeExceptionDao<T, Integer> simpleRuntimeDao = null;

	public DatabaseHelper(Context context,List<Class> tableClasses) {
		super(context, DB_PATH, null, DATABASE_VERSION, R.raw.ormlite_config);
		this.tableClasses = tableClasses;
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			for(Class c : tableClasses){
				TableUtils.createTable(connectionSource, c);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.createTable(connectionSource, MsgQueue.class);
			// after we drop the old databases, we create the new ones
			//onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "onUpgrade databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}
}