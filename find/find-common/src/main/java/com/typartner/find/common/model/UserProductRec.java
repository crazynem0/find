/**
 * 
 */
package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 用户产品记录
* @ClassName: UserProductRec 
* @Description: 
* @author zhanglei
* @date 2015年12月23日 上午12:00:51
 */
@TableBind(tableName = "user_product_rec")
public class UserProductRec extends Model<UserProductRec> {
	
	/**
	 * 产品类型
	* @ClassName: CAT_TYPE 
	* @Description: 
	* @author zhanglei
	* @date 2015年11月25日 下午10:19:55
	 */
	public static enum PRODUCT_TYPE{
		SHOP(1), //商铺
		PROJECT(2), //项目
		CHAIN(3);//连锁公司
		
		private int type;
		
		private PRODUCT_TYPE(int type){
			this.type = type;
		}

		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
	};
	
	public static enum BUSI_TYPE{
		REDUCE(1), //消费
		INCREASE(2), //充值
		/** 发布 */
		RELEASE(3);
		
		private int type;
		
		private BUSI_TYPE(int type){
			this.type = type;
		}

		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
	};
	
	private static final long serialVersionUID = 6635825771785991974L;
	
	public static final UserProductRec dao = new UserProductRec();
	
	/**
	 * 判断是否有此记录
	 * @param userNo
	 * @param productId
	 * @param productType
	 * @return
	 */
	public boolean hasRec(String userNo, Integer productId, PRODUCT_TYPE productType){
//		if (isPublishByCurrent(userNo, productId, productType)){ //为当前用户发布
//			return true;
//		}else{ //查询购买记录
			String sql = "select count(*) from user_product_rec u where u.USER_NO=? and u.PRODUCT_ID=? and u.PRODUCT_TYPE=?";
			return Db.queryLong(sql, userNo, productId, productType.toString()) >= 1;
//		}
		
	}
	/**
	 * 判断是否有此记录
	 * @param userNo
	 * @param productId
	 * @param productType
	 * @param busiType
	 * @return
	 */
	public boolean hasRec(String userNo, Integer productId, PRODUCT_TYPE productType, BUSI_TYPE busiType){
//		if (isPublishByCurrent(userNo, productId, productType)){ //为当前用户发布
//			return true;
//		}else{ //查询购买记录
		String sql = "select count(*) from user_product_rec u where u.USER_NO=? and u.PRODUCT_ID=? and u.PRODUCT_TYPE=? and BUSI_TYPE=?";
		return Db.queryLong(sql, userNo, productId, productType.toString(), busiType.toString()) >= 1;
//		}
		
	}
	
	/**
	 * 是否当前用户发布
	 * @param userNo
	 * @param productId
	 * @param productType
	 * @return
	 */
//	public boolean isPublishByCurrent(String userNo, Integer productId, PRODUCT_TYPE productType){
//		return isPublishByCurrent(userNo, productId, Integer.parseInt(productType.toString()));
//	}
	
	/**
	 * 是否当前用户发布
	 * @param userNo
	 * @param productId
	 * @param productType
	 * @return
	 */
	public boolean isPublishByCurrent(String userNo, Integer productId, Integer productType){
		String table = "";
		PRODUCT_TYPE.SHOP.ordinal();
		switch (productType) {
		case 1:
			table = "shop";
			break;
		case 2:
			table = "project";
			break;
		default:
			break;
		}
		String sql = "select count(*) from " + table + " where P_USER_NO=? and ID=? ";
		return Db.queryLong(sql, userNo, productId) >= 1;
	}

}
