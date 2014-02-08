package com.ajmst.commmon.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BeanUtils{
	 /*
	  * 将父类所有的属性COPY到子类中。
	  * 类定义中child一定要extends father；
	  * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate
	  */
	 public static void fatherToChild (Object father,Object child)throws Exception{
		 if(!(child.getClass().getSuperclass()==father.getClass())){
			 throw new Exception("child不是father的子类");
		 }
		 Class fatherClass= father.getClass();
		 Class childClass = child.getClass();
		 Field ff[]= fatherClass.getDeclaredFields();
		 for(int i=0;i<ff.length;i++){
			 Field f=ff[i];//取出每一个属性，如deleteDate
			 Class type=f.getType();
			 String getMethodName = "get" + upperHeadChar(f.getName());
			 String setMethodName = "set" + upperHeadChar(f.getName());
			 Method getM = null;
			 Method setM = null;
			 
			 try{
				 getM = fatherClass.getMethod(getMethodName);//方法getDeleteDate
			 }catch(NoSuchMethodException e){
				 //System.out.println("Warning: ignore not exist method named " + getMethodName);
			 }
			 if(getM != null){
				 getM.setAccessible(true);
				 Object value = getM.invoke(father);//取出属性值
				 try{
					 setM = fatherClass.getMethod(setMethodName,type);//取父类的方法即可,注意需要传入属性的类型
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
	  * 首字母大写，in:deleteDate，out:DeleteDate
	  */
	 private static String upperHeadChar(String in){
		 String head=in.substring(0,1);
		 String out=head.toUpperCase()+in.substring(1,in.length());
		 return out;
	 }

}
