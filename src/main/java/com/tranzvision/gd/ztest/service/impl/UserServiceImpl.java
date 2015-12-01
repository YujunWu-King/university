package com.tranzvision.gd.ztest.service.impl;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
 
import com.tranzvision.gd.ztest.dao.UserMapper;
import com.tranzvision.gd.ztest.model.User;
import com.tranzvision.gd.ztest.service.UserService;

@Service
public class UserServiceImpl implements UserService{
 
    @Autowired
    private UserMapper userDAO;
    
    ///*
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //*/
    public int insertUser(User user) {
        // TODO Auto-generated method stub
        return userDAO.insert(user);
    }
    
    public User getOneUser(int id){
    	return userDAO.selectByPrimaryKey(id);
    }
    
    ///*
    public List getAllUsers() {
		//System.out.println("jdbcTemplate=" + jdbcTemplate);
		
		String testvar = jdbcTemplate.queryForObject("SELECT 'Y' FROM user WHERE id=1", String.class);
		//System.out.println(testvar);
		
		
		return jdbcTemplate
				.queryForList("SELECT * FROM user WHERE 1=1");
		
	}
 	//*/
}