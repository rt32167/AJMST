package com.ajmst.commmon.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	public static final String EMPTY = "";

	/**
	 * 
	 * @param num
	 * @return �Ƿ��ת��Ϊint
	 */
	public static boolean isInt(String num) {
		try {
			Integer.valueOf(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param num
	 * @return �Ƿ��ת��Ϊlong
	 */
	public static boolean isLong(String num) {
		try {
			Long.valueOf(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param num
	 * @return �Ƿ��ת��Ϊdouble
	 */
	public static boolean isDouble(String num) {
		try {
			Double.valueOf(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * <p>
	 * ���ַ���Ϊnull��ת����empty,ȥ���ַ���ǰ��Ŀո��ַ���
	 * </p>
	 * 
	 * @see java.lang.String#trim()
	 * @param str
	 *            the String to clean, may be null
	 * @return the trimmed text, never <code>null</code>
	 */
	public static String trimToEmpty(String str) {
		return (str == null ? EMPTY : str.trim());
	}

	public static String trimToEmpty(Object str) {
		return (str == null ? EMPTY : (str.toString()).trim());
	}

	/**
	 * <p>
	 * ���ַ���Ϊnull��ת����ָ�����ַ�����
	 * </p>
	 * 
	 * @see java.lang.String#trim()
	 * @param str
	 *            the String to clean, may be null
	 * @return the specified text, never <code>null</code>
	 */
	public static String trimToValue(String str, String value) {
		return (str == null ? value : str.trim());
	}
	
	/**
	 * <p>
	 * ���ַ���ת����int
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return 0 if the String is empty or null
	 */
	public static int stringtoint(String str) {
		int i = 0;
		try {
			i = (new Double(str)).intValue();// ;Integer.parseInt(str);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * <p>
	 * ���ַ���ת����Long
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return 0 if the String is empty or null
	 */
	public static long stringtolong(String str) {
		long i = 0;
		try {
			i = (new Double(str)).longValue();
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * <p>
	 * ���ַ���ת����Long
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return 0 if the String is empty or null
	 */
	public static long stringtolong(Object str) {
		long i = 0;
		try {
			i = (new Double(str.toString())).longValue();
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * <p>
	 * ���ַ���ת����Long
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return returnIfExeption if the String is empty or null
	 */
	public static Long stringtolong(Object str,Long returnIfExeption) {
		try {
			return (new Double(str.toString())).longValue();
		} catch (Exception e) {
			return returnIfExeption;
		}
	}

	/**
	 * <p>
	 * ���ַ���ת����double
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return 0 if the String is empty or null
	 */
	public static double stringtodouble(String str) {
		double d = 0.0;
		try {
			d = Double.parseDouble(str);
		} catch (Exception e) {
			d = 0.0;
		}
		return d;
	}

	/**
	 * ���ַ������̶�����ƽ����
	 * @param str
	 * @param maxSubLen �ָ�ɵ��ַ�������󳤶�
	 * @return ����ı��ָ����ַ�������
	 */
	public static List<String> breakStrToMulti(String str, int maxSubLen) {
		int remainLen = str.length();
		List<String> subStrs = new ArrayList<String>();
		while (remainLen >= maxSubLen) {
			String subStr = str.substring(0, maxSubLen);
			str = str.substring(maxSubLen);
			remainLen = remainLen - maxSubLen;
			subStrs.add(subStr);
		}
		if ("".equals(str) == false) {
			subStrs.add(str);
		}
		return subStrs;
	}
	
	/**
	 * ������ַ����÷ָ�������,��"1","2","3" �÷ָ���","��װ�η�"'"���Ӻ�Ϊ"'1','2','3'";
	 * ����:����sql���ʱ,��Ҫ��������"where column in ('1','2','3')"�����
	 * @param objs
	 * @param tag �ָ���
	 * @return
	 */
	public static String joinStringsWith(java.util.List<String> objs,String decoration,String split){
		String str = "";
		for(int i = 0; i < objs.size(); i ++){
			if(i == 0){
				str += decoration + objs.get(i) + decoration;
			}else{
				str += split + decoration + objs.get(i) + decoration;
			}
		}
		return str;
	}

	// =====================================================================================
	// =================================����Ϊ�����÷���==========================================
	// ======================================================================================

	/**
	 * @deprecated ������punt �е� stringToBigDecimal()
	 * @param str
	 * @return
	 */
	public static BigDecimal stringtoBigDecimal(String str) {
		if (str == null)
			return null;
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * ���һ���ַ����Ƿ�Ϊ�ա�
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * <p>
	 * ���һ���ַ����Ƿ�Ϊ�գ�����Ϊ�հ��ַ���
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}



	/**
	 * <p>
	 * ת��ҳ����ʾ��ʽ����������ַ�Ϊ�գ�����HTML�Ŀ���&nbsp;��ʾ
	 * </p>
	 * 
	 */
	public static String changeShowFormat(String str) {
		return (str.equals("") ? "&nbsp;" : str);
	}

	/**
	 * <p>
	 * ������str�г���sub�Ĵ���
	 * </p>
	 */
	public static int countMatches(String str, String sub) {
		if (str == null || str.length() == 0 || sub == null
				|| sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	/**
	 * <p>
	 * ��һ���ַ���ǰ�����ַ�0��ʹ������ַ���Ϊ�ض��ĳ���
	 * </p>
	 * putZero("236",8) = "00000236"
	 * 
	 * @param str
	 *            �����ַ���
	 * @param lenth
	 *            ����ַ����ĳ���
	 * @return �����ַ���
	 */
	public static String putZero(String str, int length) {
		String allZero = "";
		for (int i = 0; i < length; i++) {
			allZero += "0";
		}

		if (str == null || str.length() == 0)
			return allZero;

		if (str.length() > length)
			return str;

		return allZero.substring(0, length - str.length()) + str;
	}

	/**
	 * <p>
	 * ��һ���ַ���ǰ�����ַ������ض����ַ���
	 * </p>
	 * putChar("236",8,"0",0) = "00000236" putChar("236",8,"0",1) = "23600000"
	 * 
	 * @param str
	 *            �����ַ���
	 * @param totalLenth
	 *            ����ַ����ĳ���
	 * @param suffix
	 *            ��������ַ�
	 * @param pos
	 *            0 ��ʶ�������ַ���ǰ����1��ʶ�������ַ����󲹡�
	 * @return �����ַ���
	 */
	public static String putChar(String str, int totalLength, String suffix,
			int pos) {
		String totalLengthStr = "";
		for (int i = 0; i < totalLength; i++) {
			totalLengthStr += suffix;
		}

		if (str == null || str.length() == 0)
			return totalLengthStr;

		if (str.length() > totalLength)
			return str;

		String returnValue = totalLengthStr;
		if (pos == 0)
			returnValue = totalLengthStr.substring(0,
					totalLength - str.length())
					+ str;
		if (pos == 1)
			returnValue = str
					+ totalLengthStr.substring(0, totalLength - str.length());
		return returnValue;
	}

	/**
	 * ��ȡһ���ַ��ĳ���(�����ա������ַ�����Ϊ2),��������Ӣ��,������ֲ����ã�����ȡһ���ַ�λ
	 * 
	 * @param str
	 *            ԭʼ�ַ���
	 * @param specialCharsLength
	 *            ��ȡ����(�����ա������ַ�����Ϊ2)
	 * @return
	 */
	public static String trim(String str, int specialCharsLength) {
		if (str == null || "".equals(str) || specialCharsLength < 1) {
			return "";
		}
		char[] chars = str.toCharArray();
		int charsLength = getCharsLength(chars, specialCharsLength);
		return new String(chars, 0, charsLength);
	}

	public static int getStringLength(String str) {
		return getCharsLength(str.toCharArray());
	}

	/**
	 * ��ȡһ���ַ��ĳ��ȣ����볤���к����ա������ַ�����Ϊ2����������������ַ�������Ϊ1
	 * 
	 * @param chars
	 *            һ���ַ�
	 * @return ������ȣ����볤�ȣ������ա������ַ�����Ϊ2
	 */
	public static int getCharsLength(char[] chars) {
		int count = 0;
		for (int i = 0; i < chars.length; i++) {
			int specialCharLength = getSpecialCharLength(chars[i]);
			count += specialCharLength;
		}
		return count;
	}

	/**
	 * ��ȡһ���ַ��ĳ��ȣ����볤���к����ա������ַ�����Ϊ2����������������ַ�������Ϊ1
	 * 
	 * @param chars
	 *            һ���ַ�
	 * @param specialCharsLength
	 *            ���볤�ȣ������ա������ַ�����Ϊ2
	 * @return ������ȣ������ַ�������Ϊ1
	 */
	public static int getCharsLength(char[] chars, int specialCharsLength) {
		int count = 0;
		int normalCharsLength = 0;
		for (int i = 0; i < chars.length; i++) {
			int specialCharLength = getSpecialCharLength(chars[i]);
			if (count <= specialCharsLength - specialCharLength) {
				count += specialCharLength;
				normalCharsLength++;
			} else {
				break;
			}
		}
		return normalCharsLength;
	}

	/**
	 * ��ȡ�ַ����ȣ������ա������ַ�����Ϊ2��ASCII����ַ�����Ϊ1
	 * 
	 * @param c
	 *            �ַ�
	 * @return �ַ�����
	 */
	public static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * �ж�һ���ַ���Ascill�ַ����������ַ����纺���գ������ַ���
	 * 
	 * @param char c, ��Ҫ�жϵ��ַ�
	 * @return boolean, ����true,Ascill�ַ�
	 */
	private static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * ��source�����str1��str2�滻
	 */
	public static String replaceAll(String Source, String str1, String str2) {
		String rtnStr = "";
		int idx = Source.indexOf(str1);
		while (idx > -1) {
			String part1 = Source.substring(0, idx);
			String part2 = Source.substring(idx + str1.length());
			rtnStr = rtnStr + part1 + str2;
			Source = part2;
			idx = part2.indexOf(str1);
		}
		rtnStr = rtnStr + Source;
		return rtnStr;
	}

}
