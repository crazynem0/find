/**
 * 
 */
package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/** 
 * @ClassName: Category 
 * @Description: 类别
 * @author zhanglei
 * @date 2015年11月22日 上午1:09:40  
 */
@TableBind(tableName = "shop_img")
public class ShopImg extends Model<ShopImg> {
	
	private static final long serialVersionUID = 5015900737408329288L;
	public static final ShopImg dao = new ShopImg();
	
	/**
	 * 清除已选中的商铺图片
	 * @param shopId
	 */
	public void clearByShopId(Integer shopId){
		Db.batch("delete from shop_img where SHOP_ID=?",  new Object[][]{{shopId}}, 1000);
	}
	
	/**
	 * 根据shopId查询图片
	 * @param shopId
	 * @return
	 */
	public List<ShopImg> getImgByShopId(Integer shopId){
		return find("select s.* from shop_img s where s.SHOP_ID = ?", shopId);
	}
	
	public List<ShopImg> getImgByShopId(List<Integer> shopIdList){
		StringBuilder sb = new StringBuilder();
		List<Integer> paras = new ArrayList<Integer>();
		sb.append("select s.* from shop_img s where s.SHOP_ID in (");
//		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, sc.SHOP_ID from shop_cat sc, category c where sc.CAT_ID=c.CAT_ID and c.STATUS=0 and sc.SHOP_ID in (");
		StringBuilder catPara = new StringBuilder();
		String inPara = ""; //问号参数
		for(int shopId : shopIdList){
			catPara.append("?,");
			paras.add(shopId); //具体参数
		}
		if(catPara.length() > 0){
			inPara = catPara.substring(0, catPara.length() - 1);
		}
		sb.append(inPara);
		sb.append(")");
		return find(sb.toString(), paras.toArray());
	}

}
