package com.typartner.find.common.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.typartner.find.common.model.DataDictionary;




/**
 * 本地数据初始化化类，同时提供刷新方法
 */
public class DataInitUtil {
	/**
	 * 数据字典 key:大类id value:子类id,子类value
	 */
	private static Map<Integer, Map> dataLibrary=null; 
	
	
	
	/**
	 * 获取数据字典数据
	 */
	public static Map<Integer, Map> getDataLibrary() {
		if(dataLibrary==null){
			dataLibrary=new TreeMap<Integer, Map>();
			
			List<DataDictionary> bigTypeList = DataDictionary.dao.find("select * from d_datadictionary  where status=?", "0");
			
			/*List<DataDictionary> bigTypeList = CacheKit.get("dictCache", "dataLibraryList", new IDataLoader(){
				public Object load() {
					return DataDictionary.dao.find("select * from d_datadictionary  where status=?", "0");
				}
			});*/
			
			Map<String, String> map = null;
			for(int i=bigTypeList.size()-1;i>=1;i--){
				if(dataLibrary.get(bigTypeList.get(i).getInt("bigtypeid"))!=null){
					bigTypeList.remove(i);
					continue;
				}
				map = new TreeMap<String, String>();
				for(int j=0;j<=i;j++){
					if(bigTypeList.get(i).getInt("bigtypeid").intValue()==bigTypeList.get(j).getInt("bigtypeid").intValue()){
						map.put(bigTypeList.get(j).getStr("subtypeid"),bigTypeList.get(j).getStr("subtypename"));
					}
				}
				dataLibrary.put(bigTypeList.get(i).getInt("bigtypeid"), map);
				bigTypeList.remove(i);
			}
			//CacheKit.put("dictCache", "dataLibraryList", bigTypeList);
		}
		return dataLibrary;
	}
	
	// 初始化数据
	public static void initData() {
		//getOrganizeList();
		//makeOrganizeTree();
		getDataLibrary();
	}

}