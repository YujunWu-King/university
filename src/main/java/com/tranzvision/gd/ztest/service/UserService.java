package com.tranzvision.gd.ztest.service;
 
import java.util.List;

import com.tranzvision.gd.ztest.model.User;
 
 
public interface UserService {
 
    public int insertUser(User user);
    
    public User getOneUser(int id);
    ///*
    public List getAllUsers();
    //*/
}