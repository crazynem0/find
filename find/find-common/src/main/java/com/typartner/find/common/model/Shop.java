package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.typartner.find.common.util.DateUtil;
import com.typartner.find.common.util.StringUtil;

/** 
 * @ClassName: Shop 
 * @Description: 商铺实体
 * @author zhanglei
 * @date 2015年11月21日 下午8:47:08  
 */
@TableBind(tableName = "shop")
public class Shop extends Model<Shop> {
	
	private static final String[] sensitiveInfoArr = {"OWNER_PHONE"}; //敏感信息，需付费查看
	private static final String hiddenReplace = "*";
	
	private static final long serialVersionUID = 8632641411528052464L;
	public static final Shop dao = new Shop();
	
//	private String OUT_TYPE;
//	private String INDUSTRY;
//	private String SHOP_TYPE;
	
	public Page<Shop> paginate(int pageNum, int pageSize){
		return paginate(pageNum, pageSize, "select * ", "from shop order by id asc");
	}
	
	/*
	 * add fangrui
	 * 根据发布人No查询我发布的商铺 供个人中心使用
	 */
	public Page<Shop> paginateByUid(int pageNum, int pageSize, String userNo){
		return paginate(pageNum, pageSize, "select s.* , i.IMG_PATH ", "from shop s, (select shop_id, IMG_PATH from shop_img group by shop_id having min(show_index)) i where s.ID = i.SHOP_ID and s.P_USER_NO = '"+userNo+"' order by id asc");
	}
	/**
	 * 根据条件搜索分页数据
	 * @param shopName
	 * @param countyId
	 * @param catStr
	 * @param minArea
	 * @param maxArea
	 * @param minPrice
	 * @param maxPrice
	 * @param has3D
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	public Page<Shop> search(String shopName, String countyId, String catStr, String minArea, String maxArea, String minPrice, String maxPrice, String has3D, int pageNum, int pageSize, String orderBy, boolean hiddenInfo){
		Shop qryIn = new Shop();
		qryIn.set("SHOP_NAME", shopName);
		if (!StringUtil.isBlank(countyId)) {
			qryIn.set("COUNTY_ID", Integer.parseInt(countyId));
		}
		qryIn.set("STATUS", 5);
		Page<Shop> shopList = searchShop(qryIn, catStr, minArea, maxArea, minPrice, maxPrice, has3D, pageNum, pageSize, orderBy, null);
		if(shopList != null && shopList.getTotalRow() > 0){ //查到了数据
			List<Integer> shopIdList = new ArrayList<Integer>();
			
			for(Shop shop : shopList.getList()){
				//获取商铺ID的List
				shopIdList.add(shop.getInt("ID"));
				//删除敏感信息
				if(hiddenInfo){
					for(String info : sensitiveInfoArr){
						shop.set(info, "");
					}
				}
			}
			
			//商铺类别多对多
			List<Category> catList = Category.dao.getCategoryByShopId(shopIdList);
			for(Category cat : catList){
				for(Shop shop : shopList.getList()){
					if(shop.getInt("ID").equals(cat.getInt("SHOP_ID"))){
						List<Category> tmpList = shop.get("catList");
						if(tmpList == null){
							tmpList = new ArrayList<Category>();
							shop.put("catList", tmpList);
						}
						tmpList.add(cat);
						break;
					}
				}
			}
			
			//商铺图片多对多
			List<ShopImg> imgList = ShopImg.dao.getImgByShopId(shopIdList);
			for(ShopImg img : imgList){
				for(Shop shop : shopList.getList()){
					if(shop.getInt("ID").equals(img.getInt("SHOP_ID"))){
						List<ShopImg> tmpList = shop.get("imgList");
						if(tmpList == null){
							tmpList = new ArrayList<ShopImg>();
							shop.put("imgList", tmpList);
						}
						tmpList.add(img);
						break;
					}
				}
			}
		}
		return shopList;
	}
	
	/**
	 * 根据条件搜索商铺分页数据
	 * @param qryIn
	 * @param catStr
	 * @param minArea
	 * @param maxArea
	 * @param minPrice
	 * @param maxPrice
	 * @param has3D
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @param orderType asc/desc
	 * @return
	 */
	public Page<Shop> searchShop(Shop qryIn, String catStr, String minArea, String maxArea, String minPrice, String maxPrice, String has3D, int pageNum, int pageSize, String orderBy, String orderType){
		StringBuilder sb = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		String select = "select s.* ";
		sb.append(" from shop s ");
		if(!StringUtil.isBlank(catStr)){
			String[] catArr = catStr.split(",");
			StringBuilder catPara = new StringBuilder();
			String inPara = ""; //问号参数
			for(String catId : catArr){
				catPara.append("?,");
				paras.add(catId); //具体参数
			}
			if(catPara.length() > 0){
				inPara = catPara.substring(0, catPara.length() - 1);
			}
			sb.append(",	(select DISTINCT SHOP_ID from shop_cat where CAT_ID in (").append(inPara).append(")) as sc where s.ID=sc.SHOP_ID ");
		}else{
			sb.append(" where 1=1 ");
		}
		String shopName = qryIn.getStr("SHOP_NAME");
		if(!StringUtil.isBlank(shopName)){
			sb.append(" and s.SHOP_NAME like ?");
			paras.add("%" + shopName + "%");
		}
		Integer countyId = qryIn.getInt("COUNTY_ID");
		if(!StringUtil.isBlank(countyId)){
			sb.append(" and s.COUNTY_ID = ?");
			paras.add(countyId);
		}
		if(!StringUtil.isBlank(minArea)){
			sb.append(" and s.area >= ?");
			paras.add(Integer.parseInt(minArea));
		}
		if(!StringUtil.isBlank(maxArea)){
			sb.append(" and s.area <= ?");
			paras.add(Integer.parseInt(maxArea));
		}
		if(!StringUtil.isBlank(minPrice)){
			sb.append(" and s.price >= ?");
			paras.add(Integer.parseInt(minPrice));
		}
		if(!StringUtil.isBlank(maxPrice)){
			sb.append(" and s.price <= ?");
			paras.add(Integer.parseInt(maxPrice));
		}
		if(!StringUtil.isBlank(has3D)){
			sb.append(" and s.need_3d = ?");
			paras.add(has3D);
		}
		// 发布人No
		String pNo = qryIn.getStr("P_USER_NO");
		if(!StringUtil.isBlank(pNo)){
			sb.append(" and s.P_USER_NO like ?");
			paras.add("%" + pNo + "%");
		}
		// 铺主称呼
		String ownerName = qryIn.getStr("OWNER_NAME");
		if(!StringUtil.isBlank(ownerName)){
			sb.append(" and s.OWNER_NAME like ?");
			paras.add("%" + ownerName + "%");
		}
		// 状态
		Integer status = qryIn.getInt("STATUS");
		if (!StringUtil.isBlank(status)) {
			sb.append(" and s.STATUS = ?");
			paras.add(status);
		}
		if(!StringUtil.isBlank(orderBy)){
			sb.append(" order by ").append(orderBy);
			if (!StringUtil.isBlank(orderType)) {
				sb.append(" "+orderType);
			}
		}
		if(paras.size() > 0){
			return paginate(pageNum, pageSize, select, sb.toString(), paras.toArray());
		}else{
			return paginate(pageNum, pageSize, select, sb.toString());
		}
	}
	
