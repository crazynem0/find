package com.typartner.find.common.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.typartner.find.common.exception.ActionException;
import com.typartner.find.common.util.PageController;

/**
 * 
 * <p>Title: 补丁标签</p>
 * <p>Description: 用于列表页面，辅助提供公共的页面传值参数支撑</p>
 *
 */

public class PatchListTag extends TagSupport {
    private static final long serialVersionUID = -11105423532931404L;
    private static final Logger logger = Logger.getLogger(PatchListTag.class);
    /**
	 * @return 
	 * @throws JspException
	 */
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.write(make());
        }catch (ActionException e) {
        	logger.error("补丁标签生成发生action错误",e);
        } catch (IOException e) {
        	logger.error("补丁标签生成发生io错误",e);
        }
        return super.doEndTag();
    }

    /**
	 * @return 
	 */
    public String make() throws ActionException{
        try {
			StringBuffer sbuffer = new StringBuffer();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	        PageController pageController =(PageController) request.getAttribute("pageController");
			int currentPage = 1;
			String cookie=null;
			if (pageController!=null) {
				currentPage = pageController.getCurrentPage();
				cookie=pageController.getCookie();
			}
			
			sbuffer.append("<input type=\"hidden\" name=\"pageController.currentPage\" value=\"")
					.append(currentPage).append("\" id=\"currentPage\"/>\n");
			return sbuffer.toString();
		}catch (Exception e) {
			throw new ActionException("生成功能按钮发生业务错误......",e);
		}
    }
}
