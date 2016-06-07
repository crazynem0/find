package com.typartner.find.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.typartner.find.common.constant.CommonConstants;
import com.typartner.find.common.model.Parameters;
import com.typartner.find.common.model.SysMember;

/**
 * 前台管理系统 Session Tools
 * @author YangKai
 */
public class FrontSessionUtil {
	private static final Logger log = Logger.getLogger(FrontSessionUtil.class);
	
	/**
	 * 设置 BackSession
	 * @param request
	 * @param sessionBean
	 * @return void
	 */
	public static boolean setSession(HttpServletRequest request, SysMember user){
		// 如果传入的 backUser为空，则作为异常处理，直接返回
		if (user==null || StringUtils.isBlank(user.getStr(SysMember.USER_NO))) {
			log.info("初始化会员管理系统所用用户对象为空，初始化失败，直接返回");
			return false;
		}
		String userNo = user.getStr(SysMember.USER_NO);
		
		log.info("初始化会员管理用户信息 完成[userNo="+userNo+"]");
		
		// 设置session值
		request.getSession().setAttribute(CommonConstants.FRONTSESSION, user);
		log.info("初始化会员管理用户session 完成[userNo="+userNo+"]");
		return true;
	}
	
	/**
	 * 获取 BackSession
	 * @return BackUser
	 */
	public static SysMember getSession(HttpServletRequest request){
		SysMember frontSess = (SysMember)request.getSession().getAttribute(CommonConstants.FRONTSESSION);
		if(frontSess == null){
			frontSess = new SysMember();
			request.getSession().setAttribute(CommonConstants.FRONTSESSION, frontSess);
		}
		return frontSess;
	}

	/**
	 * 清除 BackSession
	 */
	public static void clearSession(HttpServletRequest request){
		request.getSession().removeAttribute(CommonConstants.FRONTSESSION);
	}
	
	/**
	 * 判断是否登录状态
	 * @param request
	 * @return boolean
	 */
	public static boolean isLogin(HttpServletRequest request){
		SysMember frontSess = getSession(request);
		// 判断session不为空，且session中存放的 用户对象不为空，且其USER_NO属性也不为空
		if (frontSess!=null
				&& StringUtils.isNotBlank(frontSess.getStr(SysMember.USER_NO))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取登录用户.USER_NO
	 * @param request
	 * @return userNo/""
	 */
	public static String getUserNo(HttpServletRequest request){
		SysMember frontUser = getSession(request);
		if (frontUser!=null && StringUtils.isNotBlank(frontUser.getStr(SysMember.USER_NO))) {
			return frontUser.getStr(SysMember.USER_NO);
		} else {
			return "";
		}
	}
	
	/**
	 * 获取登录用户.USER_NAME
	 * @param request
	 * @return userNo/""
	 */
	public static String getUserName(HttpServletRequest request){
		SysMember frontUser = getSession(request);
		if (frontUser!=null && StringUtils.isNotBlank(frontUser.getStr(SysMember.USER_NAME))) {
			return frontUser.getStr(SysMember.USER_NAME);
		} else {
			return "";
		}
	}
	
	/**
	 * 获取对比栏
	 */
	public static Parameters getCom(HttpServletRequest request){
		Parameters list = (Parameters) request.getSession().getAttribute(CommonConstants.COMPARISON);
		if(list == null){
			list = new Parameters();
			request.getSession().setAttribute(CommonConstants.COMPARISON,list);
		}
		return list;
	}
}
