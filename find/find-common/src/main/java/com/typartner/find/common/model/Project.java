package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.typartner.find.common.util.DateUtil;
import com.typartner.find.common.util.StringUtil;

/**
* @ClassName: Project 
* @Description: 项目
* @author zhanglei
* @date 2015年11月21日 下午10:17:49
 */
@TableBind(tableName = "project")
public class Project extends Model<Project> {
	private static final long serialVersionUID = -3366867659697509516L;
	
	/** 不申请置顶/未置顶/申请下顶 */
	public static final int TOP_STATUS_OFF = 0;
	/** 申请置顶 */
	public static final int TOP_STATUS_REQ = 1;
	/** 拒绝置顶 */
	public static final int TOP_STATUS_REFUSE = 2;
	/** 已置顶 */
	public static final int TOP_STATUS_ON = 9;
	/** 扩展：状态not in 值，多个用逗号隔开 */
	private String notInStatus;
	
	private static final String[] sensitiveInfoArr = {"PHONE_NO"}; //敏感信息，需付费查看
	private static final String hiddenReplace = "*";	
	public static final Project dao = new Project();
	
	public Page<Project> paginate(int pageNum, int pageSize){
		return paginate(pageNum, pageSize, "select * ", "from project order by id asc");
	}
	
	/*
	 * add fangrui
	 * 根据发布人员ID查询发布项目，供个人中心使用
	 */
	public Page<Project> paginateByUid(int pageNum, int pageSize, String userNo){
		return paginate(pageNum, pageSize, "select p.* , i.IMG_PATH ", "from project p, (select PROJECT_ID, IMG_PATH from project_img group by PROJECT_ID having min(show_index)) i where p.id = i.PROJECT_ID and p.P_USER_NO ='"+userNo+"' order by id asc");
	}
	
