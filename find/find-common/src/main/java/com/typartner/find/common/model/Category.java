package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ty.partner.back.util.SpellSqlUtil;

/** 
 * @ClassName: Category 
 * @Description: 类别
 * @author zhanglei
 * @date 2015年11月22日 上午1:09:40  
 */
@TableBind(tableName = "category", pkName="CAT_ID")
public class Category extends Model<Category> {
	
	/**
	 * 类别类型
	* @ClassName: CAT_TYPE 
	* @Description: 
	* @author zhanglei
	* @date 2015年11月25日 下午10:19:55
	 */
	public static enum CAT_TYPE{
		SHOP(1), //商铺
		PROJECT(2), //项目
		NEWS(3), //新闻
		CHAIN(5);// 连锁公司
		
		private int type;
		private CAT_TYPE(int type){
			this.type = type;
		}
		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
	};
	
	
	private static final long serialVersionUID = 6635825771785991974L;
	
	public static final Category dao = new Category();
	
	/**
	 * 获取类别列表
	 * @param catType 类别
	 * @return
	 */
	public Map<String, List<Category>> getCategory(CAT_TYPE catType){
		StringBuilder sb = new StringBuilder();
		sb.append("select t.cat_id, t.cat_name, t1.CAT_NAME as PARENT_NAME, t1.SEARCH_KEY as SEARCH_KEY from (")
			.append("select * from category where PARENT_ID in(")
			.append("select CAT_ID from category where CAT_TYPE=? ").append("and STATUS=0)) t,")
			.append("category t1 where t.parent_id = t1.CAT_ID order by t.cat_id");

		List<Category> list = dao.find(sb.toString(), catType.toString());
		Map<String, List<Category>> map = new HashMap<String, List<Category>>();
		for(Category cat : list){
			if(!map.containsKey(cat.getStr("PARENT_NAME"))){
				map.put(cat.getStr("PARENT_NAME"), new ArrayList<Category>());
			}
			List<Category> tmpList = map.get(cat.getStr("PARENT_NAME"));
			tmpList.add(cat);
		}
		return map;
		
	}
	
	/**
	 * 根据商铺ID查询对应类别list
	 * @param shopIdList
	 * @return
	 */
	public List<Category> getCategoryByShopId(Integer shopId){
		StringBuilder sb = new StringBuilder();
		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, sc.SHOP_ID from shop_cat sc, category c where sc.CAT_ID=c.CAT_ID and c.STATUS=0 and sc.SHOP_ID = ?");
		return find(sb.toString(), shopId);
	}
	
	/**
	 * 根据商铺ID查询对应类别list
	 * @param shopIdList
	 * @return
	 */
	public List<Category> getCategoryByProjectId(Integer projectId){
		StringBuilder sb = new StringBuilder();
		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, sc.PROJECT_ID from project_cat sc, category c where sc.CAT_ID=c.CAT_ID and c.STATUS=0 and sc.PROJECT_ID = ?");
		return find(sb.toString(), projectId);
	}
	
