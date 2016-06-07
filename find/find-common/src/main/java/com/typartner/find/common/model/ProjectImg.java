/**
 * 
 */
package com.typartner.find.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/** 
 * @ClassName: Category 
 * @Description: 类别
 * @author zhanglei
 * @date 2015年11月22日 上午1:09:40  
 */
@TableBind(tableName = "project_img")
public class ProjectImg extends Model<ProjectImg> {
	
	private static final long serialVersionUID = 5015900737408329288L;
	public static final ProjectImg dao = new ProjectImg();
	
	
	public List<ProjectImg> getImgByProjectId(List<Integer> projectIdList){
		StringBuilder sb = new StringBuilder();
		List<Integer> paras = new ArrayList<Integer>();
		sb.append("select p.* from project_img p where p.PROJECT_ID in (");
		StringBuilder catPara = new StringBuilder();
		String inPara = ""; //问号参数
		for(int projectId : projectIdList){
			catPara.append("?,");
			paras.add(projectId); //具体参数
		}
		if(catPara.length() > 0){
			inPara = catPara.substring(0, catPara.length() - 1);
		}
		sb.append(inPara);
		sb.append(")");
		return find(sb.toString(), paras.toArray());
	}
	
	/**
	 * 清除已选中的项目图片
	 * @param projectId
	 */
	public void clearByProjectId(Integer projectId){
		Db.batch("delete from project_img where PROJECT_ID=?",  new Object[][]{{projectId}}, 1000);
	}
	
	/**
	 * 根据projectId查询图片
	 * @param projectId
	 * @return
	 */
	public List<ProjectImg> getImgByProjectId(Integer projectId){
		return find("select s.* from project_img s where s.PROJECT_ID = ?", projectId);
	}

}
