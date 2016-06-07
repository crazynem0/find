/**
 * @ClassName:     SysMember.java
 * @Description:   (系统会员信息表) 
 * 
 * @author         fangrui
 * @version        V1.0  
 * @Date           2015年11月19日 上午5:26:15 
 */
package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ty.partner.back.util.SpellSqlUtil;
import com.typartner.find.common.util.DateUtil;

/**
 * <p>Title: SysMember</p>
 * <p>Description: 系统会员信息表</p>
 * @author: fangrui
 * @param <M>
 * @date : 2015年11月19日 上午5:26:15
 */
@SuppressWarnings("serial")
@TableBind(tableName = "p_user")
public class SysMember extends Model<SysMember> {
	
	public static final  SysMember dao = new SysMember();
	
	/**
	 * 列名-用户名-VARCHAR(30)
	 */
	public static final String USER_NO = "USER_NO";
	//列名  USER_NAME
	public static final String USER_NAME = "USER_NAME";
	
	
	//根据用户登录名获取会员信息
	public SysMember getInfoByNo(String userNo){
		StringBuffer sb = new StringBuffer();
		sb.append("select p.* from p_user p where p.USER_NO = ?");
		return dao.findFirst(sb.toString(), userNo);
	}
	
	//根据用户登录名获取会员信息
	public SysMember getInfoByNiceName(String niceName){
		StringBuffer sb = new StringBuffer();
		sb.append("select p.* from p_user p where p.NICK_NAME = ?");
		return dao.findFirst(sb.toString(), niceName);
	}
	//根据用户手机号码获取会员信息
	public SysMember getInoByPhone(String phone){
		StringBuffer sb = new StringBuffer();
		sb.append("select p.* from p_user p where p.PHONE = ? ");
		return dao.findFirst(sb.toString(), phone);
	}
	
	//用户登录校验
	public SysMember findByLoginName(String uid){
		return SysMember.dao.findFirst("select * from p_user p, acc_integral a where p.ID = a.USER_ID and p.status='0' and p.USER_NO=?",uid);
	}
	
	//
	public SysMember findByLoginNameAndPwd(String uid,String password){
		return SysMember.dao.findFirst("select p.*, a.BALANCE from p_user p, acc_integral a where p.ID = a.USER_ID and  p.status='0' and p.USER_NO=? and p.password=?",uid,password);
	}
	
	/**
	 * 查询用户总数
	 */
	public long countTotal(){
		return Db.queryLong("SELECT COUNT(*) FROM p_user WHERE STATUS=0");
	}
	
	/**
	 * 查询今天增加数
	 */
	public long createCountToday(){
		Date today = DateUtil.stringToDate(DateUtil.dateFormat(new Date(), "yyyyMMdd"), "yyyyMMdd");
		return Db.queryLong("SELECT COUNT(*) FROM p_user WHERE STATUS=0 AND CREATE_TIME>=?", today);
	}

	/**
	 * 获取 会员 分页
	 * @param pageNum
	 * @param pageSize
	 * @param qryIn
	 * @return
	 */
	public Page<SysMember> getMemberPage(int pageNum, int pageSize, SysMember qryIn){
		StringBuffer select = new StringBuffer("select u.*, a.BALANCE");
		StringBuffer sql = new StringBuffer("FROM p_user u left join acc_integral a ON u.ID=a.USER_ID WHERE 1=1");
		List<Object> paramLst = new ArrayList<Object>();
		if (qryIn != null) {
			String userNo = qryIn.getStr("USER_NO");
			if (StringUtils.isNotBlank(userNo)) {
				sql.append(SpellSqlUtil.spellAndLike("u.USER_NO", userNo));
			}
			String userName = qryIn.getStr("USER_NAME");
			if (StringUtils.isNotBlank(userName)) {
				sql.append(SpellSqlUtil.spellAndLike("u.USER_NAME", userName));
			}
			String phone = qryIn.getStr("PHONE");
			if (StringUtils.isNotBlank(phone)) {
				sql.append(SpellSqlUtil.spellAndLike("u.PHONE", phone));
			}
			Integer provinceId = qryIn.getInt("PROVINCE_ID");
			if (provinceId != null) {
				sql.append(SpellSqlUtil.spellAndEqual("u.PROVINCE_ID"));
				paramLst.add(provinceId);
			}
			Integer cityId = qryIn.getInt("CITY_ID");
			if (cityId != null) {
				sql.append(SpellSqlUtil.spellAndEqual("u.CITY_ID"));
				paramLst.add(cityId);
			}
			Integer countyId = qryIn.getInt("COUNTY_ID");
			if (countyId != null) {
				sql.append(SpellSqlUtil.spellAndEqual("u.COUNTY_ID"));
				paramLst.add(countyId);
			}
			Integer status = qryIn.getInt("STATUS");
			if (status != null) {
				sql.append(SpellSqlUtil.spellAndEqual("u.STATUS"));
				paramLst.add(status);
			}
		}
		sql.append(" order by u.CREATE_TIME desc");
		Page<SysMember> page = paginate(pageNum, pageSize, select.toString(), sql.toString(), paramLst.toArray());
		return page;
	}

}