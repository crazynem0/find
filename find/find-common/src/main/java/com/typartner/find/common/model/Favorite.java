package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.typartner.find.common.constant.CommonConstants;

/**
 * <p>Title: Favorite</p>
 * <p>Description: 个人中心收藏店铺、项目记录</p>
 * @author: hety
 */
@SuppressWarnings("serial")
@TableBind(tableName = "favorite")
public class Favorite extends Model<Favorite>{
	public static final Favorite dao = new Favorite();
	
	public Page<Favorite> shopPaginate(int pageNum, int pageSize){
		return paginate(pageNum, pageSize, "select * ", "from favorite where type="+CommonConstants.FAV_SHOP+" order by id asc");
	}
	/*
	 * add fangrui
	 * 我的收藏店铺所使用，个人中心进行调用 
	 */
	public Page<Favorite> shopPaginateByUid(int pageNum, int pageSize, String userNo){
		String selSql = "select f.id as fid, s.*, i.IMG_PATH ";
		StringBuffer sb = new StringBuffer(" from favorite f, shop s, (select shop_id, IMG_PATH, show_index from shop_img group by shop_id having min(show_index)) i where f.FAVORITE_ID = s.id and s.id = i.SHOP_ID and f.type=");
		sb.append(CommonConstants.FAV_SHOP).append(" and f.USER_NO = '").append(userNo).append("' order by f.CREATE_TIME ");
		return paginate(pageNum, pageSize, selSql, sb.toString());
	}
	
	public Page<Favorite> projectPaginate(int pageNum, int pageSize){
		return paginate(pageNum, pageSize, "select * ", "from favorite where type='2' order by id asc");
	}
	/*
	 * add fangrui
	 * 我的收藏项目使用，个人中心进行调用 
	 */
	public Page<Favorite> projectPaginateByUid(int pageNum, int pageSize, String userNo){
		String selSql = " select f.id as fid, p.*, i.IMG_PATH ";
		StringBuffer sb = new StringBuffer(" from favorite f, project p, (select PROJECT_ID, IMG_PATH from project_img group by PROJECT_ID having min(show_index)) i where f.FAVORITE_ID = p.id and  p.id = i.PROJECT_ID  and f.type = ");
		sb.append(CommonConstants.FAV_PROJ).append(" and f.USER_NO ='").append(userNo).append("' order by f.CREATE_TIME ");
		return paginate(pageNum, pageSize, selSql, sb.toString());
	}
	
	/**
	 * 收藏校验重复
	 * @param userno 
	 */
	public Favorite findByIdAndType(String userno, int favorite_id,int type){
		String sql = "select * from favorite where user_no=? and favorite_id=? and type=?";
		return Favorite.dao.findFirst(sql,userno,favorite_id,type);
	}
	
}
