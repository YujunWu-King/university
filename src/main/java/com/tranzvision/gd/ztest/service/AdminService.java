package com.tranzvision.gd.ztest.service;

import java.util.List;

import com.tranzvision.gd.ztest.model.Admin;

public interface AdminService {
	public int insertAdmin(Admin admin);
	public Admin getOneAdmin(int id) throws ClassNotFoundException;
	public List getAllAdmins();
}
