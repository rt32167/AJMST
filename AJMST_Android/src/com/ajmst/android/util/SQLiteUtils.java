package com.ajmst.android.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtils {
	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public static boolean isTableExist(SQLiteDatabase db, String tableName) {
		boolean result = false;
		if (db == null || tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
