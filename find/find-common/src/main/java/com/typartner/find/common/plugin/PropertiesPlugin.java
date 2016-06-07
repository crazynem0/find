package com.typartner.find.common.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.typartner.find.common.constant.ConstantInit;

/**
 * 读取Properties配置文件数据放入缓存
 */
public class PropertiesPlugin implements IPlugin {

    protected final Logger log = Logger.getLogger(getClass());

	/**
	 * 保存系统配置参数值
	 */
	private static Map<String, Object> paramMap = new HashMap<String, Object>();
	
    private Properties properties;

	public PropertiesPlugin(Properties properties){
		this.properties = properties;
	}

	/**
	 * 获取系统配置参数值
	 * @param key
	 * @return
	 */
	public static Object getParamMapValue(String key){
		return paramMap.get(key);
	}
	
	public boolean start() {
		paramMap.put(ConstantInit.db_type_key, properties.getProperty(ConstantInit.db_type_key).trim());
		
		// 判断数据库类型
		String db_type = (String) getParamMapValue(ConstantInit.db_type_key);
		if(db_type.equals(ConstantInit.db_type_mysql)){ // mysql 数据库连接信息
			// 读取当前配置数据库连接信息
			paramMap.put(ConstantInit.db_connection_driverClass, "com.mysql.jdbc.Driver");
			paramMap.put(ConstantInit.db_connection_jdbcUrl, properties.getProperty("jdbcUrl").trim());
			paramMap.put(ConstantInit.db_connection_userName, properties.getProperty("user").trim());
			paramMap.put(ConstantInit.db_connection_passWord, properties.getProperty("password").trim());

			// 解析数据库连接URL，获取数据库名称
			String jdbcUrl = (String) getParamMapValue(ConstantInit.db_connection_jdbcUrl);
			String database = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
			database = database.substring(database.indexOf("/") + 1, database.indexOf("?"));

			// 解析数据库连接URL，获取数据库地址IP
			String ip = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
			ip = ip.substring(0, ip.indexOf(":"));

			// 解析数据库连接URL，获取数据库地址端口
			String port = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
			port = port.substring(port.indexOf(":") + 1, port.indexOf("/"));
			
			// 把数据库连接信息写入常用map
			paramMap.put(ConstantInit.db_connection_ip, ip);
			paramMap.put(ConstantInit.db_connection_port, port);
			paramMap.put(ConstantInit.db_connection_dbName, database);
			
		}
		
		// 数据库连接池信息
		paramMap.put(ConstantInit.db_initialSize, Integer.valueOf(properties.getProperty(ConstantInit.db_initialSize).trim()));
		paramMap.put(ConstantInit.db_minIdle, Integer.valueOf(properties.getProperty(ConstantInit.db_minIdle).trim()));
		paramMap.put(ConstantInit.db_maxActive, Integer.valueOf(properties.getProperty(ConstantInit.db_maxActive).trim()));
		

		paramMap.put(ConstantInit.config_devMode, properties.getProperty(ConstantInit.config_devMode).trim());
		paramMap.put(ConstantInit.config_domain_key, properties.getProperty(ConstantInit.config_domain_key));
		//文件上传路径
		//paramMap.put(ConstantInit.upload_file_path, properties.getProperty(ConstantInit.upload_file_path));
		//与通云平台saas的相关参数
		
		
		// 缓存类型配置
		paramMap.put(ConstantInit.config_cache_type, properties.getProperty(ConstantInit.config_cache_type).trim());
		
		
		
		for (String key : paramMap.keySet()) {
			log.debug("全局参数配置：" + key + " = " + paramMap.get(key));
		}
		
		return true;
	}

	public boolean stop() {
		paramMap.clear();
		return true;
	}

}
