/**
 * 
 */
package com.tranzvision.gd.ztest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.ztest.dao.AdminMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.ztest.model.Admin;

/**
 * @author SHIHUA
 *
 */
@Service("com.tranzvision.gd.ztest.service.impl.TestFramework")
public class TestFramework extends FrameworkImpl {

	@Autowired
	private AdminMapper AdminMapper;

	/**
	 * 
	 */
	public TestFramework() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZBaseBundle.service.Framework#testMethod(int)
	 */
	/*
	@Override
	public String testMethod(int id) {
		// TODO Auto-generated method stub

		Admin admin = AdminMapper.selectByPrimaryKey(id);

		admin.getAdminRealname();

		return admin.getAdminRealname();
	}
	*/
	
}
