/**
 * 
 */
package com.typartner.find.common.util.map.data.poi.request;

/** 
 * @ClassName: BaiduGeoDataPoiUpdateRequest 
 * @Description: 
 * @author zhanglei
 * @date 2015年11月21日 下午2:42:16  
 */
public class BaiduGeoDataPoiUpdateRequest extends BaiduGeoDataPoiBaseRequest{

	private static final String SUB_URL = "/update";
	
	private String id;
	private String address; //地址
	private String latitude; //纬度
	private String longitude; //经度
	private String coord_type; //坐标的类型
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCoord_type() {
		return coord_type;
	}

	public void setCoord_type(String coord_type) {
		this.coord_type = coord_type;
	}

}
