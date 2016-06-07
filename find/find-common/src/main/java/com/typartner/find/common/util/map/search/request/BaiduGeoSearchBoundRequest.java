/**
 * 
 */
package com.typartner.find.common.util.map.search.request;

/**
* @ClassName: BaiduGoeSearchBoundRequest 
* @Description: 矩形检索
* @author zhanglei
* @date 2015年11月20日 下午11:30:52
 */
public class BaiduGeoSearchBoundRequest extends BaiduGeoSearchCommonRequest{

	private static final String SUB_URL = "/bound";
	
	private String bounds; //矩形区域

	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.request.BaiduGoeSearchBaseRequest#getReqUrl()
	 */
	@Override
	public String getReqUrl() {
		return getBaseUrl() + SUB_URL;
	}

	public String getBounds() {
		return bounds;
	}

	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

}
