package com.typartner.find.common.model;

import java.io.Serializable;
import java.util.List;

public class Parameters implements Serializable{

	private static final long serialVersionUID = -7114727840940754066L;
	
	private String type;//类型 shop 1;project 2
	private int num;
	private List<Shop> shopList;
	private List<Project> projectList;
	
	public List<Shop> getShopList() {
		return shopList;
	}
	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
	public List<Project> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	

}
