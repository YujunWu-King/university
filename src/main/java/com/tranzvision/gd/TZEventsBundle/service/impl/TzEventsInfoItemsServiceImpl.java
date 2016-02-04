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
 * 获取活动报名信息项，原PS：TZ_GD_HDGL:ActivityInfoItems
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsInfoItemsServiceImpl")
public class TzEventsInfoItemsServiceImpl extends FrameworkImpl {

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
