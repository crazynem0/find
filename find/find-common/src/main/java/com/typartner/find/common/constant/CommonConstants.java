package com.typartner.find.common.constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.kit.PropKit;

public final class CommonConstants {
	//后台session名
	public static final String SESSOININFO_USER="userInfoBean";	//用户对象
	// 后台管理系统session名
	public static final String BACK_SESSION = "backUserBean";
	
	public static final String TRAN_TYPE_PAY = "1";//消费
	public static final String TRAN_TYPE_INCOME = "2";// 收入、充值
	
	public static final int FAV_SHOP = 1;//我的收藏商铺
	public static final int FAV_PROJ = 2;//我的收藏项目
	
	//前台session
	public static final String FRONTSESSION = "sysMember";
	public static final String SESSOININFO_MENUSET="menuSetJson";	//权限集合
	public static final String SESSOININFO_LOGINACCEPT="loginAccept";//统一接触流水
	public static final int  FLAG_SUCCESS = 0;             //操作成功标志
    public static final int  FLAG_FAIL = 1;				   //操作失败标志
    public static final String COMPARISON = "comparison";
    
    //快速发布状态
    public static final int QUICK_UNRELEASE = 1;//待发布
    public static final int QUICK_RELEASE = 2;//已发布
    public static final int QUICK_REFUSE = 3;//拒绝发布
	
    public static final String TOOL_BUTTON_TYPE="03";	  //工具按钮
    
    public static final String ROOT_ORGPARENTID="0/ ";  //组织树的根父ID(可能为0或空)
	
    
    public static final int  CHANNEL_HTGL = 1;         //渠道标志，后台
    public static final int  CHANNEL_JKYY = 2;		   //渠道标志，接口应用
    
    public static final String SYSLOG_TYPE_OPERATE = "1";    //系统日志
    public static final String SYSLOG_TYPE_SERVICE = "2"; 	 //业务日志
    
    public static final int PAGESIZE = PropKit.use("config.properties").getInt("pageSize");  //每页默认显示的记录数
    public static final int PAGENUMBER = 1;                                   //默认显示第一页 
    public static final String DATATYPE = "DATE";                             //日期型的数据类型

    public static final String JQGRID_GROUPOP_ALL = "AND";                    
    public static final String JQGRID_GROUPOP_ANY = "OR";
    
    public static final String UPLOAD_SERVER_URL = PropKit.use("config.properties").get("uploadServerUrl");  //上传文件url
    
    public static void saveOptLog(){
    	
    }
    /**
	 * 获得当前应用路径
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}
	
	
	/**
	 * 
	 * 功能描述:深复制
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
}
