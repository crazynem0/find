/**
 * 
 */
package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/** 
 * @ClassName: Prefecture 
 * @Description: 地区
 * @author zhanglei
 * @date 2015年11月22日 上午1:09:40  
 */
@TableBind(tableName = "prefecture")
public class Prefecture extends Model<Prefecture> {
	
	private static final long serialVersionUID = 6478328649745791088L;
	
	public static final Prefecture dao = new Prefecture();
	
	public List<Prefecture> findPrefecture(String parentId){
		String sql = "select * from prefecture where parent_id = ?";
		return dao.find(sql, parentId);
	}

}
