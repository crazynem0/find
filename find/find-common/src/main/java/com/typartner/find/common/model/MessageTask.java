package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * <p>Title: MessageTask</p>
 * <p>Description: 短信记录</p>
 * @author: hety
 */
@SuppressWarnings("serial")
@TableBind(tableName = "message_task")
public class MessageTask extends Model<MessageTask> {
	
	public static final MessageTask dao=new MessageTask();

}
