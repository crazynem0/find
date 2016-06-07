package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/** 
 * @ClassName: NewsCat 
 * @Description: 
 * @author hety
 * @date 2015年12月23日 下午15:40:36  
 */
@TableBind(tableName = "news_cat")
public class NewsCat extends Model<NewsCat>{

	private static final long serialVersionUID = -5671317638255539124L;
	public static final NewsCat dao = new NewsCat();
	
	/**
	 * 清除已选中的项目类型
	 * @param newsId
	 */
	public void clearByNewsId(Integer newsId){
		Db.batch("delete from news_cat where NEWS_ID=?",  new Object[][]{{newsId}}, 1000);
	}
	
	/**
	 * 根据newsId查询类别
	 * @param newsId
	 * @return
	 */
	public List<NewsCat> getCatByNewsId(Integer newsId){
		return find("select s.* from news_cat s where s.NEWS_ID = ?", newsId);
	}

}
