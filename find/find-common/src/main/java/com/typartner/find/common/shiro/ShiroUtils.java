/*
 *  Copyright 2014-2015 snakerflow.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.typartner.find.common.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.typartner.find.common.model.SysMember;
import com.typartner.find.common.model.SysUser;

/**
 * shiro工具类
 * @author yuqs
 * @since 0.1
 */
public class ShiroUtils {
	/**
	 * 返回当前登录的认证实体ID
	 * @return
	 */
	public static Integer getUserId() {
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getId();
		return -1;
	}
	
	public static SysMember getUser() {
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getUser();
		return null;
	}
	
	/**
	 * 返回当前登录的认证实体部门ID
	 * @return
	 */
	/*public static Integer getOrgId() {
		SysUser user = getUser();
		Integer org = user.getInt("org");
		if(user != null && org != null) return org;
		return -1;
	}*/
	
	/**
	 * 获取当前登录的认证实体
	 * @return
	 */
	public static ShiroPrincipal getPrincipal() {
		Subject subject = SecurityUtils.getSubject();
		return (ShiroPrincipal)subject.getPrincipal();
	}
	
	/**
	 * 获取所有组集合
	 * @return
	 */
	/*public static List<String> getGroups() {
		List<String> groups = new ArrayList<String>();
		Integer orgId = getOrgId();
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) {
			groups.addAll(principal.getRoles());
		}
		if(orgId != null) {
			groups.add(String.valueOf(orgId));
		}
		return groups;
	}*/
	
	/**
	 * 获取当前认证实体的中文名称
	 * @return
	 */
	public static String getFullname() {
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.toString();
		return "";
	}
	
	/**
	 * 获取当前认证实体的登录账号
	 * @return
	 */
	public static String getLoginNo() {
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getLoginNo();
		throw new RuntimeException("user's loginNO is null.");
	}
	
	/**
	 * 获取当前认证实体的登录名称
	 * @return
	 */
	public static String getUsername() {
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getLoginNo();
		throw new RuntimeException("user's name is null.");
	}
	
	/**
	 * 获取当前认证的实体部门名称
	 * @return
	 */
	/*public static String getOrgName() {
		SysUser user = getUser();
		if(user != null) return user.get("orgName");
		return "";
	}*/
	
	/**
	 * 功能描述:获取当前用户的公司标识
	 * @return
	 */
	/*public static String getCompany(){
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getCompany();
		throw new RuntimeException("user's company is null.");
	}*/
	
	/**
	 * 功能描述:获取当前用户的公司标识
	 * @return
	 */
	public static String getUserType(){
		ShiroPrincipal principal = getPrincipal();
		if(principal != null) return principal.getUserType();
		throw new RuntimeException("user's company is null.");
	}
}
