package com.typartner.find.common.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * 评价
* @ClassName: Evaluation 
* @Description: 
* @author zhanglei
* @date 2015年12月28日 下午11:03:33
 */
@TableBind(tableName = "evaluation")
public class Evaluation extends Model<Evaluation> {
	
	/**
	 * 类别类型
	* @ClassName: CAT_TYPE 
	* @Description: 
	* @author zhanglei
	* @date 2015年11月25日 下午10:19:55
	 */
	public static enum CAT_TYPE{
		SHOP(1), //商铺
		PROJECT(2); //项目
		
		private int type;
		
		private CAT_TYPE(int type){
			this.type = type;
		}

		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
	};
	
	/**
	 * 操作类型枚举
	 */
	public static enum OPT_TYPE{
		/** 评价 */
		EVALUATION(1),
		/** 纠错 */
		ERROR(2),
		/** 举报 */
		REPORT(3);
		
		private int t;
		private OPT_TYPE(int t){
			this.t = t;
		}
		@Override
		public String toString(){
			return String.valueOf(t);
		}
	}
	
	/**
	 * 状态值 枚举
	 */
	public static enum STATUS{
		/** 默认：未处理 */
		DEF(0),
		/** 已接受 */
		ACCEPT(1),
		/** 已拒绝 */
		REFUSE(2);
		
		private int t;
		private STATUS(int t){
			this.t = t;
		}
		@Override
		public String toString(){
			return String.valueOf(t);
		}
	}
	
	private static final long serialVersionUID = 6635825771785991974L;
	
	public static final Evaluation dao = new Evaluation();
	
	/**
	 * 获取平均评分
	 * @param catType
	 * @param objId
	 * @return
	 */
	public Double getAvgScoreById(CAT_TYPE catType, Integer objId){
		BigDecimal bd = Db.queryBigDecimal("select round(avg(SCORE),1) from evaluation where EVA_OBJ_TYPE = ? and EVA_OBJ_ID = ? and P_ID = 0", catType.toString(), objId);
		Double rs = null;
		if(bd != null){
			rs = bd.doubleValue();
		}
		return rs;
	}
	
	public Page<Evaluation> search(Integer objId, Integer objType, int pageNum, int pageSize){
		Page<Evaluation> page = paginate(pageNum, pageSize, "select e.ID, e.EVA_CONTENT, e.CREATE_TIME, u.NICK_NAME, u.PROFILE_PIC", 
				" from evaluation e, p_user u where e.USER_NO=u.USER_NO and e.EVA_OBJ_ID=? and e.EVA_OBJ_TYPE=? and e.P_ID=0 order by e.CREATE_TIME desc", objId, objType);
		
		//查询回复
		List<Integer> pIdList = new ArrayList<Integer>();
		for(Evaluation e : page.getList()){
			pIdList.add(e.getInt("ID"));
		}
		List<Evaluation> replyList = searchChild(pIdList);
		for(Evaluation c : replyList){
			for(Evaluation p : page.getList()){
				if(c.getInt("P_ID") == p.getInt("ID")){ //匹配到回复关系
					p.put("reply", c);
				}
			}
		}
		
		return page;
	}
	
	private List<Evaluation> searchChild(List<Integer> pIdList){
		List<Evaluation> list = new ArrayList<Evaluation>();
		if(pIdList != null && pIdList.size() > 0){
			List<Object> paras = new ArrayList<Object>();
			StringBuilder pPara = new StringBuilder();
			String inPara = ""; //问号参数
			for(Integer pId : pIdList){
				pPara.append("?,");
				paras.add(pId); //具体参数
			}
			if(pPara.length() > 0){
				inPara = pPara.substring(0, pPara.length() - 1);
			}
			
			StringBuilder sb = new StringBuilder("select e.P_ID, e.EVA_CONTENT, e.CREATE_TIME, u.NICK_NAME, u.PROFILE_PIC from evaluation e, p_user u where e.USER_NO=u.USER_NO and e.P_ID in (");
			sb.append(inPara).append(")");
			list = find(sb.toString(), paras.toArray());
		}
		
		return list;
	}

	/**
	 * 获取 纠错、举报 分页
	 * @param pageNum
	 * @param pageSize
	 * @param qryIn
	 * @return
	 */
	public Page<Evaluation> getEvaluationPage(int pageNum, int pageSize, Evaluation qryIn){
		StringBuffer select = new StringBuffer("select e.*");
		select.append(",(SELECT s.SHOP_NAME FROM shop s WHERE e.EVA_OBJ_TYPE="+CAT_TYPE.SHOP.toString()+" AND s.ID=e.EVA_OBJ_ID) AS SHOP_NAME")
			.append(",(SELECT p.PROJECT_NAME FROM project p WHERE e.EVA_OBJ_TYPE="+CAT_TYPE.PROJECT.toString()+" AND p.ID=e.EVA_OBJ_ID) AS PROJECT_NAME");
		StringBuffer sql = new StringBuffer("FROM evaluation e WHERE e.OPT_TYPE in("+OPT_TYPE.ERROR.toString()+","+OPT_TYPE.REPORT.toString()+")");
		List<Object> paramLst = new ArrayList<Object>();
		if (qryIn != null) {
			Integer eva_obj_type = qryIn.getInt("EVA_OBJ_TYPE");
			if (eva_obj_type != null) {
				sql.append(" and e.EVA_OBJ_TYPE=?");
				paramLst.add(eva_obj_type);
			}
			Integer opt_type = qryIn.getInt("OPT_TYPE");
			if (opt_type != null) {
				sql.append(" and e.OPT_TYPE=?");
				paramLst.add(opt_type);
			}
			Integer status = qryIn.getInt("STATUS");
			if (status != null) {
				sql.append(" and e.STATUS=?");
				paramLst.add(status);
			}
			String user_no = qryIn.getStr("USER_NO");
			if (StringUtils.isNotBlank(user_no)) {
				sql.append(" and e.USER_NO like ?");
				paramLst.add("%"+user_no+"%");
			}
		}
		sql.append(" order by e.CREATE_TIME desc");
		Page<Evaluation> page = paginate(pageNum, pageSize, select.toString(), sql.toString(), paramLst.toArray());
		return page;
	}
}
