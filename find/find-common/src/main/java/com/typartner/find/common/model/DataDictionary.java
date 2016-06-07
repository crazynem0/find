package com.typartner.find.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.typartner.find.common.plugin.ParamInitPlugin;
import com.typartner.find.common.util.ToolCache;

@SuppressWarnings("serial")
@TableBind(tableName="data_dictionary")
public class DataDictionary extends Model<DataDictionary>{
	public static final DataDictionary dao=new DataDictionary();
	
	public List<DataDictionary> findAllDataDictionary(){
		return dao.find("select BIGTYPEID,BIGTYPENAME,SUBTYPEID,SUBTYPENAME,STATUS,CREATEOPTR,CREATETIME,MODIFYOPTR,MODIFYTIME,SORT_NO from iss_datadictionary  where status=?", "0");
	}
	
	/**
	 * 
	 * 功能描述:将码表数据放入map中
	 * @param listFunc
	 * @return
	 */
	public Map<String, String> getDataDictionaryOfTypeMap(final String type){
		
		Map<String, String> dataDictionaryMap = new TreeMap<String, String>();
		
		List<DataDictionary> listDataDictionary=getDataDictionaryOfType(type);
		
		for(DataDictionary dataDictionary:listDataDictionary){
			if(StringUtils.isBlank(dataDictionary.getStr("subtypeid"))){
				continue;
			}
			dataDictionaryMap.put(dataDictionary.getStr("subtypeid"), dataDictionary.getStr("subtypename"));
		}
		
		/*Map<String, String> dataDictionaryMap = CacheKit.get("dataDictionaryMap", type, new IDataLoader(){
			public Object load() {
				List<DataDictionary> listDataDictionary=getDataDictionaryOfType(type);
				Map<String, String> dataDictionaryMap = new HashMap<String, String>();
				for(DataDictionary dataDictionary:listDataDictionary){
					if(StringUtils.isBlank(dataDictionary.getStr("subtypeid"))){
						continue;
					}
					dataDictionaryMap.put(dataDictionary.getStr("subtypeid"), dataDictionary.getStr("subtypename"));
				}
				CacheKit.put("dataDictionaryMap", type, dataDictionaryMap);
				return dataDictionaryMap;
		}});*/
		return dataDictionaryMap;
	}
	
	/**
	 * 
	 * 功能描述:查找所有的功能菜单
	 * @return
	 */
	public List<DataDictionary> getDataDictionaryOfType(String type){
		String sql="select a.subtypeid,a.subtypename from data_dictionary a,data_bigtype b where a.bigtypeid = b.bigtypeid " 
				   + "and a.status='0' and b.bigtypecode = ? ";
		return DataDictionary.dao.find(sql,type);
	}
	
	
	public void cacheAdd(String bigTypeCode) {
		// TODO Auto-generated method stub
		List<DataDictionary> dictList=getDataDictionaryOfType(bigTypeCode);
		Map<String, String> dataDictionaryMap = new HashMap<String, String>();
		for(DataDictionary dataDictionary:dictList){
			if(StringUtils.isBlank(dataDictionary.getStr("subtypeid"))){
				continue;
			}
			dataDictionaryMap.put(dataDictionary.getStr("subtypeid"), dataDictionary.getStr("subtypename"));
		}
		ToolCache.set(ParamInitPlugin.cacheStart_dict + bigTypeCode, dataDictionaryMap);
	}

	public void cacheRemove(String bigTypeCode) {
		// TODO Auto-generated method stub
		ToolCache.remove(ParamInitPlugin.cacheStart_dict + bigTypeCode);
	}

	public Map<String,String> cacheGet(String bigTypeCode) {
		// TODO Auto-generated method stub
		Map<String,String> dict = ToolCache.get(ParamInitPlugin.cacheStart_dict + bigTypeCode);
		return dict;
	}
}
