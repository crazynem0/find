/**
 * @ClassName:     UserInterceptor.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         fangrui
 * @version        V1.0  
 * @Date           2016年1月10日 下午4:33:38 
 */
package com.typartner.find.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.typartner.find.common.model.SysMember;
import com.typartner.find.common.util.FrontSessionUtil;

/**
 * <p>Title: UserInterceptor</p>
 * <p>Description: 对方法进行拦截，检验其是否已经登录</p>
 * @author: fangrui
 * @date : 2016年1月10日 下午4:33:38
 */

public class UserInterceptor implements Interceptor {

	public void intercept(Invocation invoc) {
		// TODO Auto-generated method stub
		Controller contro = invoc.getController();
		HttpServletRequest request = contro.getRequest();
		SysMember sysMember = FrontSessionUtil.getSession(request);
		if(sysMember == null || sysMember.getStr(SysMember.USER_NO) == null ){//表示用户未登录，需跳转到登录页面
			String toUrl = invoc.getActionKey();
			contro.setAttr("toUrl", toUrl);
			boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
			if(isAjax){
				contro.renderJson("{\"flag\":\"nologin\",\"toUrl\":\""+toUrl+"\"}");
			}else{
				contro.forwardAction(request.getContextPath());
			}
		}else{
			invoc.invoke();
		}
		
	}
	
}
