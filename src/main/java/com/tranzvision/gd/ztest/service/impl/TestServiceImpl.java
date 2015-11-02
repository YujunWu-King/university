package com.tranzvision.gd.ztest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.ztest.dao.AdminMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.ztest.model.Admin;

@Service("com.tranzvision.gd.ztest.service.impl.TestServiceImpl")
public class TestServiceImpl extends FrameworkImpl {

	@Autowired
    private AdminMapper AdminMapper;
    
    ///*
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //*/
    
	public TestServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	@Override
	public String testMethod(int id) {
		
		Admin admin = AdminMapper.selectByPrimaryKey(id);
		
		admin.getAdminRealname();
		
		return admin.getAdminRealname();
	}
	*/
	

}
