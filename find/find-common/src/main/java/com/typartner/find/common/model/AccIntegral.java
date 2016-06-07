/**
 * @ClassName:     AccIntegral.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         fangrui
 * @version        V1.0  
 * @Date           2015年12月17日 上午6:04:44 
 */
package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * <p>Title: AccIntegral</p>
 * <p>Description: 个人积分Model</p>
 * @author: fangrui
 * @date : 2015年12月17日 上午6:04:44
 */
@TableBind(tableName = "acc_integral", pkName="ACC_ID")
public class AccIntegral extends Model<AccIntegral> {
	private static final long serialVersionUID = 1L;
	public static final AccIntegral dao = new AccIntegral();
	/*
	 * 获取积分查询分页展示
	 * type: 1 消费    2 赚取
	 */
	public Page<AccIntegral> paginateByUserNO(int pageNum, int pageSize, String type, String userNo){
		String seleSql = "select r.ACC_ID, r.PRODUCT_NAME, t.TYPE_NAME, r.AMOUNT, r.TRAN_TIME, r.BALANCE, r.TRAN_DESC ";
		StringBuffer whereSql = new StringBuffer(" from tran_rec r , tran_type t  where r.TRAN_TYPE = t.TYPE_ID and r.STATUS = 0 and t.BUSI_TYPE = '");
		whereSql.append(type).append("' and r.USER_NO = '").append(userNo).append("' order by r.TRAN_TIME DESC");
		return paginate(pageNum, pageSize, seleSql, whereSql.toString());
	}
	
	public AccIntegral getIntegralByUid(String userNo){
		return this.dao.findFirst("select a.* from acc_integral a, p_user p where a.USER_ID = p.ID and p.USER_NO = '"+userNo+"'");
	}
	
	/**
	 * 根据userId获取 Integral
	 * @param userId
	 * @return
	 */
	public AccIntegral getIntegralByUserId(Integer userId){
		return dao.findFirst("select * from acc_integral where user_id=?", userId);
	}
	
	/**
	 * 判断用户是否支付的起
	 * @param userNo
	 * @param typeId
	 * @return
	 */
	public boolean canAfford(String userNo, TranType.TRAN_TYPE_ID typeId){
		AccIntegral integ = dao.findFirst("select a.BALANCE, (select TRAN_FEE from tran_type where TYPE_ID=?) as TRAN_FEE "
				+ "from acc_integral a, p_user p where a.USER_ID = p.ID and a.STATUS=0 and p.USER_NO = ?", typeId.toString(), userNo);
		if (integ == null) {
			return false;
		}
		return integ.getInt("BALANCE") >= integ.getInt("TRAN_FEE");
	}
}
