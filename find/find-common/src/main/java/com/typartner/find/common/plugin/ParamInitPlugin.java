package com.typartner.find.common.plugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.ty.partner.back.system.model.BackPriv;
import com.typartner.find.common.model.DataDictionary;
import com.typartner.find.common.util.ToolCache;

/**
 * 系统初始化缓存操作类
 */
public class ParamInitPlugin implements IPlugin {
	
	private static Logger log = Logger.getLogger(ParamInitPlugin.class);
	 
	/**
     * 功能缓存key前缀
     */
	public static String cacheStart_operator = "operator_";
    
	/**
     * 字典缓存key前缀
     */
	public static String cacheStart_dict = "dict_";
    
	/**
     * 菜单缓存key前缀
     */
	public static String cacheStart_func = "func_";
	
	/**
	 * 后台功能key值
	 */
	public static final String CACHE_BACKFUNC = "back_func";
	/**
	 * 后台功能key值
	 */
	public static final String CACHE_BACKURL = "back_url";
    

	public boolean start() {
		log.info("缓存参数初始化 start ...");

		// 1.缓存功能
		//cacheOperator();

		// 2.缓存字典
		cacheDict();

		// 3.缓存菜单
		//cacheFunc();

		// 缓存后台权限列表
		cacheBackFunc();
		log.info("缓存参数初始化 end ...");
		return true;
	}

	public boolean stop() {
		return false;
	}


	/**
	 * 缓存操作
	 */
	/*public static void cacheOperator() {
		log.info("缓存加载：Operator start");
		List<SysOperator> operatorList = SysOperator.dao.findAll();
		for (SysOperator operator : operatorList) {
			operator.dao.cacheAdd(operator.getPKValue());
			operator = null;
		}
		log.info("缓存加载：Operator end, size = " + operatorList.size());
		operatorList = null;
	}*/

	/**
	 * 缓存业务字典
	 */
	public static void cacheDict() {
		log.info("缓存加载：Dict start");
		List<Record> dictList=Db.find("select * from data_bigtype");
		for (Record dict : dictList) {
			DataDictionary.dao.cacheAdd(dict.getStr("bigtypecode"));
			dict = null;
		}
		log.info("缓存加载：Dict end, size = " + dictList.size());
		dictList = null;
	}

	/**
	 * 缓存菜单数据
	 */
	/*public static void cacheFunc() {
		SysFunc.dao.cacheAdd();
	}*/

	/**
	 * 缓存后台权限
	 */
	public static void cacheBackFunc(){
		log.info("缓存加载：后台权限 start");
		List<BackPriv> backPrivLst = BackPriv.dao.getFuncLst(BackPriv.STATUS_ON);
		if (backPrivLst!=null && backPrivLst.size()>0) {
			Map<Integer, BackPriv> bpMap = new HashMap<Integer, BackPriv>();
			Map<String, Integer> bUrlMap = new HashMap<String, Integer>();
			for (BackPriv bp : backPrivLst) {
				if (bp != null) {
					bpMap.put(bp.getInt("ID"), bp);
					if (StringUtils.isNotBlank(bp.getStr("FUNC_URL"))) {
						bUrlMap.put(bp.getStr("FUNC_URL"), bp.getInt("ID"));
					}
				}
			}
			ToolCache.set(CACHE_BACKFUNC, bpMap);
			ToolCache.set(CACHE_BACKURL, bUrlMap);
		}
		log.info("缓存加载：后台权限 end, size = " + backPrivLst.size());
	}
}
