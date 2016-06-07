/**
 * 
 */
package com.typartner.find.common.util.map.data.poi.request;

/** 
 * @ClassName: BaiduGeoDataPoiDeleteRequest 
 * @Description: 
 * @author zhanglei
 * @date 2015年11月21日 下午2:53:48  
 */
public class BaiduGeoDataPoiDeleteRequest extends BaiduGeoDataPoiBaseRequest{
	
	private static final String SUB_URL = "/delete";
	
	private String id; //id
	private String ids; //id列表
	private String bounds; //查询的矩形区域

	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.BaiduGeoBaseRequest#getReqUrl()
	 */
	@Override
	public String getReqUrl() {
		return getBaseUrl() + SUB_URL;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getBounds() {
		return bounds;
	}


	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

}
