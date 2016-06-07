package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ty.partner.back.util.SpellSqlUtil;

/**
 * 客户留言表
 * @author YangKai
 */
@TableBind(tableName = "CUSTOMER_MESSAGE")
public class CustMsg extends Model<CustMsg> {
	private static final long serialVersionUID = -6070221578749699144L;

	public static final CustMsg dao = new CustMsg();

	
	/**
	 * 表名
	 */
	public static final String TABLE_NAME = "CUSTOMER_MESSAGE";
	/**
	 * 列名-留言ID-INT
	 */
	public static final String C_MSG_ID = "ID";
	/**
	 * 列名-手机号-VARCHAR(11)
	 */
	public static final String C_PHONE = "PHONE";
	/**
	 * 列名-邮箱-VARCHAR(50)
	 */
	public static final String C_EMAIL = "EMAIL";
	/**
	 * 列名-微信号-VARCHAR(200)
	 */
	public static final String C_WECHAT = "WECHAT";
	/**
	 * 列名-QQ号-VARCHAR(100)
	 */
	public static final String C_QQ = "QQ";
	/**
	 * 列名-留言内容-VARCHAR(1000)
	 */
	public static final String C_CONTENT = "CONTENT";
	/**
	 * 列名-客户称呼-VARCHAR(20)
	 */
	public static final String C_USER_NAME = "USER_NAME";
	/**
	 * 列名-客户NO-VARCHAR(30)
	 */
	public static final String C_USER_NO = "USER_NO";
	/**
	 * 列名-创建时间-DATATIME
	 */
	public static final String C_CREATE_TIME = "CREATE_TIME";
	/**
	 * 列名-回复管理员No-VARCHAR(30)
	 */
	public static final String C_REPLY_USER_NO = "REPLY_USER_NO";
	/**
	 * 列名-回复内容-VARCHAR(1000)
	 */
	public static final String C_REPLY_CONTENT = "REPLY_CONTENT";
	/**
	 * 列名-回复时间-DATATIME
	 */
	public static final String C_REPLY_TIME = "REPLY_TIME";
	/**
	 * 列名-状态-TINYINT(1)
	 */
	public static final String C_STATUS = "STATUS";
	
	/**
	 * 扩展-最小 创建时间
	 */
	private Date lowCreateTime;
	/**
	 * 扩展-最大 创建时间
	 */
	private Date highCreateTime;
	/**
	 * 扩展-最小 回复时间
	 */
	private Date lowReplyTime;
	/**
	 * 扩展-最大 回复时间
	 */
	private Date highReplyTime;

	/**
	 * 状态-待答复-1
	 */
	public static final int STATUS_WAIT = 1;
	/**
	 * 状态-已答复-2
	 */
	public static final int STATUS_REPLY = 2;
	/**
	 * 状态-已删除-3
	 */
	public static final int STATUS_DELETE = 3;
	
	 
	 
	 
	 
	public Page<CustMsg> getCustMsgPage(CustMsg qryIn, int pageNum, int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("from " + TABLE_NAME + " where 1=1");
		List<Object> paramLst = new ArrayList<Object>();
		if (null != qryIn) {
			if (StringUtils.isNotBlank(qryIn.getStr(C_PHONE))) {
				sql.append(SpellSqlUtil.spellAndLike(C_PHONE, qryIn.getStr(C_PHONE)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_EMAIL))) {
				sql.append(SpellSqlUtil.spellAndLike(C_EMAIL, qryIn.getStr(C_EMAIL)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_WECHAT))) {
				sql.append(SpellSqlUtil.spellAndLike(C_WECHAT, qryIn.getStr(C_WECHAT)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_QQ))) {
				sql.append(SpellSqlUtil.spellAndLike(C_QQ, qryIn.getStr(C_QQ)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_USER_NO))) {
				sql.append(SpellSqlUtil.spellAndLike(C_USER_NO, qryIn.getStr(C_USER_NO)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_USER_NAME))) {
				sql.append(SpellSqlUtil.spellAndLike(C_USER_NAME, qryIn.getStr(C_USER_NAME)));
			}
			if (StringUtils.isNotBlank(qryIn.getStr(C_REPLY_USER_NO))) {
				sql.append(SpellSqlUtil.spellAndLike(C_REPLY_USER_NO, qryIn.getStr(C_REPLY_USER_NO)));
			}
			// 设置留言时间
			Date lowCT = qryIn.getLowCreateTime();
			if (lowCT != null) {
				sql.append(" and "+C_CREATE_TIME+">?");
				paramLst.add(lowCT);
			}
			Date highCT = qryIn.getHighCreateTime();
			if (highCT != null) {
				sql.append(" and "+C_CREATE_TIME+"<?");
				paramLst.add(highCT);
			}
			// 设置回复时间
			Date lowRT = qryIn.getLowReplyTime();
			if (lowRT != null) {
				sql.append(" and "+C_REPLY_TIME+">?");
				paramLst.add(lowRT);
			}
			Date highRT = qryIn.getHighReplyTime();
			if (highRT != null) {
				sql.append(" and "+C_REPLY_TIME+"<?");
				paramLst.add(highRT);
			}
			// 设置status	0-全部（非删除），1-待回复，2-已回复，3-删除
			int status = qryIn.getInt(C_STATUS);
			switch (status) {
			case STATUS_WAIT:
				sql.append(SpellSqlUtil.spellAndEqual(C_STATUS, STATUS_WAIT));
				break;
			case STATUS_REPLY:
				sql.append(SpellSqlUtil.spellAndEqual(C_STATUS, STATUS_REPLY));
				break;
			case STATUS_DELETE:
				sql.append(SpellSqlUtil.spellAndEqual(C_STATUS, STATUS_DELETE));
				break;
			default:
				sql.append(" and "+C_STATUS+" in("+STATUS_WAIT+","+STATUS_REPLY+")");
				break;
			}
		}
		sql.append(" order by "+C_MSG_ID + " desc");
		
		return dao.paginate(pageNum, pageSize, "select *", sql.toString(), paramLst.toArray());
	}

	/**
	 * 待回复数
	 */
	public long countReply(){
		return Db.queryLong("select count(*) from "+TABLE_NAME+" where "+C_STATUS+"="+STATUS_WAIT);
	}


	
	
	
	
	public Date getLowCreateTime() {
		return lowCreateTime;
	}
	public void setLowCreateTime(Date lowCreateTime) {
		this.lowCreateTime = lowCreateTime;
	}
	public Date getHighCreateTime() {
		return highCreateTime;
	}
	public void setHighCreateTime(Date highCreateTime) {
		this.highCreateTime = highCreateTime;
	}
	public Date getLowReplyTime() {
		return lowReplyTime;
	}
	public void setLowReplyTime(Date lowReplyTime) {
		this.lowReplyTime = lowReplyTime;
	}
	public Date getHighReplyTime() {
		return highReplyTime;
	}
	public void setHighReplyTime(Date highReplyTime) {
		this.highReplyTime = highReplyTime;
	}
}
