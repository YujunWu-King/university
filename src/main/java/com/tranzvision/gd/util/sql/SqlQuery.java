/**
 * 
 */
package com.tranzvision.gd.util.sql;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.VarType2Class;

/**
 * @author SHIHUA
 * @since 2015-10-29
 */
@Service
public class SqlQuery {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private VarType2Class varType2Class;

	/**
	 * 
	 */
	public SqlQuery() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String sql, String objType) {

		try {
			return (T) jdbcTemplate.queryForObject(sql, varType2Class.change2Class(objType));
		} catch (IncorrectResultSizeDataAccessException ex) {
			System.out.println(ex.getMessage());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String sql, Object[] args, String objType) {
		try {
			return (T) jdbcTemplate.queryForObject(sql, args, varType2Class.change2Class(objType));
		} catch (IncorrectResultSizeDataAccessException ex) {
			System.out.println(ex.getMessage());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sql) {

		try {
			return (List<T>) jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sql, Object[] args) {

		try {
			return (List<T>) jdbcTemplate.queryForList(sql, args);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sql, String elmType) {

		try {
			return (List<T>) jdbcTemplate.queryForList(sql, varType2Class.change2Class(elmType));
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public Map<String, Object> queryForMap(String sql) {

		try {
			return jdbcTemplate.queryForMap(sql);
		} catch (IncorrectResultSizeDataAccessException ex) {
			System.out.println(ex.getMessage());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public Map<String, Object> queryForMap(String sql, Object[] args) {

		try {
			return jdbcTemplate.queryForMap(sql, args);
		} catch (IncorrectResultSizeDataAccessException ex) {
			System.out.println(ex.getMessage());
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	public int update(String sql) {
		try {
			return jdbcTemplate.update(sql);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

	public int update(String sql, Object[] args) {
		try {
			return jdbcTemplate.update(sql, args);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}

}
