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



/**
 * 
 * <p>Title: 数据字典值显示标签</p>
 * <p>Description: 通过key值从数据字典中获得相应的值并显示</p>
 */

public class DataValueTag extends TagSupport {
    private static final long serialVersionUID = -1734805423532531304L;
    private static final Logger logger = Logger.getLogger(DataValueTag.class);
    private String bigTypeKey;
    private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return
	 * @throws JspException
	 */
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.write(make());
        }catch (ActionException e) {
        	logger.error("dataValue内容显示标签生成发生action错误",e);
        } catch (IOException e) {
        	logger.error("dataValue内容显示标签生成发生io错误",e);
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
				Iterator it = map.keySet().iterator();
				while(it.hasNext()) {
					String tempKey = it.next().toString();
					String value = map.get(tempKey).toString();
					if (!StringUtil.isBlank(key)&&key.equals(tempKey)) {
						sbuffer.append(value);
						break;
					}
				}
			}else{
				return "";
			}
			
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
}
