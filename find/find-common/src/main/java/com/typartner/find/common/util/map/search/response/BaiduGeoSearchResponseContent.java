/**
 * 
 */
package com.typartner.find.common.util.map.search.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @ClassName: BaiduGoeSearchResponseContent 
* @Description: LBS搜索返回信息
* @author zhanglei
* @date 2015年11月20日 下午11:55:12
 */
public class BaiduGeoSearchResponseContent{

	private Integer uid; //数据id
	private Integer geotable_id; //geotable_id
	private String title; //Poi名称
	private String address; //Poi地址
	private String province; //Poi所属省
	private String city; //Poi所属城市
	private String district; //Poi所属区
	private List<BigDecimal> location; //经纬度
	private Integer coord_type; //坐标系定义
	private String tags; //Poi的标签
	private Integer distance; //距离
	private Integer weight; //权重
	private Date modify_time; 
	private Date create_time;
	private Integer type;
	
	private String extra_address; //地址补充
	private Integer price; //金额
	private Integer area; //面积
	private Integer shop_type; //商铺类型
	private Integer out_type; //转出类别
	private Integer industry; //行业
	private Integer need_3d; //是否有全景图
	private Integer county_id; //区
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public List<BigDecimal> getLocation() {
		return location;
	}
	public void setLocation(List<BigDecimal> location) {
		this.location = location;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getGeotable_id() {
		return geotable_id;
	}
	public void setGeotable_id(Integer geotable_id) {
		this.geotable_id = geotable_id;
	}
	public Integer getCoord_type() {
		return coord_type;
	}
	public void setCoord_type(Integer coord_type) {
		this.coord_type = coord_type;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getExtra_address() {
		return extra_address;
	}
	public void setExtra_address(String extra_address) {
		this.extra_address = extra_address;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getArea() {
		return area;
	}
	public void setArea(Integer area) {
		this.area = area;
	}
	public Integer getShop_type() {
		return shop_type;
	}
	public void setShop_type(Integer shop_type) {
		this.shop_type = shop_type;
	}
	public Integer getOut_type() {
		return out_type;
	}
	public void setOut_type(Integer out_type) {
		this.out_type = out_type;
	}
	public Integer getIndustry() {
		return industry;
	}
	public void setIndustry(Integer industry) {
		this.industry = industry;
	}
	public Integer getNeed_3d() {
		return need_3d;
	}
	public void setNeed_3d(Integer need_3d) {
		this.need_3d = need_3d;
	}
	public Integer getCounty_id() {
		return county_id;
	}
	public void setCounty_id(Integer county_id) {
		this.county_id = county_id;
	}
}
