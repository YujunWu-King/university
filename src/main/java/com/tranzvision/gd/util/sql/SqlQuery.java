/**
 * 
 */
package com.tranzvision.gd.util.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author SHIHUA
 *
 */
@Service
public class SqlQuery {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 */
	public SqlQuery() {
		// TODO Auto-generated constructor stub
	}
	
	

}
