/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 定义文件管理相关方法
 * 
 * @author SHIHUA
 * @since 2015-11-26
 */
public interface FileManageService {

	/**
	 * 再服务器上创建一个文件
	 * 
	 * @param parentPath
	 * @param fileName
	 * @param fileBytes
	 * @return boolean 成功 - true ，失败 - false
	 * @throws Exception
	 */
	public boolean CreateFile(String parentPath, String fileName, byte[] fileBytes) throws Exception;

	/**
	 * 在服务器上删除一个文件
	 * 
	 * @param parentPath
	 * @param fileName
	 * @return boolean 成功 - true ，失败 - false
	 */
	public boolean DeleteFile(String parentPath, String fileName);

	/**
	 * 在服务器上删除一个文件
	 * 
	 * @param filePath
	 * @return boolean 成功 - true ，失败 - false
	 */
	public boolean DeleteFile(String filePath);

	/**
	 * 获取图片文件的宽度和高度
	 * 
	 * @param filePath
	 * @return ArrayList<Integer> 图片文件的宽、高
	 * @throws IOException
	 */
	public ArrayList<Integer> getImageWidthHeight(String filePath) throws IOException;

}
