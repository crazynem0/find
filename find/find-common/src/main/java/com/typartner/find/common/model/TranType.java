package com.typartner.find.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * 交易类型表
* @ClassName: TranType 
* @Description: 
* @author zhanglei
* @date 2016年1月2日 下午10:29:27
 */
@TableBind(tableName = "tran_type")
public class TranType extends Model<TranType> {
	private static final long serialVersionUID = 6635825771785991974L;
	
	public static final TranType dao = new TranType();
	
	public static enum TRAN_TYPE_ID{
		BUY_SHOP(1001), //查看商铺手机号
		BUY_PROJECT(1002), //被用户查看项目信息
		PROJECT_TOP(1004), //项目置顶
		CHAIN_TOP(1005), //查看连锁店手机号或微信号
		HAND_LESS(1006), // 手工扣减
		REGISTER_GIVE(2001),//注册赠送
		SHOP_PASS_CHK(2005), //商铺成功发布
		PROJECT_PASS_CHK(2006), //项目成功发布
		HAND_ADD(2007);// 手工充值
			
		
		private int type;
		
		private TRAN_TYPE_ID(int type){
			this.type = type;
		}

		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
		public Integer toInt() {
			return type;
		}
	};
	
	public TranType findTranTypeById(TRAN_TYPE_ID typeId){
		return findFirst("select * from tran_type where TYPE_ID=?", typeId.toString());
	}

}
