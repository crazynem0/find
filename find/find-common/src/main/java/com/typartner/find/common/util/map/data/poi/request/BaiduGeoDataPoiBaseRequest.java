/**
 * 
 */
package com.typartner.find.common.util.map.data.poi.request;

import com.typartner.find.common.util.map.BaiduGeoBaseRequest;

/** 
 * @ClassName: BaiduGoeDataPoiBaseRequest 
 * @Description: LBS数据POI请求基类
 * @author zhanglei
 * @date 2015年11月21日 上午11:55:49  
 */
public abstract class BaiduGeoDataPoiBaseRequest extends BaiduGeoBaseRequest{
	protected static final String POI_SUB_URL = "/poi";
	
	protected String title;
	protected String tags;
	
	@Override
	protected String getBaseUrl() {
		return DATA_BASE_URL + POI_SUB_URL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	
}
