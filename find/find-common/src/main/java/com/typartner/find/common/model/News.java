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
import com.typartner.find.common.util.StringUtil;

/** 
 * @Description: 新闻资讯
 * @author YangKai
 * @date 2016年01月26日  
 */
@TableBind(tableName = "news")
public class News extends Model<News> {
	private static final long serialVersionUID = 6635825771785991974L;
	public static final News dao = new News();
	
	
	/**
	 * 状态枚举
	 */
	public static enum STATUS_TYPE{
		/** 1-草稿 */
		READY(1),
		/** 2-正常 */
		ON(2),
		/** 3-停用 */
		OFF(3);
		private int t;
		private STATUS_TYPE(int t){
			this.t = t;
		}
		@Override
		public String toString() {
			return String.valueOf(this.t);
		}
	};
	/**
	 * 是否平台推荐状态枚举
	 */
	public static enum RECOMMEND_TYPE{
		/** 0-否 */
		OFF(0),
		/** 1-是 */
		ON(1);
		private int t;
		private RECOMMEND_TYPE(int t){
			this.t = t;
		}
		@Override
		public String toString() {
			return String.valueOf(this.t);
		}
	}
	
	/**
	 * 获取 新闻资讯 分页信息
	 * @param pageNum
	 * @param pageSize
	 * @param qryIn
	 * @return
	 */
	public Page<News> getPage(int pageNum, int pageSize, News qryIn, String catStr){
		String select = "select n.*";
		StringBuffer sql = new StringBuffer("from news n ");
		List<Object> paramLst = new ArrayList<Object>();
		
		if(!StringUtil.isBlank(catStr)){
			String[] catArr = catStr.split(",");
			StringBuilder catPara = new StringBuilder();
			String inPara = ""; //问号参数
			for(String catId : catArr){
				catPara.append("?,");
				paramLst.add(catId); //具体参数
			}
			if(catPara.length() > 0){
				inPara = catPara.substring(0, catPara.length() - 1);
			}
			sql.append(",	(select DISTINCT NEWS_ID from news_cat where CAT_ID in (").append(inPara).append(")) as nc where n.ID=nc.NEWS_ID ");
		}else{
			sql.append(" where 1=1 ");
		}
		
		if (qryIn != null) {
			String title = qryIn.getStr("TITLE");
			if (StringUtils.isNotBlank(title)) {
				sql.append(SpellSqlUtil.spellAndLike("n.TITLE", title));
			}
			String summary = qryIn.getStr("SUMMARY");
			if (StringUtils.isNotBlank(summary)) {
				sql.append(SpellSqlUtil.spellAndLike("n.SUMMARY", summary));
			}
			String content = qryIn.getStr("CONTENT");
			if (StringUtils.isNotBlank(content)) {
				sql.append(SpellSqlUtil.spellAndLike("n.CONTENT", content));
			}
			String author = qryIn.getStr("AUTHOR");
			if (StringUtils.isNotBlank(author)) {
				sql.append(SpellSqlUtil.spellAndLike("n.AUTHOR", author));
			}
			String c_user_no = qryIn.getStr("C_USER_NO");
			if (StringUtils.isNotBlank(c_user_no)) {
				sql.append(SpellSqlUtil.spellAndLike("n.C_USER_NO", c_user_no));
			}
			Integer status = qryIn.getInt("STATUS");
			if (status != null) {
				sql.append(SpellSqlUtil.spellAndEqual("n.STATUS"));
				paramLst.add(status);
			}
			Integer recommend = qryIn.getInt("RECOMMEND");
			if (recommend != null) {
				sql.append(SpellSqlUtil.spellAndEqual("n.RECOMMEND"));
				paramLst.add(recommend);
			}
		}
		sql.append(" order by n.CREATE_TIME desc");
		Page<News> page = paginate(pageNum, pageSize, select, sql.toString(), paramLst.toArray());
		return page;
	}
	
	/**
	 * 更新 status
	 */
	public boolean updStatus(int newsId, int statusReq){
		News news = findById(newsId);
		if (news == null) {
			return false;
		}
		news.set("STATUS", statusReq);
		if (statusReq == Integer.parseInt(STATUS_TYPE.OFF.toString())) {// 若请求停用，则将推荐状态也取消
			news.set("RECOMMEND", Integer.parseInt(RECOMMEND_TYPE.OFF.toString()));
		}
		news.set("MODIFY_TIME", new Date());
		return news.update();
	}
	/**
	 * 更新 平台推荐 状态
	 */
	public boolean updRecommend(int newsId, int recommendReq){
		News news = findById(newsId);
		if (news == null) {
			return false;
		}
		news.set("RECOMMEND", recommendReq);
		news.set("MODIFY_TIME", new Date());
		return news.update();
	}
	
	/**
	 * 根据条件搜索分页数据
	 * @param catStr
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<News> search(String catStr, int pageNum, int pageSize){
		News qryIn = new News();
		qryIn.set("STATUS", News.STATUS_TYPE.ON.t);
		Page<News> newsList = getPage(pageNum, pageSize, qryIn, catStr);
		if(newsList != null && newsList.getTotalRow() > 0){ //查到了数据
			List<Integer> newsIdList = new ArrayList<Integer>();
			for(News news : newsList.getList()){
				//获取资讯ID的List
				newsIdList.add(news.getInt("ID"));
			}
			
			//资讯类别多对多
			List<Category> catList = Category.dao.getCategoryByNewsId(newsIdList);
			for(Category cat : catList){
				for(News news : newsList.getList()){
					if(news.getInt("ID") == cat.getInt("NEWS_ID")){
						List<Category> tmpList = news.get("catList");
						if(tmpList == null){
							tmpList = new ArrayList<Category>();
							news.put("catList", tmpList);
						}
						tmpList.add(cat);
						break;
					}
				}
			}
		}
		return newsList;
	}
	
	/**
	 * 首页展示12条新闻
	 * @return
	 */
	public List<News> showNews(){
		String sql = "select  @rownum:=@rownum+1 AS ROWNUM,ID,TITLE,SUB_TITLE,CREATE_TIME,RECOMMEND from (SELECT @rownum:=0) r,news where STATUS='2' order by RECOMMEND desc LIMIT 12";
		return dao.find(sql);
	}
	
	/**
	 * 新闻名称唯一性校验
	 * @return
	 */
	public boolean findNewsByName(String newsName){
		Long rs = Db.queryLong("select count(*) from news where TITLE = ?",newsName);
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
}
