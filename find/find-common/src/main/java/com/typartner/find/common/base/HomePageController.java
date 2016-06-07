package com.typartner.find.common.base;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.typartner.find.common.model.News;
import com.typartner.find.common.model.Project;
import com.typartner.find.common.model.Shop;

/**
 * @author Hety
 *
 */
@ControllerBind(controllerKey="/home",viewPath="/")
public class HomePageController extends Controller{
	
	/**
	 * 跳转到欢迎页面
	 */
	public void toWelcome(){
		render("welcome.jsp");
	}
	
	/**
	 * 跳转到主页面
	 */
	public void index(){
		render("index.jsp");
	}

	/**
	 * 展示商铺
	 */
	public void showMain(){
		setAttr("shopList", Shop.dao.shopMain());
		renderJson();
	}
	
	/**
	 * 展示项目
	 */
	public void showProMain(){
		setAttr("projectList",Project.dao.proMain());
		renderJson();
	}
	
	/**
	 * 站长推荐
	 */
	public void showRecomShop(){
		setAttr("shopList", Shop.dao.shopRecommend());
		renderJson();
	}
	
	/**
	 * 展示首页新闻
	 */
	public void showNews(){
		setAttr("newsList", News.dao.showNews());
		renderJson();
	}
}