	/**
	 * 验证商铺是否归属于某用户，避免篡改ID修改其他人商铺
	 * @param shopId
	 * @param userNo
	 * @return
	 */
	public boolean belongToUser(Integer shopId, String userNo){
//		int rs = Db.queryInt("select count(*) from shop where ID=? and P_USER_NO=?", shopId, userNo);
//		return rs == 0;
		return true; //测试用
	}
	
	/**
	 * 根据地区获取数量
	 * @return
	 */
	public List<Shop> countByCounty(String shopName, String catStr, String minArea, String maxArea, String minPrice, String maxPrice, String has3D){
		StringBuilder sb = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		
		sb.append("select s.COUNTY_ID, s.COUNTY_NAME, count(*) as SHOP_NUM, AVG(LONGITUDE) as LONGITUDE, AVG(LATITUDE) as LATITUDE ");
		sb.append(" from shop s ");
		if(!StringUtil.isBlank(catStr)){
			String[] catArr = catStr.split(",");
			StringBuilder catPara = new StringBuilder();
			String inPara = ""; //问号参数
			for(String catId : catArr){
				catPara.append("?,");
				paras.add(catId); //具体参数
			}
			if(catPara.length() > 0){
				inPara = catPara.substring(0, catPara.length() - 1);
			}
			sb.append(",	(select DISTINCT SHOP_ID from shop_cat where CAT_ID in (").append(inPara).append(")) as sc where s.ID=sc.SHOP_ID and s.STATUS=5");
		}else{
			sb.append(" where s.STATUS=5 ");
		}
		if(!StringUtil.isBlank(shopName)){
			sb.append(" and s.SHOP_NAME like ?");
			paras.add("%" + shopName + "%");
		}
		if(!StringUtil.isBlank(minArea)){
			sb.append(" and s.area >= ?");
			paras.add(Integer.parseInt(minArea));
		}
		if(!StringUtil.isBlank(maxArea)){
			sb.append(" and s.area <= ?");
			paras.add(Integer.parseInt(maxArea));
		}
		if(!StringUtil.isBlank(minPrice)){
			sb.append(" and s.price >= ?");
			paras.add(Integer.parseInt(minPrice));
		}
		if(!StringUtil.isBlank(maxPrice)){
			sb.append(" and s.price <= ?");
			paras.add(Integer.parseInt(maxPrice));
		}
		if(!StringUtil.isBlank(has3D)){
			sb.append(" and s.need_3d = ?");
			paras.add(has3D);
		}
		sb.append(" group by s.COUNTY_ID ");
			return dao.find(sb.toString(), paras.toArray());
	}
	
//	/**
//	 * 根据地区获取List
//	 * @return
//	 */
//	public List<Shop> searchByCounty(Integer countyId){
//		String sql = "select * from shop where STATUS=5 and COUNTY_ID = " + countyId;
//		return dao.find(sql);
//	}
	
