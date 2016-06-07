package com.typartner.find.common.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/** 
 * @ClassName: ProjectCat 
 * @Description: 
 * @author hety
 * @date 2015年12月23日 下午15:40:36  
 */
@TableBind(tableName = "project_cat")
public class ProjectCat extends Model<ProjectCat>{

	private static final long serialVersionUID = -5671317638255539124L;
	public static final ProjectCat dao = new ProjectCat();
	
	/**
	 * 清除已选中的项目类型
	 * @param projectId
	 */
	public void clearByProjectId(Integer projectId){
		Db.batch("delete from project_cat where PROJECT_ID=?",  new Object[][]{{projectId}}, 1000);
	}
	
	/**
	 * 根据projectId查询类别
	 * @param projectId
	 * @return
	 */
	public List<ProjectCat> getCatByProjectId(Integer projectId){
		return find("select s.* from project_cat s where s.PROJECT_ID = ?", projectId);
	}

}
