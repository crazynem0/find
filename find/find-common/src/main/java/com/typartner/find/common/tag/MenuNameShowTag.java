package com.typartner.find.common.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.ty.partner.busi.system.model.SysFunc;
import com.typartner.find.common.exception.ActionException;

/**
 * 
 * <p>Title: 补丁标签</p>
 * <p>Description: 用于展示所选菜单的所有父菜单</p>
 *
 */

public class MenuNameShowTag extends TagSupport {
    private static final long serialVersionUID = -11105423532931404L;
    private static final Logger logger = Logger.getLogger(MenuNameShowTag.class);
    private String funcCode;
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
			funcName(sbuffer,funcCode);
			return sbuffer.toString();
		}catch (Exception e) {
			throw new ActionException("生成菜单描述串发生业务错误......",e);
		}
    }

    public void funcName(StringBuffer sb,String funcCode){
    	Map<String, SysFunc> funcMap=SysFunc.dao.getAllFuncMap();
    	SysFunc func=funcMap.get(funcCode);
    	if(func!=null){
	    	if(func.getStr("parent_func_code").equals("F")){
	    		sb.insert(0,"<ul class=\"breadcrumb\"><li><i class=\"icon-home home-icon\"></i><a href='#'>"+func.getStr("func_name")+"</a></li>");
	    		return;
	    	}else if(func.getStr("func_type").equals("0")){ //有叶结点
	    		sb.insert(0,"<li><a href='#'>"+func.getStr("func_name")+"</a></li>");
	    	}else{
	    		sb.append("<li class=\"active\">").append(func.getStr("func_name")).append("</li></ul>");
	    	}
    	}
    	funcName(sb,func.getStr("parent_func_code"));
    }
	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
    
}
