/**
 * 
 */
package com.typartner.find.common.util.map;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: BaiduGoeBaseRequest 
 * @Description: 
 * @author zhanglei
 * @date 2015年11月21日 下午12:13:45  
 */
public abstract class BaiduGeoBaseRequest {
	
	protected static final String SEARCH_BASE_URL = "http://api.map.baidu.com/geosearch/v3";
	protected static final String DATA_BASE_URL = "http://api.map.baidu.com/geodata/v3";
	
	protected String ak = "zuQq2siI0aIDU7HF72nOo3nD"; //用户key
	protected int geotable_id = 125039; //Geotable主键
	protected String sn; //用户的权限签名
	
	private static final List<String> allowParam = new ArrayList<String>();
	
	protected abstract String getBaseUrl();
	
	static{
		allowParam.add("");
	}
	
	/**
	 * 返回请求链接
	 * @return
	 */
	public abstract String getReqUrl();
	
	/**
	 * 返回请求内容
	 * @return
	 */
	public String getReqContent(){
		StringBuilder sb = new StringBuilder();
		getParentClassFields(sb, this.getClass());
		String content = sb.toString();
		if(content.length() > 0){
			content = content.substring(0, content.length() -1);
		}
		return content;
	}
	
	private void getParentClassFields(StringBuilder sb, @SuppressWarnings("rawtypes") Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		String fieldName = null;
		Object fieldValue = null;
		for(Field field : fields){
			field.setAccessible(true);//修改访问权限
			try {
				fieldName = field.getName();
				fieldValue = field.get(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fieldValue != null && fieldName.indexOf("URL") == -1){
				sb.append(fieldName).append("=").append(fieldValue).append("&");
			}
		}
		if (clazz.getSuperclass() == null) {
			return;
		}
		getParentClassFields(sb, clazz.getSuperclass());
	}
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getAk() {
		return ak;
	}
	public int getGeotable_id() {
		return geotable_id;
	}

}
