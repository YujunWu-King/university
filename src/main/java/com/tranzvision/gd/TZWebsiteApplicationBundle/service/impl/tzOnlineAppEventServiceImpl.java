package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请人在线报名；原：TZ_ONLINE_REG_PKG:TZ_ONLINE_APP_EVENT
 * 
 * @author 张彬彬
 * @since 2016-1-29
 */

@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppEventServiceImpl")
public class tzOnlineAppEventServiceImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;
	

}
