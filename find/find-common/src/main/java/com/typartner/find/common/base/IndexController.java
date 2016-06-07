/**
 * @ClassName:     IndexController.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         Administrator
 * @version        V1.0  
 * @Date           2015年11月8日 下午5:43:26 
 */
package com.typartner.find.common.base;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.ext.route.ControllerBind;
import com.typartner.find.common.util.FrontSessionUtil;

/**
 * <p>Title: IndexController</p>
 * <p>Description: TODO 首页的控制器</p>
 * @author: fangrui 
 * @date : 2015年11月8日 下午5:43:26
 */
@ControllerBind(controllerKey="/")
public class IndexController extends Controller {

	public void index(){
		//render("index.jsp");
	}
	
	public void logout(){
		FrontSessionUtil.clearSession(getRequest());
		render("index.jsp");
	}
}
