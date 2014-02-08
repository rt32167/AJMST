/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajmst.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author caijun
 */
public class ExceptionUtil {
    /***
     * 异常转化成字符串
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t){
        Writer writer = new StringWriter();
        PrintWriter s = new PrintWriter(writer);
        t.printStackTrace(s);
        return writer.toString();
    }
}
