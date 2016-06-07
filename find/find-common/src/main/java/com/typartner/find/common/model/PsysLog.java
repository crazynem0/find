package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
* @ClassName: PsysLog 
* @Description: 前台系统日志表
* @author hety
* @date 2015年12月23日 下午15:38:49
 */
@TableBind(tableName = "p_sys_log")
public class PsysLog extends Model<PsysLog> {
	
	public static final PsysLog dao = new PsysLog();

}
