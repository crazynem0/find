package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ty.partner.back.util.SpellSqlUtil;

/**
 * 快速发布
* @ClassName: QuickRelease 
* @Description: 
* @author zhanglei
* @date 2015年12月27日 下午11:31:02
 */
@TableBind(tableName = "quick_release")
public class QuickRelease extends Model<QuickRelease> {
	
	private static final long serialVersionUID = 5015900737408329288L;
	public static final QuickRelease dao = new QuickRelease();
	
	
	/**
	 * 获取分页
	 * @param qryIn
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<QuickRelease> getPage(QuickRelease qryIn, int pageNum, int pageSize){
		StringBuffer sql = new StringBuffer("from quick_release where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (qryIn != null) {
			if (StringUtils.isNotBlank(qryIn.getStr("PRODUCT_NAME"))) {
				sql.append(SpellSqlUtil.spellAndLike("PRODUCT_NAME"));
				params.add("%"+qryIn.getStr("PRODUCT_NAME")+"%");
			}
			if (qryIn.getInt("PRODUCT_TYPE") != null) {
				sql.append(SpellSqlUtil.spellAndEqual("PRODUCT_TYPE"));
				params.add(qryIn.getInt("PRODUCT_TYPE"));
			}
			if (StringUtils.isNotBlank(qryIn.getStr("PRODUCT_DESC"))) {
				sql.append(SpellSqlUtil.spellAndLike("PRODUCT_DESC"));
				params.add("%"+qryIn.getStr("PRODUCT_DESC")+"%");
			}
			if (StringUtils.isNotBlank(qryIn.getStr("P_USER_NO"))) {
				sql.append(SpellSqlUtil.spellAndLike("P_USER_NO"));
				params.add("%"+qryIn.getStr("P_USER_NO")+"%");
			}
			if (StringUtils.isNotBlank(qryIn.getStr("C_USER_NO"))) {
				sql.append(SpellSqlUtil.spellAndLike("C_USER_NO"));
				params.add("%"+qryIn.getStr("C_USER_NO")+"%");
			}
			if (qryIn.getInt("STATUS") != null) {
				sql.append(SpellSqlUtil.spellAndEqual("STATUS"));
				params.add(qryIn.getInt("STATUS"));
			}
		}
		sql.append(" order by CREATE_TIME desc");
		return paginate(pageNum, pageSize, "select *", sql.toString(), params.toArray());
	}
	
	/**
	 * 申请数
	 */
	public long countApply(String t){
		Integer productType;
		if ("shop".equalsIgnoreCase(t)) {
			productType = 1;
		} else if ("project".equalsIgnoreCase(t)) {
			productType = 2;
		} else {
			productType = 0;
		}
		return Db.queryLong("SELECT COUNT(*) FROM quick_release WHERE STATUS=1 AND PRODUCT_TYPE=?", productType);
	}
}
