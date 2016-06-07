/**
 * 实现文件和文件夹管理.
 */
package com.typartner.find.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现文件和文件夹管理.<br>
 * 处理文件拷贝、文件夹拷贝、删除整个目录、强制删除指定目录、删除文件<br>
 * 得到无后缀名的文件名、得到文件后缀名.
 * 
 * @author gm
 * 
 */
public class FileHelper {
	
	private static final int BUFFER_SIZE = 16 * 1024 ;

	/**
	 * 文件夹拷贝.<br>
	 * 将目标文件夹下所有的目录和文件全部拷贝到.
	 * 
	 * @param sourcePath
	 *            源文件夹
	 * @param targetPath
	 *            目标文件夹
	 * @throws IOException
	 *             抛出输出异常
	 */
	public static void copyDirectiory(String sourcePath, String targetPath)
			throws IOException {
		File sourceFileD = new File(sourcePath);
		if (sourceFileD.exists() && sourceFileD.canRead()
				&& sourceFileD.isDirectory()) {
			(new File(targetPath)).mkdirs();
			File[] file = (new File(sourcePath)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					FileInputStream input = new FileInputStream(file[i]);
					FileOutputStream output = new FileOutputStream(targetPath
							+ "/" + file[i].getName());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (file[i].isDirectory()) {
					copyDirectiory(sourcePath + "/" + file[i].getName(),
							targetPath + "/" + file[i].getName());
				}
			}
		}
	}

	/**
	 * 实现文件拷贝.
	 * 
	 * @param sourcePath
	 *            原文件路径S
	 * @param newFolder
	 *            目标文件夹路径
	 * @throws IOException
	 */
	public static File copyFile(String sourcePath, String newFolder)
			throws IOException {
		File f = new File(sourcePath);
		if (!f.exists())
			return null;// 不存在返回失败
		if (f.isDirectory())
			return null;// 文件夹返回失败

		File folder = new File(newFolder);
		if (folder == null)
			return null;
		// if(folder.isFile())return null;//目标不是文件夹
		if (!folder.exists()) {
			folder.mkdirs();
		}

		FileInputStream in = new FileInputStream(f);
		String fileName = f.getName();
		String nameWithoutExt = getNameWithoutExtension(fileName);
		String ext = getExtension(fileName);
		File pathToSave = new File(newFolder, fileName);
		// String newName ="";
		int counter = 1;
		while (pathToSave.exists()) {
			fileName = nameWithoutExt + "(" + counter + ")" + "." + ext;
			pathToSave = new File(newFolder, fileName);
			counter++;
		}
		FileOutputStream output = new FileOutputStream(pathToSave);
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = in.read(b)) != -1) {
			output.write(b, 0, len);
		}
		output.flush();
		output.close();
		in.close();
		return pathToSave;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件.
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return true;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件.
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否.
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 获得文件的后缀名.<br>
	 * 例如 test.doc 使用该方法可获得后缀名"doc".
	 * 
	 * @param fileName
	 *            文件全名
	 * @return 返回文件后缀名
	 */
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i < 0)
			return "";
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 获得文件名，该文件名不存在有后缀名.<br>
	 * 例如 test.doc使用该方法可获得文件名"test".
	 * 
	 * @param fileName
	 *            文件全名
	 * @return 返回不含有后缀名的文件名
	 */
	public static String getNameWithoutExtension(String fileName) {
		int i = fileName.lastIndexOf(".");
		if (i < 0)
			return fileName;
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * 写文件.
	 * 
	 * @param data
	 *            byte数据
	 * @param filePath
	 *            写文件目录
	 * @param fileName
	 *            具体文件名
	 * @throws IOException
	 *             抛出输出异常
	 */
	public static void writeToFile(byte[] data, String filePath, String fileName)
			throws IOException {
		// 如果filePath不以文件分隔符结尾，自动添加文件分隔符
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		// 如何文件夹不存,则创建一个
		File f = new File(filePath);
		if (!f.exists()) {
			f.mkdir();
		}
		// 写入文件
		FileInputStream in = null;
		FileOutputStream output = null;
		byte[] b = null;
		try {
			in = new FileInputStream(f);
			b = new byte[in.available()];
			// FileHelper.writeToFile(b, uploadPath, filename);
			output = new FileOutputStream(filePath + fileName);
			int len;
			while ((len = in.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
		} finally {
			if (in != null) {
				in.close();
			}
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * 服务器拷贝文件到客户端
	 * 
	 * @param src
	 * @param dst
	 * 
	 */
	public static void copy2com(String filePath, String filename,
			HttpServletResponse response) throws RuntimeException {
		try {
			FileInputStream fis = null;
			ServletOutputStream os = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			//System.out.println(filePath+"@@@@@@@@###########"+filename);
			try {
				fis = new FileInputStream(new File(filePath));
				
				bis = new BufferedInputStream(fis);
				os = response.getOutputStream();
				bos = new BufferedOutputStream(os);
				filename = URLEncoder.encode(filename, "UTF-8");
				filename = new String(filename.getBytes("UTF-8"), "GBK");

				response.reset();
				response.setContentType("UTF-8");
				response.setContentType("Application/x-msdownload");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + filename);
				response.setHeader("Content-Length", String.valueOf(bis
						.available()));

				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("没有找到该文件", e);
			} catch (Exception e) {
				throw new RuntimeException("读取文件发生错误", e);
			} finally {
				try {
					bos.flush();
					bos.close();
					bis.close();
					os.close();
					fis.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (RuntimeException se) {
			throw se;
		} catch (Exception e) {
			throw new RuntimeException("服务器拷贝文件到客户端发生业务错误", e);
		}
	}
	
	/**
     * 拷贝文件到服务器
     * @param src
     * @param dst
     */   
	public static void copy2Server(File src, File dst) throws RuntimeException{
        try {
           InputStream in = null ;
           OutputStream out = null ;
            try {                
               in = new BufferedInputStream( new FileInputStream(src),BUFFER_SIZE);
               out = new BufferedOutputStream( new FileOutputStream(dst),BUFFER_SIZE);
                byte [] buffer = new byte [BUFFER_SIZE];
                while (in.read(buffer) > 0 ) {
                   out.write(buffer);
               } 
           } finally {
                if ( null != in) {
                   in.close();
               } 
                if ( null != out) {
                   out.close();
               } 
           } 
       } catch (FileNotFoundException e) {
    	   throw new RuntimeException("上传文件配置路径发生错误",e);
       } catch (Exception e) {
    	   throw new RuntimeException("上传文件发生数据流错�?",e);
       } 
   }
 
}
