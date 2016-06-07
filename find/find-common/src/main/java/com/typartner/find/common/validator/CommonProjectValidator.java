package com.typartner.find.common.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/** 
 * @ClassName: CommonProjectValidator 
 * @Description: 项目验证
 * @author hety
 * @date 2015年11月22日 上午12:29:30  
 */
public abstract class CommonProjectValidator extends Validator {
	
	@Override
	protected void validate(Controller c) {
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		try {
			startDate = dateFormat1.parse("1900-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		validateString("project.PROJECT_NAME", 1, 50, "project_name_msg", "请输入1~50字项目名称");
		validateInteger("project.MIN_PRICE", "min_price_msg", "请输入金额");
		validateInteger("project.MAX_PRICE", "max_price_msg", "请输入金额");
		validateString("project.BRAND_NAME", 1, 50, "brand_name_msg", "请输入1~50字品牌名称");
		validateInteger("project.DIRECT_SALE_NUM", "direct_sale_num_msg", "请输入直营店数量");
		validateInteger("project.NAPA_NUM", "napa_num_msg", "请输入加盟店数量");
		validateRegex("project.PHONE_NO", "^1\\d{10}$", "phone_no_msg", "请输入电话号码");
		validateString("project.COMPANY_NAME", 1, 50, "company_name_msg", "请输入1~50字公司名称");
		validateString("project.PROJECT_DESC", 1, 500, "project_desc_msg", "请输入1~500字项目描述");
		validateInteger("project.PROVINCE_ID", 0, 99, "prefecture_msg", "请选择省份");
		validateInteger("project.CITY_ID", 0, 9999, "prefecture_msg", "请选择城市");
		validateDate("project.BRAND_CREATE_DATE", startDate, new Date(), "brand_create_date_msg", "请输入合法日期");
		validateRequiredString("IMG", "img_msg", "请至少上传一个图片");
		validateString("project.BRAND_REGIST_NUM", 1, 50, "brand_regist_num_msg", "请输入1~50字商标注册号");
		validateInteger("project.INVEST_TYPE", 0, 1, "invest_type_msg", "请选择招商模式");
		validateInteger("project.REGION_AUTH", 0, 1, "region_auth_msg", "请选择有无区域授权");
		validateInteger("project.RECORD", 0, 1, "record_msg", "请选择是否商务部备案");
		validateString("project.PRODUCT", 1, 200, "product_msg", "请输入1~200字经营类型");
	}

}
