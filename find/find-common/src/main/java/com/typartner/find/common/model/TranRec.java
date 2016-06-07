package com.typartner.find.common.model;

import java.util.Date;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.typartner.find.common.model.TranType.TRAN_TYPE_ID;
import com.typartner.find.common.util.DateUtil;

/**
 * 交易记录表
* @ClassName: TranRec 
* @Description: 
* @author zhanglei
* @date 2016年1月2日 下午9:53:46
 */
@TableBind(tableName = "tran_rec", pkName="TRAN_NO")
public class TranRec extends Model<TranRec> {
	
	/**
	 * 类别类型
	* @ClassName: CAT_TYPE 
	* @Description: 
	* @author zhanglei
	* @date 2015年11月25日 下午10:19:55
	 */
	public static enum CAT_TYPE{
		SHOP(1), //商铺
		PROJECT(2), //项目
		CHAIN(3), //连锁公司
		REGISTER(5);// 用户
		
		private int type;
		
		private CAT_TYPE(int type){
			this.type = type;
		}

		@Override
		public String toString() {
			return String.valueOf(this.type);
		}
	};
	
	
	private static final long serialVersionUID = 6635825771785991974L;
	
	public static final TranRec dao = new TranRec();
	
	//保存交易信息
	public boolean saveRec(){
		Date now = new Date();
		this.put("PART_ID", DateUtil.getDayOfMonth(now));
		Integer amount = this.getInt("AMOUNT");
		if(amount == null){ //若未设置交易金额，则从交易类型表中获取
			TranType tt = TranType.dao.findTranTypeById(TRAN_TYPE_ID.BUY_SHOP);
			this.put("AMOUNT", tt.getInt("TRAN_FEE"));
		}
		this.put("TRAN_TIME", now);
		return this.save();
	}

}
