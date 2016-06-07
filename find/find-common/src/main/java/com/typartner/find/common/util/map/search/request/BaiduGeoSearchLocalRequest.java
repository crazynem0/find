/**
 * 
 */
package com.typartner.find.common.util.map.search.request;


/**
* @ClassName: BaiduGoeSearchLocalRequest 
* @Description: 本地检索
* @author zhanglei
* @date 2015年11月20日 下午11:36:26
 */
public class BaiduGeoSearchLocalRequest extends BaiduGeoSearchCommonRequest{
	private static final String SUB_URL = "/local";
	
	private String region; //区域名称

	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.request.BaiduGoeSearchBaseRequest#toRequestUrl()
	 */
	@Override
	public String getReqUrl() {
		return getBaseUrl() + SUB_URL;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
