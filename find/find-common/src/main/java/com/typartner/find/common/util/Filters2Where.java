package com.typartner.find.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.typartner.find.common.constant.CommonConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Filters2Where {
	public static List<Filter> filtersJESON2Filter(String filters){
    	List <Filter> filterList = new ArrayList<Filter>();
		JSONObject jsonObject = JSONObject.fromObject(filters);
		JSONArray rules = jsonObject.getJSONArray("rules");
		for(Object obj : rules) {  
	        JSONObject rule = (JSONObject) obj;  
	        String field = rule.getString("field");  
	        String op = rule.getString("op");  
	        String data = rule.getString("data");  
            if(field!=null && !"".equals(field)
        			&& op!=null && !"".equals(op)
        			&& data!=null && !"".equals(data)){
            	Filter filter = new Filter();
    	        filter.setField(field);
    	        filter.setOp(op);
    	        filter.setData(data);
    	        filterList.add(filter);
            }
	    }
		return filterList;
    }
	
    public static HashMap<String,Object> filtersJESON2SqlWhere(String filters,HashMap <String,String>filterTempMap){
    	HashMap<String,Object> filterMap = new HashMap<String,Object>();
    	ArrayList <Filter> filterList = new ArrayList<Filter>();
		JSONObject jsonObject = JSONObject.fromObject(filters);
		JSONArray rules = jsonObject.getJSONArray("rules");
		String groupOp = jsonObject.getString("groupOp");
		String sqlWhere = "";
		ArrayList<String> valuesList = new ArrayList<String>();
		for(Object obj : rules) {  
	        JSONObject rule = (JSONObject) obj;  
	        String field = rule.getString("field");  
	        String op = rule.getString("op");  
	        String data = rule.getString("data");  
            if(field!=null && !"".equals(field)
        			&& op!=null && !"".equals(op)
        			&& data!=null && !"".equals(data)){
            	Filter filter = new Filter();
    	        filter.setField(field);
    	        filter.setOp(op);
    	        filter.setData(data);
    	        filterList.add(filter);
    	        String tableName = filterTempMap.get(field+"-belong");
    	        String type = filterTempMap.get(field+"-type");
    	        if(!"".equals(sqlWhere)){
    	        	if(CommonConstants.JQGRID_GROUPOP_ALL.equals(groupOp)){
    	        		sqlWhere += " and ";
    	        	}else if(CommonConstants.JQGRID_GROUPOP_ANY.equals(groupOp)){
    	        		sqlWhere += " or ";
    	        	}
    	        }
    	        String sqlTemp = filter2Where(filter,tableName,type);
    	        if(!"".equals(sqlTemp)){
    	        	sqlWhere += sqlTemp;
    	        	valuesList.add(data);
    	        }
            }
	    }
		filterMap.put("sqlWhere", sqlWhere);
		filterMap.put("valuesArray", valuesList);
		/*System.out.println("["+sqlWhere+"]");
		if(valuesList.toArray()!=null&&valuesList.toArray().length>0){
			for(int i=0;i<valuesList.toArray().length;i++){
				System.out.println("("+valuesList.toArray()[i]+")");
			}
		}*/
		return filterMap;
    }
    
    public static String filter2Where(Filter filter,String tableName,String type){
    	String whereStr = "";
    	String data = filter.getData();
    	String op = filter.getOp();
    	String field = filter.getField();
    	if(field!=null && !"".equals(field)
    			&& op!=null && !"".equals(op)
    			&& data!=null && !"".equals(data)){
    		if("eq".equals(op)){//等于
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " = ? ";
    		}else if("ne".equals(op)){//不等
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " != ? ";
    		}else if("lt".equals(op)){//小于
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " < ? ";
    		}else if("le".equals(op)){//小于等于
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " <= ? ";
    		}else if("gt".equals(op)){//大于
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " > ? ";
    		}else if("ge".equals(op)){//大于等于
    			if(CommonConstants.DATATYPE.equals(type)){
    				whereStr = "date_format(";
    				if(!"".equals(tableName)){
        				whereStr += tableName + ".";
        			}
    				whereStr += field + ",'%Y-%m-%d')";
    			}else{
    				if(!"".equals(tableName)){
        				whereStr = tableName + ".";
        			}
    				whereStr += field;
    			}
    			whereStr += " >= ? ";
    		}else if("bw".equals(op)){//开始于
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " like concat(?,'%') ";
    		}else if("bn".equals(op)){//不开始于
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " not like concat(?,'%') ";
    		}else if("ew".equals(op)){//结束于
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " like concat('%',?)";
    		}else if("en".equals(op)){//不结束于
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " not like concat('%',?)";
    		}else if("cn".equals(op)){//包含
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " like  concat('%',?,'%') ";
    		}else if("nc".equals(op)){//不包含
    			if(!"".equals(tableName)){
    				whereStr = tableName + ".";
    			}
    			whereStr += field + " not like concat('%',?,'%')";
    		}else if("in".equals(op)){//属于
    			
    		}else if("ni".equals(op)){//不属于
    			
    		}
    	}
    	return whereStr;
    }
}
