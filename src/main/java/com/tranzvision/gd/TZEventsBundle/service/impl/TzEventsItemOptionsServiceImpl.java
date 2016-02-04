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
 * 报名项下拉值定义，原PS：TZ_GD_HDGL:ActivityItemOption
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsItemOptionsServiceImpl")
public class TzEventsItemOptionsServiceImpl extends FrameworkImpl {

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
