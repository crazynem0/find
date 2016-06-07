package com.typartner.find.common.base;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.typartner.find.common.plugin.ParamInitPlugin;
import com.typartner.find.common.plugin.PropertiesPlugin;

/**
 * <p>Title: CommonConfig</p>
 * <p>Description: 公共 config</p>
 * @author: fangrui 
 * @date : 2015年11月5日 上午10:20:31
 */

public class CommonConfig extends JFinalConfig {

	private boolean isDev=false;
	@SuppressWarnings("unused")
	private Routes routes;

	@Override
	public void configConstant(Constants me) {
		new PropertiesPlugin(loadPropertyFile("config.properties")).start();
		isDev=getPropertyToBoolean("devMode", false);//读取配置文件内容
		me.setViewType(ViewType.JSP);
	    me.setBaseViewPath("WEB-INF/page/");
	    me.setBaseUploadPath(getProperty("uploadDir"));
	}

	@Override
	public void configRoute(Routes me) {
		this.routes = me;
		//me.add("/",IndexController.class);
		me.add(new AutoBindRoutes());
	}

	@Override
	public void configPlugin(Plugins me) {
		//DruidPlugin druid=new DruidPlugin(getProperty("jdbcUrl"), Encoding.decoding(getProperty("user")), Encoding.decoding(getProperty("password").trim()));
		DruidPlugin druid=new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		druid.setInitialSize(1);	//初始连接数
		druid.setMaxActive(5);		//最大连接池数量
		druid.setMinIdle(1);		//最小连接池数量
		druid.setMaxWait(60000);	//获取连接最大等待时间
		druid.setFilters("stat");
		me.add(druid);
		
		// 配置AutoTableBindPlugin插件
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(druid,SimpleNameStyles.LOWER_UNDERLINE);
		if (isDev) atbp.setShowSql(true);
		atbp.autoScan(false);	//关闭自动扫描
		atbp.setContainerFactory(new CaseInsensitiveContainerFactory()); //配置属性名(字段名)大小写不敏感容器工厂
		me.add(atbp);
		
		// sql记录
		SqlReporter.setLog(true);
		//缓存参数
		me.add(new EhCachePlugin());// 要使用ehcache必须先加载ehcache插件
		me.add(new ParamInitPlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {
		//me.add(new ShiroInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler());
		me.add(new UploadServerUrlHandler());
		//me.add(new UrlSkipHandler(".+\\.\\w{1,4}", false));
	}

	public static void main(String[] args) {
		JFinal.start("WebRoot", 8100, "/simplify-site", 5);
	}
}
