package com.ajmst.android.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Workbook;
import android.content.Context;
import com.ajmst.commmon.entity.AjmstMaintain;
import com.ajmst.common.response.Response;
import com.ajmst.android.util.DateTimeUtils;
import com.ajmst.android.util.ExcelUtils;
import com.ajmst.android.util.StringUtils;
import com.j256.ormlite.stmt.QueryBuilder;

public class MaintainService extends BaseService<AjmstMaintain>{
/*	private static final String LOG_TAG = "MaintainDBService";
	private static final String TABLE_NAME_MAINTAIN = "maintain";
	private static final String DB_NAME = "ajmst.db";
	//context.getFilesDir().getAbsolutePath()
	private SQLiteDatabase db;
	private String dbPath;*/
	
	private static final int DEFAULT_SHEET_INDEX = 0;//excel��������ʱ,��ȡ�ڼ���worksheet
	
	public MaintainService(Context context) {
		super(context);
	}

	/**
	 * @author caijun 2013-12-14
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Response saveOrUpdate(AjmstMaintain obj) {
		Response r = new Response();
		try {
			AjmstMaintain objExist = this.getMaintainItem(obj.getSpbh(), obj.getPihao());
			if (objExist != null) {
				this.getDao().update(obj);
			} else {
				this.getDao().create(obj);
			}
		} catch (SQLException e) {
			r.setIsOk(false);
			r.setException(e);
		}
		return r;
	}

	/**
	 * ��ѯ����
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @return
	 */
	public List<AjmstMaintain> getMaintainItems() {
		return this.getMaintainItemsByGH("ȫ��");
/*		Log.d(LOG_TAG, "��ѯ��������");
		List<AjmstMaintain> maintainItems = new ArrayList<AjmstMaintain>();
		Cursor c = db.rawQuery("select * from "+ TABLE_NAME_MAINTAIN + " order by _id", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			AjmstMaintain maintainItem = cusorToMaintainItem(c);
			maintainItems.add(maintainItem);
			c.moveToNext();
		}
		return maintainItems;*/
	}
	
