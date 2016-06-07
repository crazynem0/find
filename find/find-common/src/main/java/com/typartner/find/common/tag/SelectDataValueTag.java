package com.typartner.find.common.tag;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.typartner.find.common.exception.ActionException;
import com.typartner.find.common.util.DataInitUtil;
import com.typartner.find.common.util.StringUtil;



/**
 * 
 * <p>Title: 下拉框标签</p>
 * <p>Description: 支持从数据字典中取得值集合显示与下拉框中</p>
 *
 */

public class SelectDataValueTag extends TagSupport {
    private static final long serialVersionUID = -2731221242429754L;
    private static final Logger logger = Logger.getLogger(SelectDataValueTag.class);
    private String bigTypeKey;
    private String key;
    private String id;
    private boolean isNull = true;
    private boolean disabled = false;
    private String name = "";
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
        	logger.error("selectDataValue内容显示下拉标签生成发生action错误",e);
        } catch (IOException e) {
        	logger.error("selectDataValue内容显示下拉标签生成发生io错误",e);
        }
        return super.doEndTag();
    }

    /**
	 * @return 
	 */
    public String make() throws ActionException{
        try {
			StringBuffer sbuffer = new StringBuffer("");
			if (StringUtil.isBlank(name)) 
				name = id;
			Object obj = DataInitUtil.getDataLibrary().get(Integer.valueOf(bigTypeKey));
			if (obj != null) {
				Map map = (Map)obj;
				if (map.size()>0) {
					sbuffer.append("<select class=\"us-slt\" style=\"width:249px;height:36px\" name=\"")
							.append(name)
							.append("\" id=\"")
							.append(id)
							.append("\"");
					if (disabled) {
						sbuffer.append(" disabled=\"true\"");
					}
					sbuffer.append(">\n");
					if (StringUtil.isBlank(tip))
						sbuffer.append("<option value=\"\">--请选择--</option>\n");
					else
						sbuffer.append("<option value=\"\">").append(tip).append("</option>\n");
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
					sbuffer.append("</select>");
				}
			}
			this.setIsNull(true);
			this.setName("");
			return sbuffer.toString();
		}catch (Exception e) {
			throw new ActionException("dataValue标签按钮发生业务错误......",e);
		}
    }

	public String getBigTypeKey() {
		return bigTypeKey;
	}

	public void setBigTypeKey(String bigTypeKey) {
		this.bigTypeKey = bigTypeKey;
	}

	public boolean getIsNull() {
		return isNull;
	}

	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
}
