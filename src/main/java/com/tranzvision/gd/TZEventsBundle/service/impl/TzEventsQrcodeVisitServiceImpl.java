/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动手机发布二维码页面，原PS：TZ_GD_HDGL:ActivityView
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsQrcodeVisitServiceImpl")
public class TzEventsQrcodeVisitServiceImpl extends FrameworkImpl{

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	
	
}
