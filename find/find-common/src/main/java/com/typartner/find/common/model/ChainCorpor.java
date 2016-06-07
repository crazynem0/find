package com.typartner.find.common.model;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ty.partner.back.util.SpellSqlUtil;
import com.typartner.find.common.model.UserProductRec.PRODUCT_TYPE;

/**
 * <p>Title: ChainCorpor</p>
 * <p>Description: 连锁公司表 model </p>
 * @author: fangrui
 * @date : 2016年1月26日 下午11:09:22
 */
@TableBind(tableName = "CHAIN")
public class ChainCorpor extends Model<ChainCorpor> {
	
	private static final long serialVersionUID = 7789590308460475975L;
	private static final String[] sensitiveInfoArr = {"PHONE_NO","WECHAT"}; //敏感信息，需付费查看
	private static final String hiddenReplace = "*";

	/**
	 * 连锁公司状态 枚举
	 */
	public static enum CHAIN_STATUS{
		/** 草稿 */
		PAPER(1),
		/** 已发布 */
		RELEASE(5),
		/** 已下架 */
		OFF(7);
		
		private int type;
		private CHAIN_STATUS(int type){
			this.type = type;
		}
		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
		public int toInt(){
			return this.type;
		}
	};
	
	public static final ChainCorpor dao = new ChainCorpor();
	
	//根据查询条件获取数据，支持分页
	public Page<ChainCorpor> findByCondition(int pageNum, int pageSize,Integer type, Integer minArea, Integer maxArea, String userNo){
		Page<ChainCorpor> chainPageLst = null;
		StringBuffer sb = new StringBuffer("select a.ID, a.CHAIN_NAME, a.CONTACTS, a.PHONE_NO, a.WECHAT, a.MIN_AREA, a.MAX_AREA,  left(a.CHAIN_DESC,15) as CHAIN_DESC, a.WECHAT_IMG ");
		StringBuffer sql = new StringBuffer(" from chain a, chain_cat b where a.id = b.CHAIN_ID and a.STATUS = 5");
		if(type != null ){
			sql.append(" and b.CAT_ID = ").append(type);
		}
		
		if(minArea != null ){
			sql.append(" and a.MIN_AREA >= ").append(minArea);
		}
		
		if(maxArea != null ){
			sql.append(" and a.MAX_AREA <= ").append(maxArea);
		}
		
		sql.append(" group by a.id having max(a.id) ");
		
		chainPageLst = paginate(pageNum, pageSize, sb.toString(), sql.toString());
		List<ChainCorpor> chainLst = chainPageLst.getList();
		//区分是否已购买
		if(chainLst!=null && chainLst.size() > 0){
			int chairId;
			for(ChainCorpor chair: chainLst){
				chairId = chair.getInt("ID");
				if(!UserProductRec.dao.hasRec(userNo, chairId, PRODUCT_TYPE.CHAIN)){ //无此条购买记录
					//chainLst.remove(chair);//由于要修改元素，需先移除再增加
					for(String info : sensitiveInfoArr){
						chair.set(info, hiddenInfo(chair.getStr(info)));
					}
					//chainLst.add(chair);
				}
			}
		}
		
		return chainPageLst;
	}
	

	/**
	 * 隐藏信息
	 * @param source 原始信息
	 * @return
	 */
	private String hiddenInfo(String source){
		int hiddenStrLength = source.length() / 2;
		int startIndex = (source.length() - hiddenStrLength) / 2;
		int endIndex = startIndex + hiddenStrLength;
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(char c : source.toCharArray()){
			if(index >= startIndex && index <= endIndex){
				sb.append(hiddenReplace);
			}else{
				sb.append(c);
			}
			index++;
		}
		return sb.toString();
	}
	
	/**
	 * 获取 分页数据（后台）
	 * @param pageNum
	 * @param pageSize
	 * @param qryIn
	 * @return
	 */
	public Page<ChainCorpor> getPage(int pageNum, int pageSize, ChainCorpor qryIn){
		String select = "select *";
		StringBuffer sql = new StringBuffer("from chain where 1=1");
		if (qryIn != null) {
			String chainName = qryIn.getStr("CHAIN_NAME");
			if (StringUtils.isNotBlank(chainName)) {
				sql.append(SpellSqlUtil.spellAndLike("CHAIN_NAME", chainName));
			}
			String contacts = qryIn.getStr("CONTACTS");
			if (StringUtils.isNotBlank(contacts)) {
				sql.append(SpellSqlUtil.spellAndLike("CONTACTS", contacts));
			}
			String phoneNo = qryIn.getStr("PHONE_NO");
			if (StringUtils.isNotBlank(phoneNo)) {
				sql.append(SpellSqlUtil.spellAndLike("PHONE_NO", phoneNo));
			}
			Integer status = qryIn.getInt("STATUS");
			if (status != null) {
				sql.append(SpellSqlUtil.spellAndEqual("STATUS", status));
			}
		}
		sql.append(" order by CREATE_TIME desc");
		Page<ChainCorpor> page = paginate(pageNum, pageSize, select, sql.toString());
		return page;
	}
}
