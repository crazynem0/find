package com.typartner.find.common.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/** 
 * @ClassName: CommonShopValidator 
 * @Description: 商铺验证
 * @author zhanglei
 * @date 2015年11月22日 上午12:29:30  
 */
public abstract class CommonShopValidator extends Validator {

	/* (non-Javadoc)
	 * @see com.jfinal.validate.Validator#validate(com.jfinal.core.Controller)
	 */
	@Override
	protected void validate(Controller c) {
		validateString("shop.SHOP_NAME", 1, 50, "shop_name_msg", "请输入1~50字商铺名称");
		validateInteger("shop.STATUS", 1, 2, "status_msg", "非法操作");
		validateDouble("shop.LONGITUDE", "address_msg", "请在地图选商铺位置");
		validateDouble("shop.LATITUDE", "address_msg", "请在地图选商铺位置");
		validateRequiredString("shop.SHOP_ADDRESS", "address_msg", "请在地图选择商铺位置");
		validateRequiredString("IMG", "img_msg", "请至少上传一个图片");
		validateInteger("shop.PROVINCE_ID", 0, 99, "prefecture_msg", "请选择省份");
		validateInteger("shop.CITY_ID", 0, 9999, "prefecture_msg", "请选择城市");
		validateInteger("shop.COUNTY_ID", 0, 999999, "prefecture_msg", "请选择区县");
		validateInteger("shop.STREET_ID", 0, 999999999, "prefecture_msg", "请选择街道");
		validateRequiredString("shop.OWNER_NAME", "owner_name_msg", "请输入铺主称呼");
		validateRegex("shop.OWNER_PHONE", "^1\\d{10}$", "owner_phone_msg", "请输入铺主手机号");
		validateRequiredString("shop.AREA", "area_msg", "请输入商铺面积");
		validateInteger("shop.PAY_TYPE", 1, 2, "pay_type_msg", "请选择支付方式");
		validateInteger("shop.PRICE", "price_msg", "请输入金额");
		validateInteger("shop.SHOP_STATUS", 1, 2, "shop_status_msg", "请选择商铺现状");
		validateInteger("shop.TRANSFER_FEE", "transfer_fee_msg", "请输入转让费");
		validateInteger("shop.CONTRACT_YEAR", "contract_year_msg", "请输入合同年限");
		validateInteger("shop.UP_LOW_WATER", 1, 2, "up_low_water_msg", "请选择是否有上下水");
		validateInteger("shop.THREE_PHASE_ELEC", 1, 2, "three_phase_elec_msg", "请选择是否有三相电");
		validateInteger("shop.PARKING_SPACE", 1, 2, "parking_space_msg", "请选择是否有停车位");
		validateInteger("shop.HEATING", 1, 2, "heating_msg", "请选择是否有暖气");
		validateInteger("shop.NEED_3D", 0, 1, "need_3d_msg", "请选择是否需要全景地图");
	}

}