	/**
	 * 根据商铺ID查询对应类别list
	 * @param shopIdList
	 * @return
	 */
	public List<Category> getCategoryByShopId(List<Integer> shopIdList){
		StringBuilder sb = new StringBuilder();
		List<Integer> paras = new ArrayList<Integer>();
		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, sc.SHOP_ID from shop_cat sc, category c where sc.CAT_ID=c.CAT_ID and c.STATUS=0 and sc.SHOP_ID in (");
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
	
	/**
	 * 根据项目ID查询对应类别list
	 * @param shopIdList
	 * @return
	 */
	public List<Category> getCategoryByProjectId(List<Integer> projectIdList){
		StringBuilder sb = new StringBuilder();
		List<Integer> paras = new ArrayList<Integer>();
		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, pc.PROJECT_ID from project_cat pc, category c where pc.CAT_ID=c.CAT_ID and c.STATUS=0 and pc.PROJECT_ID in (");
		StringBuilder catPara = new StringBuilder();
		String inPara = ""; //问号参数
		for(int projectId : projectIdList){
			catPara.append("?,");
			paras.add(projectId); //具体参数
		}
		if(catPara.length() > 0){
			inPara = catPara.substring(0, catPara.length() - 1);
		}
		sb.append(inPara);
		sb.append(")");
		return find(sb.toString(), paras.toArray());
	}
	
	/**
	 * 根据资讯ID查询对应类别list
	 * @param shopIdList
	 * @return
	 */
	public List<Category> getCategoryByNewsId(List<Integer> newsIdList){
		StringBuilder sb = new StringBuilder();
		List<Integer> paras = new ArrayList<Integer>();
		sb.append("select c.CAT_ID, c.CAT_NAME, (select SEARCH_KEY from category where CAT_ID=c.PARENT_ID) as SEARCH_KEY, pc.NEWS_ID from news_cat pc, category c where pc.CAT_ID=c.CAT_ID and c.STATUS=0 and pc.NEWS_ID in (");
		StringBuilder catPara = new StringBuilder();
		String inPara = ""; //问号参数
		for(int newsId : newsIdList){
			catPara.append("?,");
			paras.add(newsId); //具体参数
		}
		if(catPara.length() > 0){
			inPara = catPara.substring(0, catPara.length() - 1);
		}
		sb.append(inPara);
		sb.append(")");
		return find(sb.toString(), paras.toArray());
	}
	
	public Map<String, List<Category>> getCategory(CAT_TYPE catType,Integer shopId){
		StringBuilder sb = new StringBuilder();
		sb.append("select b.cat_id,b.CAT_NAME,b.PARENT_NAME,b.PARENT_ID from shop_cat a,(")
			.append(" select t.cat_id, t.cat_name, t1.CAT_NAME as PARENT_NAME, t1.cat_id as PARENT_ID from (")
			.append(" select * from category where PARENT_ID in(")
			.append(" select CAT_ID from category where CAT_TYPE=? ").append("and STATUS=0)) t,")
			.append(" category t1 where t.parent_id = t1.CAT_ID order by t.cat_id ) b")
			.append(" where a.CAT_ID=b.CAT_ID and SHOP_ID=?");

		List<Category> list = dao.find(sb.toString(), catType.toString(), shopId);
		Map<String, List<Category>> map = new HashMap<String, List<Category>>();
		for(Category cat : list){
			if(!map.containsKey(cat.getStr("PARENT_NAME"))){
				map.put(cat.getStr("PARENT_NAME"), new ArrayList<Category>());
			}
			List<Category> tmpList = map.get(cat.getStr("PARENT_NAME"));
			tmpList.add(cat);
		}
		return map;
		
	}

	/**
	 * 获取分页列表信息
	 */
	public Page<Category> getPage(int pageNum, int pageSize, Category qryIn){
		String select = "SELECT p.*,count(c.PARENT_ID) AS CHILD_COUNT";
		StringBuffer sql = new StringBuffer("FROM category p LEFT JOIN category c ON c.PARENT_ID=p.CAT_ID WHERE p.PARENT_ID=0");
		List<Object> paramLst = new ArrayList<Object>();
		if (qryIn != null) {
			String catName = qryIn.getStr("CAT_NAME");
			if (StringUtils.isNotBlank(catName)) {
				sql.append(SpellSqlUtil.spellAndLike("p.CAT_NAME", catName));
			}
			Integer catType = qryIn.getInt("CAT_TYPE");
			if (catType != null) {
				sql.append(SpellSqlUtil.spellAndEqual("p.CAT_TYPE"));
				paramLst.add(catType);
			}
			Integer status = qryIn.getInt("STATUS");
			if (status != null) {
				sql.append(SpellSqlUtil.spellAndEqual("p.STATUS"));
				paramLst.add(status);
			}
		}
		sql.append(" GROUP BY p.CAT_ID");
		sql.append(" ORDER BY p.CAT_TYPE");
		Page<Category> page = paginate(pageNum, pageSize, select, sql.toString(), paramLst.toArray());
		return page;
	}
	
	/**
	 * 根据 类别类型 获取 一级类别列表
	 * @param catType
	 * @return List<Category>
	 */
	public List<Category> getLstByCatType(Integer catType){
		return dao.find("select * from category where CAT_TYPE=? and PARENT_ID=0 AND STATUS=0", catType);
	}
	
	/**
	 * 根据 父类别 获取 子类别列表
	 * @param parentId
	 * @return List<Category>
	 */
	public List<Category> getLstByParentId(Integer parentId){
		return dao.find("select * from category where PARENT_ID=?", parentId);
	}
}
