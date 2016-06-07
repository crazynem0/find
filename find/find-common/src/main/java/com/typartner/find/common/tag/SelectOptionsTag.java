package com.typartner.find.common.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.typartner.find.common.exception.ActionException;
import com.typartner.find.common.model.DataDictionary;
import com.typartner.find.common.util.StringUtil;


public class SelectOptionsTag extends TagSupport {
    private static final long serialVersionUID = 3754897857385319542L;
    private static final Logger logger = Logger.getLogger(SelectDataValueTag.class);
    private String bigTypeKey;
    private String key;
    private boolean isNull = true;
    private String tip;
    
    
    /**
	 * @return 
	 * @throws JspException
	 */
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.write(make());
        }catch (ActionException e) {
        	logger.error("SelectOptions内容显示下拉标签生成发生action错误",e);
        } catch (IOException e) {
        	logger.error("SelectOptions内容显示下拉标签生成发生io错误",e);
        }
        return super.doEndTag();
    }
    
    /**
	 * @return 
	 */
    public String make() throws ActionException{
        try {
			StringBuffer sbuffer = new StringBuffer("");
			Map<String, String> map = DataDictionary.dao.getDataDictionaryOfTypeMap(bigTypeKey);
			if (map != null) {
				if (map.size()>0) {
					if (!StringUtil.isBlank(tip)){
						sbuffer.append("<option value=\"\">").append(tip).append("</option>\n");
					}
					Iterator it = map.keySet().iterator();
					while(it.hasNext()) {
						String tempKey = it.next().toString();
						String value = map.get(tempKey).toString();
						sbuffer.append("<option value=\"")
								.append(tempKey)
								.append("\"");
						if (!StringUtil.isBlank(key)&&key.equals(tempKey)) {
							sbuffer.append(" selected");
						}
						sbuffer.append(">")
								.append(value)
								.append("</option>\n");
					}
				}
			}
			this.setIsNull(true);
			return sbuffer.toString();
		}catch (Exception e) {
			throw new ActionException("SelectOptions标签发生业务错误......",e);
		}
    }
    
	public String getBigTypeKey() {
    	return bigTypeKey;
    }
	public void setBigTypeKey(String bigTypeKey) {
    	this.bigTypeKey = bigTypeKey;
    }
	public String getKey() {
    	return key;
    }
	public void setKey(String key) {
    	this.key = key;
    }
	public String getId() {
    	return id;
    }
	public void setId(String id) {
    	this.id = id;
    }
	public boolean getIsNull() {
    	return isNull;
    }
	public void setIsNull(boolean isNull) {
    	this.isNull = isNull;
    }
	public String getTip() {
    	return tip;
    }
	public void setTip(String tip) {
    	this.tip = tip;
    }
}
