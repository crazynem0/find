/**
 * 
 */
package com.typartner.find.common.util.map.data.poi.request;

/** 
 * @ClassName: BaiduGeoDataPoiCreateRequest 
 * @Description: 创建数据
 * @author zhanglei
 * @date 2015年11月21日 下午2:33:52  
 */
public class BaiduGeoDataPoiCreateRequest extends BaiduGeoDataPoiBaseRequest{
	
	private static final String SUB_URL = "/create";
	
	private String address; //地址
	private String latitude; //纬度
	private String longitude; //经度
	private String coord_type; //坐标的类型
	
	/*自定义参数*/
	private String extra_address; //地址补充
	private String area; //面积
	private String price; //金额
	private String shop_type; //商铺类型
	private String out_type; //转出类型
	private String industry; //行业
	private String need_3d; //是否有全景图
	private String county_id; //区
	
	
	/* (non-Javadoc)
	 * @see com.typartner.find.common.util.map.BaiduGeoBaseRequest#getReqUrl()
	 */
	@Override
	public String getReqUrl() {
		return getBaseUrl() + SUB_URL;
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

	public String getExtra_address() {
		return extra_address;
	}

	public void setExtra_address(String extra_address) {
		this.extra_address = extra_address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}

	public String getOut_type() {
		return out_type;
	}

	public void setOut_type(String out_type) {
		this.out_type = out_type;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getNeed_3d() {
		return need_3d;
	}

	public void setNeed_3d(String need_3d) {
		this.need_3d = need_3d;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

}
