/**
 * 
 */
package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/** 
 * @ClassName: ProjectPoi 
 * @Description: 项目标点
 * @author zhanglei
 * @date 2016年2月4日 下午8:56:03  
 */
@TableBind(tableName = "project_poi")
public class ProjectPoi extends Model<ProjectPoi>{
	private static final long serialVersionUID = -2782723052171371765L;
	public static final ProjectPoi dao = new ProjectPoi();
	
	/**
	 * 根据项目ID查询点
	 * @param type
	 * @return
	 */
	public List<ProjectPoi> findPoiByProjectId(int projectId){
		return find("select s.* from project_poi s where s.PROJECT_ID = ?", projectId);
	}
	
	/**
	 * 清除已选中的项目类型
	 * @param projectId
	 */
	public void clearByProjectId(Integer projectId){
		Db.batch("delete from project_poi where PROJECT_ID=?",  new Object[][]{{projectId}}, 1000);
	}
}
