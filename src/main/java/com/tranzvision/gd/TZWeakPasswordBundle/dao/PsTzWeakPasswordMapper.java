package com.tranzvision.gd.TZWeakPasswordBundle.dao;

import com.tranzvision.gd.TZWeakPasswordBundle.model.PsTzWeakPassword;

public interface PsTzWeakPasswordMapper {
	PsTzWeakPassword selectByPrimaryKey(String tzPwdId);
	
	int insert(PsTzWeakPassword record);
	
	int updateByPrimaryKey(PsTzWeakPassword record);
	
	int deleteByPrimaryKey(String tzPwdId);
}
