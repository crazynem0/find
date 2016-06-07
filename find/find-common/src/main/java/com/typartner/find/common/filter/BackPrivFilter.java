package com.typartner.find.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.ty.partner.back.util.BackSessionUtil;
import com.typartner.find.common.plugin.ParamInitPlugin;
import com.typartner.find.common.util.ToolCache;

/**
 * 后台权限控制过滤器
 * @author YangKai
 */
public class BackPrivFilter implements Filter{
	private static Logger log = Logger.getLogger(BackPrivFilter.class);

    /**
     * 无权限页面
     */
    protected String noPrivPage = "";
    
    public void init(FilterConfig fconfig){
    	log.info("后台权限过滤器初始化 开始...");
    	noPrivPage = fconfig.getInitParameter("noPrivPage");
    	log.info("后台权限过滤器初始化 结束...");
    }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String pathInfo = req.getPathInfo() == null ? "" : req.getPathInfo();
        String url = req.getServletPath() + pathInfo;
        // 非后台请求，略过
        if (!url.startsWith("/back/")) {
        	chain.doFilter(request, response);
			return;
		}

        // 1.校验是否在priv表中，在则校验是否已登录
        @SuppressWarnings("unchecked")
        Map<String, Integer> bUrlMap = (HashMap<String, Integer>)ToolCache.get(ParamInitPlugin.CACHE_BACKURL);
        if (bUrlMap==null || !bUrlMap.containsKey(url)) {
        	chain.doFilter(request, response);
			return;
		}
        // 校验是否已登录
        if (!BackSessionUtil.isLoginBack(req)) {
        	noPrivReturn(req, res, NO_PRIV_TYPE.NO_LOGIN);
			return;
		}
        
        // 2.校验是否在当前登录用户的priv列表中，不在则返回no_priv
        Integer privId = bUrlMap.get(url);
        List<Integer> privIdLst = BackSessionUtil.getSession(req).getPrivIdLst();
        if (privIdLst==null || !privIdLst.contains(privId)) {
        	noPrivReturn(req, res, NO_PRIV_TYPE.NO_PRIV);
			return;
		}
        
        // 3.没啦，默认是通过，比如/login/方法
        chain.doFilter(request, response);
    }
    
	public static enum NO_PRIV_TYPE{
		/** 未登录 */
		NO_LOGIN,
		/** 无操作权限 */
		NO_PRIV;
	};
	
	public void noPrivReturn(HttpServletRequest request, HttpServletResponse response, NO_PRIV_TYPE t) throws IOException, ServletException{
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if(isAjax){
			Map<String, String> em = new HashMap<String, String>();
			em.put("flag", t.toString());
			JSONObject json = JSONObject.fromObject(em);
			response.getWriter().print(json);
			response.flushBuffer();
		}else{
			request.setAttribute("flag", t.toString());
			request.getRequestDispatcher(noPrivPage).forward(request, response);
		}
	}
    
    public void destroy(){}
}
