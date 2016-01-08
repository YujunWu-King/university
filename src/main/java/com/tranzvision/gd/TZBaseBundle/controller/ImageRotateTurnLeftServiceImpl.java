package com.tranzvision.gd.TZBaseBundle.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 
 * @author tang 图片左旋转
 */
@Controller
@RequestMapping(value = "/")
public class ImageRotateTurnLeftServiceImpl {

	@RequestMapping(value = "ImageRotateTurnLeft", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgUploadFileHandler(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<>();

		try {
			String imaName = request.getParameter("imaName");
			String imaPath = request.getParameter("imaPath");
			
			String path = request.getServletContext().getRealPath(imaPath);
			 String imgPath = "";
			 
		    if((path.lastIndexOf(File.separator) + 1) != path.length()){
		    	path = path + File.separator;
		    	imgPath = path + File.separator + imaName ; 
		    }else{
		    	imgPath = path + imaName ; 
		    }
		    
		   
			
			FileInputStream fis = new FileInputStream(imgPath);
			BufferedImage bufferedImg = ImageIO.read(fis);
			//图片宽度
			int width = bufferedImg.getWidth();
			int height = bufferedImg.getHeight();
			//得到后缀
			int pos = imaName.lastIndexOf(".");
			String imaType = imaName.substring(pos + 1);
			
			//开始写入
			BufferedImage biFlip = new BufferedImage(height, width, bufferedImg.getType());
			for (int i = 0; i < width; i++){
				for (int j = 0; j < height; j++){
					biFlip.setRGB(j, width - 1 - i, bufferedImg.getRGB(i, j));
				}	
			}
			
			String newSysFileName = (new StringBuilder(String.valueOf(getNowTime()))).append(".").append(imaType)
					.toString();
			String toUrl = path + newSysFileName;
			ImageIO.write(biFlip, imaType, new File(toUrl));
			biFlip.flush();	
			bufferedImg.flush();
			fis.close();
			
			File file = new File(imgPath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
			//返回信息
			map.put("imaUrl", imaPath+newSysFileName);
			map.put("imaName", newSysFileName);
			map.put("error", "");
			return jacksonUtil.Map2json(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("ImaUrl", "");
			map.put("name", "");
			map.put("error", "保存失败");
			return jacksonUtil.Map2json(map);
		}
	}
	
	protected String getNowTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		int hour = cal.get(10);
		int minute = cal.get(12);
		int second = cal.get(13);
		int mi = cal.get(14);
		long num = cal.getTimeInMillis();
		int rand = (int) (Math.random() * 899999 + 100000);
		return (new StringBuilder()).append(year).append(month).append(day).append(hour).append(minute).append(second)
				.append(mi).append(num).append("_").append(rand).toString();
	}
	
}
