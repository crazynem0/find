package com.typartner.find.common.util.map.search.request;

/**
* @ClassName: BaiduGoeSearchNearbyRequest 
* @Description: 周边检索
* @author zhanglei
* @date 2015年11月20日 下午11:38:11
 */
public class BaiduGeoSearchNearbyRequest extends BaiduGeoSearchCommonRequest{
	private static final String SUB_URL = "/nearby";
	
	private String location; //检索中心点
	private String radius; //检索半径
	
	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.request.BaiduGoeSearchBaseRequest#getReqUrl()
	 */
	@Override
	public String getReqUrl() {
		return getBaseUrl() + SUB_URL;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

}
