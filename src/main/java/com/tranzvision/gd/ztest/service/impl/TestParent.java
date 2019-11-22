/**
 * 
 */
package com.tranzvision.gd.ztest.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

/**
 * @author SHIHUA
 *
 */
public class TestParent extends FrameworkImpl {

	/**
	 * 
	 */
	public TestParent() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws Exception {
		String recname="CLASS_USER_BM";
		recname = recname.substring(recname.indexOf("_")+1, recname.length());
		System.out.println(recname);
	}
	
	

}
