/**
 * 
 */
package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/** 
 * @ClassName: ShopCat 
 * @Description: 
 * @author zhanglei
 * @date 2015年12月3日 下午11:28:36  
 */
@TableBind(tableName = "shop_cat")
public class ShopCat extends Model<ShopCat> {
	
	private static final long serialVersionUID = -2674359206207076556L;
	public static final ShopCat dao = new ShopCat();
	
	/**
	 * 清除已选中的商铺类型
	 * @param shopId
	 */
	public void clearByShopId(Integer shopId){
		Db.batch("delete from shop_cat where SHOP_ID=?",  new Object[][]{{shopId}}, 1000);
	}
	
	/**
	 * 根据shopId查询类别
	 * @param shopId
	 * @return
	 */
	public List<ShopCat> getCatByShopId(Integer shopId){
		return find("select s.* from shop_cat s where s.SHOP_ID = ?", shopId);
	}
}
