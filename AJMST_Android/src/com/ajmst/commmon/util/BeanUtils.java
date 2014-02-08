package com.ajmst.commmon.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BeanUtils{
	 /*
	  * ���������е�����COPY�������С�
	  * �ඨ����childһ��Ҫextends father��
	  * ����child��fatherһ��Ϊ�ϸ�javabeanд��������ΪdeleteDate������ΪgetDeleteDate
	  */
	 public static void fatherToChild (Object father,Object child)throws Exception{
		 if(!(child.getClass().getSuperclass()==father.getClass())){
			 throw new Exception("child����father������");
		 }
		 Class fatherClass= father.getClass();
		 Class childClass = child.getClass();
		 Field ff[]= fatherClass.getDeclaredFields();
		 for(int i=0;i<ff.length;i++){
			 Field f=ff[i];//ȡ��ÿһ�����ԣ���deleteDate
			 Class type=f.getType();
			 String getMethodName = "get" + upperHeadChar(f.getName());
			 String setMethodName = "set" + upperHeadChar(f.getName());
			 Method getM = null;
			 Method setM = null;
			 
			 try{
				 getM = fatherClass.getMethod(getMethodName);//����getDeleteDate
			 }catch(NoSuchMethodException e){
				 //System.out.println("Warning: ignore not exist method named " + getMethodName);
			 }
			 if(getM != null){
				 getM.setAccessible(true);
				 Object value = getM.invoke(father);//ȡ������ֵ
				 try{
					 setM = fatherClass.getMethod(setMethodName,type);//ȡ����ķ�������,ע����Ҫ�������Ե�����
					 setM.setAccessible(true);
					 setM.invoke(child, value);
				 }catch(NoSuchMethodException e){
					 //System.out.println("Warning: ignore not exist method named " + setMethodName);
				 }
			 }


/*				 if(m.isAccessible()){

				 }else{
					 System.out.println("Warning: ignore not accessible method named " + methodName); 
				 }*/


			 	 
		 }
	 }
	 /**
	  * ����ĸ��д��in:deleteDate��out:DeleteDate
	  */
	 private static String upperHeadChar(String in){
		 String head=in.substring(0,1);
		 String out=head.toUpperCase()+in.substring(1,in.length());
		 return out;
	 }

}