	/**
	 * 根据ID查询非敏感信息
	 * @param id
	 * @return
	 */
	public Shop findUnSensitiveInfoById(int id){
		Shop shop = findById(id);
		
		for(String info : sensitiveInfoArr){
			shop.set(info, hiddenInfo(shop.getStr(info)));
		}
		return shop;
	}
	
	/**
	 * 隐藏信息
	 * @param source 原始信息
	 * @return
	 */
	private String hiddenInfo(String source){
		int hiddenStrLength = source.length() / 2;
		int startIndex = (source.length() - hiddenStrLength) / 2;
		int endIndex = startIndex + hiddenStrLength;
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(char c : source.toCharArray()){
			if(index >= startIndex && index <= endIndex){
				sb.append(hiddenReplace);
			}else{
				sb.append(c);
			}
			index++;
		}
		return sb.toString();
	}
	
	/**
	 * 审批
	 * @param idValue
	 * @return
	 */
//	public boolean approve(int idValue){
//		//1.获取数据库信息
//		Shop shop = findShopCatById(idValue);
//		BaiduGeoDataPoiCreateRequest req = new BaiduGeoDataPoiCreateRequest();
//		req.setAddress(shop.getStr("SHOP_ADDRESS"));
//		req.setArea(shop.getStr("AREA"));
//		req.setCoord_type("3");
//		req.setExtra_address(shop.getStr("EXTRA_ADDRESS"));
//		req.setIndustry(String.valueOf(shop.getInt("INDUSTRY")));
//		req.setLatitude(String.valueOf(shop.getDouble("LATITUDE")));
//		req.setLongitude(String.valueOf(shop.getDouble("LONGITUDE")));
//		req.setNeed_3d(String.valueOf(shop.getInt("NEED_3D")));
//		req.setOut_type(String.valueOf(shop.getInt("OUT_TYPE")));
//		if(shop.getInt("PRICE") != null){
//			req.setPrice(String.valueOf(shop.getInt("PRICE")));
//		}
//		req.setShop_type(String.valueOf(shop.getInt("SHOP_TYPE")));
//		req.setTitle(shop.getStr("SHOP_NAME"));
//		req.setCounty_id(shop.getStr("COUNTY_ID"));
//		//2.同步数据到LBS
//		BaiduGeoDataPoiResponse res = BaiduGeoUtil.dataPoi(req); //同步数据到LBS
//		if(res.getStatus() == BaiduGeoUtil.STATUS_SUCCESS){ //同步成功
//			//3.修改数据库信息
//			Shop sh = findById(idValue);
//			sh.set("OPI_ID", res.getId());
//			sh.set("SYN_STATUS", 1);
//			sh.set("SYN_TIME", new Date());
//			return sh.update();
//		}
//		
//		return false;
//	}
	
