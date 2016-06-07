package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 连锁公司-类别 关系表 model
 * @author: YangKai
 */
@TableBind(tableName = "CHAIN_CAT")
public class ChainCat extends Model<ChainCat> {
	private static final long serialVersionUID = -2744048968336161191L;
	public static final ChainCat dao = new ChainCat();
	
	
	/**
	 * 根据 chainId 获取类别列表
	 * @param chainId
	 * @return
	 */
	public List<ChainCat> getLstByChainId(Integer chainId){
		return find("select * from chain_cat where CHAIN_ID=?", chainId);
	}
	
	/**
	 * 根据 chainId 删除记录
	 * @param chainId
	 */
	public void delByChainId(Integer chainId){
		Db.update("delete from chain_cat where CHAIN_ID=?", chainId);
	}
}
