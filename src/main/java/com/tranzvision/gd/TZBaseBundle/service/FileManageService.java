/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义文件管理相关方法
 * 
 * @author SHIHUA
 * @since 2015-11-26
 */
public interface FileManageService {

	/**
	 * 在服务器上创建一个文件
	 * 
	 * @param parentPath
	 * @param fileName
	 * @param fileBytes
	 * @return boolean 成功 - true ，失败 - false
	 * @throws Exception
	 */
	public boolean CreateFile(String parentPath, String fileName, byte[] fileBytes) throws Exception;

	/**
	 * 在服务器上更新一个文件内容
	 * 
	 * @param parentPath
	 * @param fileName
	 * @param fileBytes
	 * @return boolean 成功 - true ，失败 - false
	 * @throws Exception
	 */
	public boolean UpdateFile(String parentPath, String fileName, byte[] fileBytes) throws Exception;

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

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
	 * 
	 * @param request
	 * @param pFileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
			throws UnsupportedEncodingException;

}