	/**
	 * ���ݽ����ϵĹ�Ų�ѯ��Ŀ
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @param gh
	 * @return
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<AjmstMaintain> getMaintainItemsByGH(String gh){
		List<AjmstMaintain> maintainItems = new ArrayList<AjmstMaintain>();
		try{
			gh = gh.replace("��", "").trim();
			QueryBuilder queryBuilder = this.getDao().queryBuilder();
			queryBuilder.orderBy("cabinetNo", true).orderBy("spmch", true);
			if(gh.equals("ȫ��")){
				
				//sql = "select * from "+ TABLE_NAME_MAINTAIN + " order by _id";
			}else if(gh.equals("δ���")){
					queryBuilder.where().isNull("shl");
				//sql = "select * from "+ TABLE_NAME_MAINTAIN + " where quantity is null order by _id";
			}else{
				queryBuilder.where().eq("cabinetNo", gh);
				//sql = "select * from "+ TABLE_NAME_MAINTAIN + " where trim(cabinetNo)='" + gh + "' order by _id";
			}
			maintainItems = queryBuilder.query();
		}catch(SQLException e){
			e.printStackTrace();
		}

		return maintainItems;
/*		String sql = null;
		List<AjmstMaintain> maintainItems = new ArrayList<AjmstMaintain>();
		gh = gh.replace("��", "").trim();
		if(gh.equals("ȫ��")){
			sql = "select * from "+ TABLE_NAME_MAINTAIN + " order by _id";
		}else if(gh.equals("δ���")){
			sql = "select * from "+ TABLE_NAME_MAINTAIN + " where quantity is null order by _id";
		}else{
			sql = "select * from "+ TABLE_NAME_MAINTAIN + " where trim(cabinetNo)='" + gh + "' order by _id";
		}
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			AjmstMaintain maintainItem = cusorToMaintainItem(c);
			maintainItems.add(maintainItem);
			c.moveToNext();
		}
		return maintainItems;*/
	}
	
	/**
	 * 
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @param name
	 * @param batchcode
	 * @return
	 */
	public AjmstMaintain getMaintainItem(String name,String batchcode) {
		AjmstMaintain mt = null;
		try {
			mt = (AjmstMaintain) this.getDao().queryBuilder().where().eq("spbh", name).and().eq("pihao", batchcode).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mt;
/*		Log.d(LOG_TAG, "��ѯ��������");
		AjmstMaintain maintainItem = null;
		Cursor c = db.rawQuery("select * from "+ TABLE_NAME_MAINTAIN + " where name='" + name + "' and batchcode=" + batchcode +"'", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			maintainItem = cusorToMaintainItem(c);
		}
		return maintainItem;*/
	}
	
	/**
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @param maintainItem
	 * @return
	 */
	public boolean updateQuantity(AjmstMaintain maintainItem){
		Response r = this.saveOrUpdate(maintainItem);
		return r.isOk();
/*		String name = maintainItem.getSpbh();
		String batchcode = maintainItem.getPihao();
		Double quantity = maintainItem.getShl();
		
		ContentValues values =new ContentValues();
		values.put("quantity", quantity);
		int result = db.update(TABLE_NAME_MAINTAIN, values, "name=? and batchcode=?", new String[]{name,batchcode});
		if(result > 0){
			Log.d(LOG_TAG, "��������:" + quantity);
			return true;
		}else{
			return false;
		}*/
	}
	
/*	private AjmstMaintain cusorToMaintainItem(Cursor c){
		String spid = c.getString(c.getColumnIndex("commodityID"));
		Date maintainDate = DateTimeUtils.parseDate(c.getString(c.getColumnIndex("maintainDate")), "yyyy-MM-dd");//ֻȡ����,����ʱ�������
		String name = c.getString(c.getColumnIndex("name"));
		String desc = c.getString(c.getColumnIndex("description"));
		String factory = c.getString(c.getColumnIndex("factory"));
		String specification = c.getString(c.getColumnIndex("specification"));
		String unit = c.getString(c.getColumnIndex("unit"));
		String batchcode = c.getString(c.getColumnIndex("batchcode"));
		Double quantity = null;
		if(c.getString(c.getColumnIndex("quantity")) != null){
			quantity = c.getDouble(c.getColumnIndex("quantity"));
		}
		Double suggestQuantity = null;
		if(c.getString(c.getColumnIndex("suggestQuantity")) != null){
			suggestQuantity = c.getDouble(c.getColumnIndex("suggestQuantity"));
		}
		String cabinetNo = c.getString(c.getColumnIndex("cabinetNo"));
		String zjm = c.getString(c.getColumnIndex("mnemonicCode"));
		Date createTime = DateTimeUtils.parseDate(c.getString(c.getColumnIndex("createTime")), "yyyy-MM-dd HH:mm:ss");
		
		AjmstMaintain maintainItem = new AjmstMaintain();
		AjmstMaintainId id = new AjmstMaintainId();
		id.setSpid(spid);
		id.setPihao(batchcode);
		id.setMaintainDate(maintainDate);
		maintainItem.setId(id);
		maintainItem.setSpid(spid);
		maintainItem.setPihao(batchcode);
		maintainItem.setMaintainDate(maintainDate);
		maintainItem.setSpbh(name);
		maintainItem.setSpmch(desc);
		maintainItem.setShengccj(factory);
		maintainItem.setShpgg(specification);
		maintainItem.setDw(unit);
		maintainItem.setCabinetNo(cabinetNo);
		maintainItem.setSuggestQuantity(suggestQuantity);
		maintainItem.setShl(quantity);
		maintainItem.setZjm(zjm);
		maintainItem.setCreateTime(createTime);
		return maintainItem;
	}*/

	/**
	 * 
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @param maintainItem
	 * @return
	 */
	public boolean create(AjmstMaintain maintainItem) {
		Response r =this.saveOrUpdate(maintainItem);
		return r.isOk();
/*		ContentValues values =new ContentValues();
		//maintainDate,commodityID
		values.put("commodityID", maintainItem.getSpid());
		values.put("batchcode", maintainItem.getPihao());
		values.put("maintainDate", DateTimeUtils.formatDate(maintainItem.getMaintainDate(), "yyyy-MM-dd"));
		values.put("name", maintainItem.getSpbh());
		values.put("description", maintainItem.getSpmch());
		values.put("factory", maintainItem.getShengccj());
		values.put("specification", maintainItem.getShpgg());
		values.put("unit", maintainItem.getDw());
		values.put("quantity", maintainItem.getShl());
		values.put("suggestQuantity", maintainItem.getSuggestQuantity());
		values.put("cabinetNo", maintainItem.getCabinetNo());
		values.put("mnemonicCode", maintainItem.getZjm());
		values.put("createTime", DateTimeUtils.formatDate(maintainItem.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		if(db.insert(TABLE_NAME_MAINTAIN, "_id", values) == -1){
			return false;
		}
		return true;*/
	}
	
	public boolean initData(String path){
		boolean result = false;
		Workbook wb = null;
		try {
			InputStream is = new FileInputStream(path);
			wb = Workbook.getWorkbook(is);
			// wb = Workbook.getWorkbook(new File(path));
			List<List<String>> data = ExcelUtils.getData(wb,
					DEFAULT_SHEET_INDEX);

			clearData();
			
			for (int r = 1; r < data.size(); r++) {
				List<String> rowData = data.get(r);
				AjmstMaintain maintainItem = new AjmstMaintain();
				String spID = rowData.get(0);
				if(spID == null || "".equals(spID)){
					break;
				}
				String desc = rowData.get(1);
				String batchcode = rowData.get(2);
				Double suggestQuantity = Double.valueOf(StringUtils
						.stringtodouble(rowData.get(3)));
				Double quantity = null;
				if (rowData.get(4) != null
						&& "".equals(rowData.get(4).trim()) == false) {
					quantity = StringUtils.stringtodouble(rowData.get(4));
				}
				String cabinetNo = rowData.get(5);
				String factory = rowData.get(6);
				String specification = rowData.get(7);
				String unit = rowData.get(8);
				String spid = rowData.get(13);
				Date maintainDate = DateTimeUtils.parseDate(rowData.get(14), "yyyy-MM-dd");
				String zjm = rowData.get(15);
				if (spID != null && "".equals(spID) == false) {
/*					AjmstMaintainId id = new AjmstMaintainId();
					id.setSpid(spid);
					id.setPihao(batchcode);
					id.setMaintainDate(maintainDate);
					maintainItem.setId(id);*/
					maintainItem.setSpid(spid);
					maintainItem.setPihao(batchcode);
					maintainItem.setMaintainDate(maintainDate);
					maintainItem.setSpbh(spID);
					maintainItem.setSpmch(desc);
					maintainItem.setShengccj(factory);
					maintainItem.setShpgg(specification);
					maintainItem.setDw(unit);
					maintainItem.setCabinetNo(cabinetNo);
					maintainItem.setSuggestQuantity(suggestQuantity);
					maintainItem.setShl(quantity);
					maintainItem.setZjm(zjm);
					maintainItem.setCreateTime(new Date());
					this.create(maintainItem);
				}
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
/*	private void createTable(){
		db.execSQL("create table " + TABLE_NAME_MAINTAIN + "(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"commodityID TEXT," +
				"name TEXT," +
				"description TEXT," +
				"mnemonicCode TEXT," +
				"factory TEXT," +
				"specification TEXT," +
				"unit TEXT," +
				"batchcode TEXT," +
				"quantity REAL," +
				"suggestQuantity REAL," +
				"cabinetNo TEXT," +
				"maintainDate TEXT," +
				"createTime TEXT" +
				")");
		Log.d(LOG_TAG, "������");
	}*/
	
	/**
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @return
	 */
	public int clearData(){
		return this.deleteAll();
/*		return db.delete(TABLE_NAME_MAINTAIN, "1=1", null);*/
	}
	
/*	public int getDataNum(){
		Log.d(LOG_TAG, "��ѯ���ݸ���");
		Cursor c = db.rawQuery("select * from "+ TABLE_NAME_MAINTAIN + "", null);
		return c.getCount();
	}*/

/*	
	*//**
	 * @deprecated ���ȶ�,������office2003���ϵ�excel,���������ļ�����
	 * @param path
	 * @throws Exception
	 *//*
	public void exportDataToExcel(String path) throws Exception{
		List<AjmstMaintain> maintainItems = this.getMaintainItems();
		Workbook wb = null;
		String info = "";
		try {
			Log.d(this.getClass().getName(), "��ȡ�ļ�");
			InputStream is = new FileInputStream(path);
			wb = Workbook.getWorkbook(is);
			List<List<String>> excelData = ExcelUtils.getData(wb);
			
			//����֤�Ƿ���Ŀ��ͬ
			boolean isSameItem = true;
			
			if(maintainItems.size() == excelData.size() - 1){
				for(int i= 0; i < maintainItems.size(); i++){
					AjmstMaintain maintainItem = maintainItems.get(i);
					List<String> rowData = excelData.get(i+1);
					if( maintainItem.getSpbh().trim().equals(rowData.get(0).trim()) == false || maintainItem.getPihao().trim().equals(rowData.get(2).trim()) == false ){
						info = "���ݿ��е�" + i + "����¼��Excel�е� " + (i+1) + " �в�ͬ: " + maintainItem.getSpbh() + "," + maintainItem.getPihao() 
								+ " : " + rowData.get(0) + "," + rowData.get(2);
						isSameItem = false;
						break;
					}
				}
			}else{
				info = "��Ŀ������ͬ,���ݿ���Ϊ" + maintainItems.size() + ",Excel��Ϊ"+ (excelData.size() - 1);
			}
			
			if(isSameItem == true){				
				// ����workbook�ĸ���
				WritableWorkbook wbe = Workbook.createWorkbook(new File(path), wb);
				// ��ȡ��һ��������
				WritableSheet sheet = wbe.getSheet(DEFAULT_SHEET_INDEX);
				
				for(int i= 0; i < maintainItems.size(); i++){
					
					AjmstMaintain maintainItem = maintainItems.get(i);
					info = "���ڵ�����" + (i + 1) + "��:" + maintainItem.getSpbh() + "," + maintainItem.getSpmch();
					ExcelUtils.setCell(sheet, i+1, 4, maintainItem.getShl());
					Log.d(this.getClass().getName(), "����ҩƷ���:" + maintainItem.getSpbh() + ",����:" + maintainItem.getPihao() + ",����:" + maintainItem.getShl());
				}
				
				Log.d(this.getClass().getName(), "д�ļ�");
				wbe.write();
				Log.d(this.getClass().getName(), "�ر��ļ�");
				wbe.close();
				Log.d(this.getClass().getName(), "�����ɹ�");
				
			}else{
				throw new Exception(info);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(info + ",�����쳣:" + e.getMessage());
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	
	
	
	
/*	
	*//**
	 * �������ݿ��ļ���ָ��λ��(�����ļ���)
	 * @param targetPath
	 *//*
	public void exportDBFile(String targetPath)throws Exception{
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			File srcFile = new File(dbPath);
			File targetFile = new File(targetPath);
			targetFile.createNewFile();
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(targetFile);
			inChannel = fis.getChannel();
	        outChannel = fos.getChannel();
	        inChannel.transferTo(0, inChannel.size(), outChannel);
		}catch(Exception e){
			throw(e);
		}finally {
        	try {
				inChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
				outChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}*/
	
	/**
	 * ��������Ϊ�յ�������Ŀ
	 * @author caijun 2013-10-15
	 * ��Ϊ����ormlite��ʵ��,20131214,cj
	 * @return
	 */
	public List<AjmstMaintain> getNoQuantityItems(){
		return getMaintainItemsByGH("δ���");
/*		List<AjmstMaintain> maintainItems = new ArrayList<AjmstMaintain>();
		String sql = "select * from "+ TABLE_NAME_MAINTAIN + " where quantity is null order by _id";
		
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			AjmstMaintain maintainItem = cusorToMaintainItem(c);
			maintainItems.add(maintainItem);
			c.moveToNext();
		}
		return maintainItems;*/
	}
	
}