	/**
	 * 根据ID查商铺，且包含类型
	 * @param id
	 * @return
	 */
//	private Shop findShopCatById(int id){
//		String sql = "select s.*, c1.cat_id as OUT_TYPE, c2.cat_id as INDUSTRY, c3.cat_id as SHOP_TYPE from shop s "
//					+ " left JOIN (select sc.* from shop_cat sc, category c where c.CAT_ID=sc.CAT_ID and c.PARENT_ID=84) c1 on s.ID=c1.SHOP_ID "
//					+ " left JOIN (select sc.* from shop_cat sc, category c where c.CAT_ID=sc.CAT_ID and c.PARENT_ID=60) c2 on s.ID=c2.SHOP_ID "
//					+ " left JOIN (select sc.* from shop_cat sc, category c where c.CAT_ID=sc.CAT_ID and c.PARENT_ID=59) c3 on s.ID=c3.SHOP_ID "
//					+ " where s.ID=?";
////		List<Shop> list = dao.find(sql);
////		if(list == null || list.size() == 0){
////			return null;
////		}
////		return list.get(0);
//		return dao.findFirst(sql, id);
//	}

	/**
	 * 首页展示前六条商铺
	 * @return
	 */
	public List<Shop> shopMain(){
		String sql = "select a.*,b.IMG_PATH FROM shop a,shop_img b WHERE a.ID=b.SHOP_ID AND a.STATUS='5' AND b.SHOW_INDEX='1' ORDER BY a.CREATE_TIME DESC LIMIT 6";
		return dao.find(sql);
	}
	
	/**
	 * 获取平台推荐商铺
	 * @return
	 */
	public List<Shop> findRecommend(){
		String sql = "select s.ID, s.SHOP_NAME, s.CREATE_TIME, s.MODIFY_TIME, s.AREA, s.PRICE, "
				+ "(SELECT IMG_PATH FROM shop_img WHERE SHOP_ID = s.ID AND SHOW_INDEX = "
				+ "( SELECT min(SHOW_INDEX) FROM shop_img WHERE SHOP_ID = s.ID ) ) AS IMG_PATH from shop s where s.STATUS=5 limit 2";
		return dao.find(sql);
	}

	
	/**
	 * 查询商铺总数
	 */
	public long countTotal(){
		return Db.queryLong("SELECT COUNT(*) FROM shop WHERE STATUS NOT in(1,7)");
	}
	
	/**
	 * 查询今天增加数
	 */
	public long createCountToday(){
		Date today = DateUtil.stringToDate(DateUtil.dateFormat(new Date(), "yyyyMMdd"), "yyyyMMdd");
		return Db.queryLong("SELECT COUNT(*) FROM shop WHERE STATUS NOT in(1,7) AND CREATE_TIME>=?", today);
	}
	
	/**
	 * 待审批数
	 */
	public long waitChkCount(String t){
		String inStatus = "";
		if ("first".equalsIgnoreCase(t)) {
			inStatus = "2,6";
		} else if ("last".equalsIgnoreCase(t)) {
			inStatus = "3";
		} else {
			inStatus = "2,3,6";
		}
		return Db.queryLong("SELECT COUNT(*) FROM shop where STATUS in("+inStatus+")");
	}
	
	/**
	 * 需要3D数
	 * @return
	 */
	public long countNeed3D(){
		return Db.queryLong("SELECT COUNT(*) FROM shop WHERE NEED_3D=1 AND STATUS in(2,6)");
	}

	public List<Shop> shopRecommend() {
		String sql = "SELECT * FROM shop WHERE STATUS='5' AND RECOMMEND='1' ORDER BY CREATE_TIME DESC LIMIT 6";
		return dao.find(sql);
	}
	
	/**
	 * 商铺名称唯一性校验
	 * @return
	 */
	public boolean findShopByName(String shopName){
		Long rs = Db.queryLong("select count(*) from shop where shop_name = ?",shopName);
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
}
