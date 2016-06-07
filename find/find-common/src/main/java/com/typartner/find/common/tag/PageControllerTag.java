package com.typartner.find.common.tag;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.typartner.find.common.util.PageController;
import com.typartner.find.common.util.StringUtil;

/**
 * 
 * <p>Title: 翻页标签</p>
 * <p>Description: 翻页标签</p>
 *
 */

public class PageControllerTag extends TagSupport {
    private static final long serialVersionUID = -8049479179986983934L;
    private static final Logger logger = Logger.getLogger(PageControllerTag.class);
    private String name;	//
    private String formId;
    private String qryMethod;
    private String scope = "request";
    private boolean refresh = true;

    /**
	 * @return 
	 * @throws JspException
	 */
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        String str = make();
        try {
            out.write(str);
        } catch (IOException e) {
        	logger.error("分页标签生成发生io错误",e);
        }
        return super.doEndTag();
    }
    
    /**
	 * @return 
	 */
    public String make() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        PageController pageController =(PageController)request.getAttribute("pageController");
        int totalPages = pageController.getTotalPages();
        int currentPage = pageController.getCurrentPage();
        
        /*StringBuilder sbuffer = new StringBuilder("<tr valign=\"center\" align=\"middle\"><td width=\"250\">&nbsp;");
        sbuffer.append(currentPage).append("/").append(totalPages).append("&nbsp;页，&nbsp;总共").append(pageController.getTotalRows()).append("条记录");
        sbuffer.append("</td><td>&nbsp;</td><td>");*/
        StringBuilder sbuffer = new StringBuilder("<ul class=\"pageul\">");
        
        name = name+".currentPage";
        String id="currentPage";
      
        
        //如果指定了要执行的js方法，则执行该方法，否则直接提交表单
        if(!StringUtil.isBlank(qryMethod)){
        	formId=qryMethod;
        }else
        	formId = "document.getElementById('" + formId + "')"+".submit()";
        
        if (this.refresh) {
            sbuffer.append("<li>【<a href=\"javascript:").append(formId).append("\">刷新</a>】</li>\n");
        }
        if (pageController.isHasPreviousPage()) {
            sbuffer.append("<li>【 <a href=\"javascript:document.getElementById('currentPage').value=1;")
                .append(formId).append("\"> 首页</a>\n");
            sbuffer.append("<a href=\"javascript:document.getElementById('").append(id).append("').value=")
                .append(pageController.getPreviousPage()).append(";")
                .append(formId).append("\">上页</a>\n");
        } else {
        	sbuffer.append("<li> 【 首页 上页\n");
        }
        if (pageController.isHasNextPage()) {
        	sbuffer.append("<a href=\"javascript:document.getElementById('").append(id).append("').value=")
	    		.append(pageController.getNextPage()).append(";")
	            .append(formId).append("\">下页</a>\n");
        	sbuffer.append("<a href=\"javascript:document.getElementById('").append(id).append("').value=")
                .append(pageController.getTotalPages()).append(";")
                .append(formId).append("\">末页</a> 】 </li>\n");
        } else {
        	sbuffer.append(" 下页 末页 】</li> \n");
        }
        sbuffer.append("<li>共 ").append(pageController.getTotalRows())
            .append(" 条</li><li>第 <select size=\"1\" onchange=\"javascript:document.getElementById('")
            .append(id).append("').value=this.value;")
            .append(formId).append("\">\n");

        

        for (int i = 1; i <= totalPages; i++) {
            if (i == currentPage) {
                sbuffer.append("<option value=\"" + i + "\" selected>" + i + "</options>\n");
            } else {
            	sbuffer.append("<option value=\"" + i + "\">" + i + "</options>\n");
            }
        }
        sbuffer.append("</select>/ " + totalPages +  "页</li></ul>");
        return sbuffer.toString();
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String form) {
        this.formId = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRefresh() {
        return refresh;
    }
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

	public String getQryMethod() {
		return qryMethod;
	}

	public void setQryMethod(String qryMethod) {
		this.qryMethod = qryMethod;
	}
}