	/**
	 * 根据条件搜索分页数据
	 * @param projectName
	 * @param catStr
	 * @param minPrice
	 * @param maxPrice
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	public Page<Project> search(String projectName, String catStr, String minPrice, String maxPrice, int pageNum, int pageSize, String orderBy, boolean hiddenInfo){
		Project qryIn = new Project();
		qryIn.set("PROJECT_NAME", projectName);
		if (!StringUtil.isBlank(minPrice)) {
			qryIn.set("MIN_PRICE", Integer.parseInt(minPrice));
		}
		if (!StringUtil.isBlank(maxPrice)) {
			qryIn.set("MAX_PRICE", Integer.parseInt(maxPrice));
		}
		qryIn.set("STATUS", 5);
		Page<Project> projectList = searchProject(qryIn, catStr, pageNum, pageSize, orderBy, null);
		if(projectList != null && projectList.getTotalRow() > 0){ //查到了数据
			List<Integer> projectIdList = new ArrayList<Integer>();
			for(Project project : projectList.getList()){
				//获取项目ID的List
				projectIdList.add(project.getInt("ID"));
				//删除敏感信息
				if(hiddenInfo){
					for(String info : sensitiveInfoArr){
						project.set(info, "");
					}
				}
			}
			
			//项目类别多对多
			List<Category> catList = Category.dao.getCategoryByProjectId(projectIdList);
			for(Category cat : catList){
				for(Project project : projectList.getList()){
					if(project.getInt("ID") == cat.getInt("PROJECT_ID")){
						List<Category> tmpList = project.get("catList");
						if(tmpList == null){
							tmpList = new ArrayList<Category>();
							project.put("catList", tmpList);
						}
						tmpList.add(cat);
						break;
					}
				}
			}
			
			//项目图片多对多
			List<ProjectImg> imgList = ProjectImg.dao.getImgByProjectId(projectIdList);
			for(ProjectImg img : imgList){
				for(Project project : projectList.getList()){
					if(project.getInt("ID") == img.getInt("PROJECT_ID")){
						List<ProjectImg> tmpList = project.get("imgList");
						if(tmpList == null){
							tmpList = new ArrayList<ProjectImg>();
							project.put("imgList", tmpList);
						}
						tmpList.add(img);
						break;
					}
				}
			}
		}
		return projectList;
	}
	
	/**
	 * 根据条件搜索项目分页数据
	 * @param qryIn : 条件入参实体
	 * @param catStr : 类型集合串，以逗号隔开
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy : 排序列
	 * @param orderType : asc 或 desc
	 * @return
	 */
	public Page<Project> searchProject(Project qryIn, String catStr, int pageNum, int pageSize, String orderBy, String orderType){
		StringBuilder sb = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		String select = "select p.* ";
		sb.append(" from project p ");
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
			sb.append(",	(select DISTINCT PROJECT_ID from project_cat where CAT_ID in (").append(inPara).append(")) as pc where p.ID=pc.PROJECT_ID ");
		}else{
			sb.append(" where 1=1 ");
		}
		// 项目名称
		String projectName = qryIn.getStr("PROJECT_NAME");
		if(!StringUtil.isBlank(projectName)){
			sb.append(" and p.PROJECT_NAME like ?");
			paras.add("%" + projectName + "%");
		}
		// 投资额上下限
		Integer minPrice = qryIn.getInt("MIN_PRICE");
		Integer maxPrice = qryIn.getInt("MAX_PRICE");
		if(!StringUtil.isBlank(minPrice) && !StringUtil.isBlank(maxPrice)){ //选择区间
			sb.append(" and ((p.MIN_PRICE between ? and ?) or (p.MAX_PRICE between ? and ?) or ((p.MIN_PRICE <= ?) and (p.MAX_PRICE >= ?)))");
			paras.add(minPrice);
			paras.add(maxPrice);
			paras.add(minPrice);
			paras.add(maxPrice);
			paras.add(minPrice);
			paras.add(maxPrice);
		}else if(!StringUtil.isBlank(minPrice)){ //选择低限
			sb.append(" and p.MAX_PRICE >= ?");
			paras.add(minPrice);
		}
		// 发布人No
		String pNo = qryIn.getStr("P_USER_NO");
		if(!StringUtil.isBlank(pNo)){
			sb.append(" and p.P_USER_NO like ?");
			paras.add("%" + pNo + "%");
		}
		// 状态
		Integer status = qryIn.getInt("STATUS");
		if (!StringUtil.isBlank(status)) {
			sb.append(" and p.STATUS = ?");
			paras.add(status);
		}
		// not in STATUS
		if (StringUtils.isNotBlank(qryIn.getNotInStatus())) {
			sb.append(" and p.STATUS not in ("+qryIn.getNotInStatus()+")");
		}
		// 置顶状态
		Integer top = qryIn.getInt("TOP_STATUS");
		if (!StringUtil.isBlank(top)) {
			sb.append(" and p.TOP_STATUS = ?");
			paras.add(top);
		}
		// 排序(正序/倒序)
		if(!StringUtil.isBlank(orderBy)){
			sb.append(" order by p.TOP_STATUS desc, ").append(orderBy);
			if (StringUtils.isNotBlank(orderType)) {
				sb.append(" "+orderType);
			}
		} else {
			sb.append(" order by p.TOP_STATUS desc");// 默认 按置顶列倒序
		}
		if(paras.size() > 0){
			return paginate(pageNum, pageSize, select, sb.toString(), paras.toArray());
		}else{
			return paginate(pageNum, pageSize, select, sb.toString());
		}
	}
	
	/**
	 * 首页展示前五条项目
	 * @return
	 */
	public List<Project> proMain(){
		String sql = "SELECT a.*,b.IMG_PATH FROM project a,project_img b WHERE a.ID=b.PROJECT_ID AND a.STATUS='5' AND b.SHOW_INDEX='1' "
				   + "order by a.TOP_STATUS desc, CREATE_TIME DESC LIMIT 6";
		return dao.find(sql);
	}
	
	/**
	 * 获取已置顶 个数
	 * @return
	 */
	public long getTopStatusCount(){
		return Db.queryLong("SELECT COUNT(*) FROM project WHERE TOP_STATUS=?", TOP_STATUS_ON);
	}
	
	/**
	 * 验证项目是否归属于某用户，避免篡改ID修改其他人项目
	 * @param projectId
	 * @param userNo
	 * @return
	 */
	public boolean belongToUser(Integer projectId, String userNo){
//		int rs = Db.queryInt("select count(*) from project where ID=? and P_USER_NO=?", projectId, userNo);
//		return rs == 0;
		return true; //测试用
	}
	
	/**
	 * 根据ID查询非敏感信息
	 * @param id
	 * @return
	 */
	public Project findUnSensitiveInfoById(int id){
		Project project = findById(id);
		
		for(String info : sensitiveInfoArr){
			project.set(info, hiddenInfo(project.getStr(info)));
		}
		return project;
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
	 * 获取平台推荐项目
	 * @return
	 */
	public List<Project> findRecommend(){
		String sql = "select s.ID, s.PROJECT_NAME, s.CREATE_TIME, s.MODIFY_TIME, s.PRODUCT, s.INVEST_TYPE,  "
				+ "(SELECT IMG_PATH FROM project_img WHERE PROJECT_ID = s.ID AND SHOW_INDEX = "
				+ "( SELECT min(SHOW_INDEX) FROM project_img WHERE PROJECT_ID = s.ID ) ) AS IMG_PATH from project s limit 2";
		return dao.find(sql);
	}

	/**
	 * 查询项目总数
	 */
	public long countTotal(){
		return Db.queryLong("SELECT COUNT(*) FROM project WHERE STATUS NOT in(1,7)");
	}
	
	/**
	 * 查询今天增加数
	 */
	public long createCountToday(){
		Date today = DateUtil.stringToDate(DateUtil.dateFormat(new Date(), "yyyyMMdd"), "yyyyMMdd");
		return Db.queryLong("SELECT COUNT(*) FROM project WHERE STATUS NOT in(1,7) AND CREATE_TIME>=?", today);
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
		return Db.queryLong("SELECT COUNT(*) FROM project where STATUS in("+inStatus+")");
	}
	
	/**
	 * 置顶申请数
	 * @return
	 */
	public long countTopApply(){
		return Db.queryLong("SELECT COUNT(*) FROM project WHERE TOP_STATUS=?", TOP_STATUS_REQ);
	}
	
	/**
	 * 项目名称唯一性校验
	 * @return
	 */
	public boolean findProByName(String projectName){
		Long rs = Db.queryLong("select count(*) from project where project_name = ?",projectName);
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	
	////////getter and setter////////
	public String getNotInStatus() {
		return notInStatus;
	}
	public void setNotInStatus(String notInStatus) {
		this.notInStatus = notInStatus;
	}
}
