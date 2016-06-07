/**
 * @ClassName:     FileLog.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         fangrui
 * @version        V1.0  
 * @Date           2015年11月19日 上午11:27:43 
 */
package com.typartner.find.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * <p>Title: FileLog</p>
 * <p>Description: TODO(进行业务日志操作记录)</p>
 * @author: fangrui
 * @date : 2015年11月19日 上午11:27:43
 */

public class FileLog {

	private static Logger debugLogger = null;
	private static Logger infoLogger = null;
	private static Logger errorLogger = null;
	
	static{
		loadLogger();
	}
	
	static void loadLogger(){
		debugLogger = Logger.getLogger("debugLogger");
		infoLogger = Logger.getLogger("infoLogger");
		errorLogger = Logger.getLogger("errorLogger");
	}
	
	public static void errorLog(Object msg) {
		errorLogger.error(msg);
	}
	
	public static void errorLog(Exception e) {
		errorLogger.error(getExceptionTrace(e));
	}
	
	public static void errorLog(Exception e, Object msg) {
		errorLogger.error(msg + "\n" + getExceptionTrace(e));
	}
	
	public static void debugLog(Object msg) {
		debugLogger.debug(msg);
	}
	
	public static void debugLog(Exception e) {
		debugLogger.debug(getExceptionTrace(e));
	}
	
	public static void debugLog(Exception e, Object msg) {
		debugLogger.debug(msg + "\n" + getExceptionTrace(e));
	}
	
	public static void infoLog(Object msg) {
		infoLogger.info(msg);
	}
	
	public static void infoLog(Exception e) {
		infoLogger.info(getExceptionTrace(e));
	}
	
	public static void infoLog(Exception e, Object msg) {
		infoLogger.info(msg + "\n" + getExceptionTrace(e));
	}
	
	private static String getExceptionTrace(Exception e) {
		String s = null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PrintWriter wrt = new PrintWriter(bout);
		e.printStackTrace(wrt);
		wrt.close();
		s = bout.toString();
		return s;
	}
	
	
}
