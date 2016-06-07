/**
 * 
 */
package com.typartner.find.common.util.map.search.request;

/**
* @ClassName: BaiduGoeSearchDetailRequest 
* @Description: poi详情
* @author zhanglei
* @date 2015年11月20日 下午11:34:01
 */
public class BaiduGeoSearchDetailRequest extends BaiduGeoSearchBaseRequest{

	private static final String SUB_URL = "/detail";
	
	private String uid; //uid为poi点的id号

	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.request.BaiduGoeSearchBaseRequest#getReqUrl()
	 */
	@Override
	public String getReqUrl() {
		return  getBaseUrl() + SUB_URL + "/" + uid;
	}

	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}

}
