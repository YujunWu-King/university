package com.tranzvision.gd.TZBaseBundle.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.imaScaling.ImaScaling;
//import com.tranzvision.gd.util.imaScaling.Msg;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;

/**
 * 
 * @author tang
 * 图片剪切
 */
@Controller
@RequestMapping(value = "/")
public class ImaScalingController {
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;
	  
	@RequestMapping(value = "ImaScalingServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgUploadFileHandler(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<>();
		
		try{
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String rootPath = getSysHardCodeVal.getWebsiteFileUploadPath();
			
		    String currentPath = request.getParameter("imaPath");
		    if ((currentPath == null) || ("".equals(currentPath))) {
		    	currentPath = rootPath + "/" + orgid;
		    }else{
		    	currentPath = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(currentPath);
		    	if("/".equals(currentPath.substring(0, 1))){
		    		currentPath = rootPath +"/" + orgid + currentPath;
		    	}else{
		    		currentPath = rootPath +"/" + orgid +"/" + currentPath;
		    	}
		    	
		    }
		    String currentDirPath = request.getServletContext().getRealPath(currentPath);
		    File userDir = new File(currentDirPath);
		    if (!userDir.exists()) {
		      userDir.mkdir();
		    }
		    String sysfilename = request.getParameter("sysfilename");
		    File pathToSave = new File(currentDirPath, sysfilename);
	
		    String x1 = request.getParameter("x1");
		    String y1 = request.getParameter("y1");
		    String x2 = request.getParameter("x2");
		    String y2 = request.getParameter("y2");
		    String w = request.getParameter("w");
		    String h = request.getParameter("h");
	
		    if (x1.indexOf(".") >= 0) {
		      x1 = x1.substring(0, x1.indexOf(".") + 1);
		    }
	
		    if (y1.indexOf(".") >= 0) {
		      y1 = y1.substring(0, y1.indexOf(".") + 1);
		    }
		    if (x2.indexOf(".") >= 0) {
		      x2 = x2.substring(0, x2.indexOf(".") + 1);
		    }
	
		    if (y2.indexOf(".") >= 0) {
		      y2 = y2.substring(0, y2.indexOf(".") + 1);
		    }
		    if (w.indexOf(".") >= 0) {
		      w = w.substring(0, w.indexOf(".") + 1);
		    }
	
		    if (h.indexOf(".") >= 0) {
		      h = h.substring(0, h.indexOf(".") + 1);
		    }
	
		    double x_1 = Double.parseDouble(x1);
		    double y_1 = Double.parseDouble(y1);
	
		    double w_2 = Double.parseDouble(w);
		    double h_2 = Double.parseDouble(h);
	
		    int ix_1 = (int)(x_1 + 0.5D);
		    int iy_1 = (int)(y_1 + 0.5D);
		    int iw_2 = (int)(w_2 + 0.5D);
		    int ih_2 = (int)(h_2 + 0.5D);
	
		    FileInputStream fis = new FileInputStream(pathToSave);
		    BufferedImage bufferedImg = ImageIO.read(fis);
		    double imgWidth = bufferedImg.getWidth();
		    double imgHeight = bufferedImg.getHeight();
		    fis.close();
	
		    if ((imgWidth > 300.0D) || (imgHeight > 360.0D)) {
		      double ratio = imgWidth / 300.0D > imgHeight / 360.0D ? imgWidth / 300.0D : imgHeight / 360.0D;
	
		      ix_1 = (int)(x_1 * ratio + 0.5D);
		      iy_1 = (int)(y_1 * ratio + 0.5D);
		      iw_2 = (int)(w_2 * ratio + 0.5D);
		      ih_2 = (int)(h_2 * ratio + 0.5D);
		    }
		    //Msg msg = new Msg();
		    ImaScaling.scissor(ix_1, iy_1, iw_2, ih_2, pathToSave.toString(), currentDirPath + "/new" + sysfilename);
	
		    if ((pathToSave.isFile()) && (pathToSave.exists())) {
		      pathToSave.delete();
		    }
		    currentDirPath = currentDirPath.replace('\\', '/');
		    
		    
		    map.put("ImaUrl", currentPath);
		    map.put("name", "new" + sysfilename);
		    map.put("error", "");
		    return jacksonUtil.Map2json(map);
		    //msg.setImaUrl(currentPath);
		    //msg.setName("new" + sysfilename);
		    //String jsonString = JSON.toJSONString(msg);
		    //String jsonString = "";
		    //PrintWriter out2 = response.getWriter();
		    //out2.print(jsonString);
		}catch(Exception e){
			e.printStackTrace();
			map.put("ImaUrl", "");
		    map.put("name", "");
		    map.put("error", "保存失败");
		    return jacksonUtil.Map2json(map);
		}
	 }
}
