/**
 * 
 */
package com.typartner.find.common.util.map.search.response;

import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: BaiduGoeSearchResponse 
 * @Description: 
 * @author zhanglei
 * @date 2015年11月20日 下午11:41:35  
 */
public class BaiduGeoSearchResponse{
	
	private Integer status;
	private Integer size;
	private Integer total;
	private String message;
	private List<BaiduGeoSearchResponseContent> contents = new ArrayList<BaiduGeoSearchResponseContent>();
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<BaiduGeoSearchResponseContent> getContents() {
		return contents;
	}
	public void setContents(List<BaiduGeoSearchResponseContent> contents) {
		this.contents = contents;
	}
	protected String getMessage() {
		return message;
	}
	protected void setMessage(String message) {
		this.message = message;
	}
}
