package com.typartner.find.common.util;

import java.io.File;
import java.util.Date;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.upload.UploadFile;

/**
 * 文件上传
* @ClassName: FileUploadController 
* @Description: 
* @author zhanglei
* @date 2015年12月26日 下午3:47:03
 */
@ControllerBind(controllerKey="/upload")
public class UploadController extends Controller {
	
	private final static Integer IMG_MAX_SIZE = 1 * 1024 * 1024; //图片最大几兆
	private final static String UPLOAD_ROOT_PATH = "upload/";
	private final static String UPLOAD_IMG_PATH = "img/";
	
	/**
	 * 图片上传
	 * 前台input file 的name需为：imgForUpload
	 */
	public void imgUpload(){
		String newFileName = String.valueOf(new Date().getTime());
        UploadFile uf = getFile("imgForUpload", UPLOAD_IMG_PATH, IMG_MAX_SIZE, "UTF-8");
        if (uf != null) {
        	String suffix = uf.getFileName().substring(uf.getFileName().lastIndexOf("."));
        	uf.getFile().renameTo(new File(uf.getSaveDirectory() + File.separator + newFileName + suffix));
        	String fileName = UPLOAD_ROOT_PATH + UPLOAD_IMG_PATH + newFileName + suffix;
        	setAttr("fileName", fileName);
		}
        renderJson();
	}
	
}
