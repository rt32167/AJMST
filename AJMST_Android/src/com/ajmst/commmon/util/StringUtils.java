package com.ajmst.commmon.util;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	public static final String EMPTY = "";

	/**
	 * 
	 * @param num
	 * @return 是否可转换为int
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
	 * @return 是否可转换为long
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
	 * @return 是否可转换为double
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
	 * 将字符串为null的转换成empty,去掉字符串前后的空格字符。
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
	 * 将字符串为null的转换成指定的字符串。
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
	 * 将字符串转换成int
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
	 * 将字符串转换成Long
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
	 * 将字符串转换成Long
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
	 * 将字符串转换成Long
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
	 * 将字符串转换成double
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
	 * 将字符串按固定长度平均分
	 * @param str
	 * @param maxSubLen 分割成的字符床的最大长度
	 * @return 有序的被分割后的字符串集合
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
	 * 将多个字符串用分隔符连接,如"1","2","3" 用分隔符","和装饰符"'"连接后为"'1','2','3'";
	 * 例如:生成sql语句时,需要生成类似"where column in ('1','2','3')"的语句
	 * @param objs
	 * @param tag 分隔符
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
	// =================================以下为不常用方法==========================================
	// ======================================================================================

	/**
	 * @deprecated 建议用punt 中的 stringToBigDecimal()
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
	 * 检查一个字符串是否为空。
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
	 * 检查一个字符串是否为空，或者为空白字符串
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
	 * 转换页面显示格式，如果输入字符为空，这以HTML的空字&nbsp;显示
	 * </p>
	 * 
	 */
	public static String changeShowFormat(String str) {
		return (str.equals("") ? "&nbsp;" : str);
	}

	/**
	 * <p>
	 * 出符串str中出现sub的次数
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
	 * 在一个字符串前补上字符0，使得输出字符串为特定的长度
	 * </p>
	 * putZero("236",8) = "00000236"
	 * 
	 * @param str
	 *            输入字符串
	 * @param lenth
	 *            输出字符串的长度
	 * @return 返回字符串
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
	 * 对一个字符串前，或字符串后补特定的字符串
	 * </p>
	 * putChar("236",8,"0",0) = "00000236" putChar("236",8,"0",1) = "23600000"
	 * 
	 * @param str
	 *            输入字符串
	 * @param totalLenth
	 *            输出字符串的长度
	 * @param suffix
	 *            用来填补的字符
	 * @param pos
	 *            0 标识在输入字符串前补，1标识在输入字符串后补。
	 * @return 返回字符串
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
	 * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @param str
	 *            原始字符串
	 * @param specialCharsLength
	 *            截取长度(汉、日、韩文字符长度为2)
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
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
	 * 
	 * @param chars
	 *            一段字符
	 * @return 输出长度，输入长度，汉、日、韩文字符长度为2
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
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
	 * 
	 * @param chars
	 *            一段字符
	 * @param specialCharsLength
	 *            输入长度，汉、日、韩文字符长度为2
	 * @return 输出长度，所有字符均长度为1
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
	 * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
	 * 
	 * @param c
	 *            字符
	 * @return 字符长度
	 */
	public static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param char c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	private static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 把source里面的str1用str2替换
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
