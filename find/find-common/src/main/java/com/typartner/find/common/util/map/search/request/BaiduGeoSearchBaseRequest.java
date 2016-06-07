/**
 * 
 */
package com.typartner.find.common.util.map.search.request;

import com.typartner.find.common.util.map.BaiduGeoBaseRequest;

/**
* @ClassName: BaiduGoeSearchBaseRequest 
* @Description: LBS搜索请求基类
* @author zhanglei
* @date 2015年11月20日 下午11:29:56
 */
public abstract class BaiduGeoSearchBaseRequest extends BaiduGeoBaseRequest{
	protected String coord_type; //坐标系
	
	@Override
	protected String getBaseUrl() {
		return SEARCH_BASE_URL;
	}
	
	public String getCoord_type() {
		return coord_type;
	}

	public void setCoord_type(String coord_type) {
		this.coord_type = coord_type;
	}

}
