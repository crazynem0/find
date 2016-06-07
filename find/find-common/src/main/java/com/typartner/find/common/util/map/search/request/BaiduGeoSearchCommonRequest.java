/**
 * 
 */
package com.typartner.find.common.util.map.search.request;

/**
* @ClassName: BaiduGoeSearchCommonRequest 
* @Description: 搜索请求共同参数提取
* @author zhanglei
* @date 2015年11月20日 下午11:32:12
 */
public abstract class BaiduGeoSearchCommonRequest extends BaiduGeoSearchBaseRequest{

	private String q; //检索关键字
	private String tags; //检索标签
	private String sortby; //排序字段
	private String filter; //过滤条件
	private String page_index; //分页索引
	private String page_size; //分页数量
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSortby() {
		return sortby;
	}
	public void setSortby(String sortby) {
		this.sortby = sortby;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getPage_index() {
		return page_index;
	}
	public void setPage_index(String page_index) {
		this.page_index = page_index;
	}
	public String getPage_size() {
		return page_size;
	}
	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}
}
