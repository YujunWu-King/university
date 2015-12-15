/**
 * 
 */
package com.tranzvision.gd.util.sql;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzSQLObject;
import com.tranzvision.gd.util.sql.SQLObjectManager;
import com.tranzvision.gd.util.base.HTMLObjectManager;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.batch.engine.base.BaseEngine;

/**
 * @author LiGang
 * 2015/10/30
 */
@Service
public class TZGDObject
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@Autowired
	private SQLObjectManager sqlObjectManager;
	
	@Autowired
	private HTMLObjectManager htmlObjectManager;
	
	private String basePath = "";
	
	public TZGDObject()
	{
		;
	}
	
	public String getWebAppRootPath()
	{
		if(basePath == null || basePath.trim().equals("") == true)
		{
			Resource resource = new ClassPathResource("conf/cookieSession.properties");
			try
			{
				Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
				String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
				basePath = System.getProperty(webAppRootKey);
			}
			catch(IOException ioe)
			{
				basePath = "";
				ioe.printStackTrace();
			}
			catch(Exception e)
			{
				basePath = "";
				e.printStackTrace();
			}
		}
		
		return basePath;
	}
	
	/**
	 * 返回事务对象的方法
	 */
	public TransactionStatus getTransaction()
	{
		return transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	/**
	 * 回滚事务的方法
	 */
	public void rollback(TransactionStatus status)
	{
		transactionManager.rollback(status);
	}
	
	/**
	 * 提交事务的方法
	 */
	public void commit(TransactionStatus status)
	{
		transactionManager.commit(status);
	}
	
	/**
	 * 获取当前操作系统类型的方法
	 */
	public String getOSType()
	{
	    String l_OSType = "";
	    
	    String t_OSType = System.getProperty("os.name").toUpperCase().substring(0,3);
	    
	    if(t_OSType.equals("WIN") == true)
	    {
	    	l_OSType = "WINDOWS";
	    }
	    else
	    {
	    	l_OSType = "UNIX";
	    }
	    
	    return l_OSType;
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * args			TzSQLData类型的数组，用户指定接收SQL执行结果的变量数组，注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	public void sqlExec(String sqlText,TzSQLData...args) throws TzSystemException
	{
		sqlExec(sqlText,null,args);
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * params		SqlParams类型，用于指定执行的SQL所需要的参数
	 * args			TzSQLData类型的数组，用户指定接收SQL执行结果的变量数组，注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	public void sqlExec(String sqlText,SqlParams params,TzSQLData...args) throws TzSystemException
	{
		String tmpSQLText = sqlText.trim().toUpperCase();
		
		if(tmpSQLText.startsWith("SELECT") == true)
		{//执行查询SQL语句
			querySqlExec(sqlText,params,args);
		}
		else if(tmpSQLText.startsWith("UPDATE") == true || tmpSQLText.startsWith("DELETE") == true || tmpSQLText.startsWith("INSERT") == true)
		{//执行非查询SQL语句
			updateSqlExec(sqlText,params);
		}
		else
		{//执行其他既不是查询类型，又不是插入、更新或者删除类型的SQL
			jdbcTemplate.execute(sqlText);
		}
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * params		SqlParams类型，用于指定执行的SQL所需要的参数
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	private void updateSqlExec(String sqlText,SqlParams params) throws TzSystemException
	{
		if(params != null)
		{
			jdbcTemplate.update(sqlText, params.getValue());
		}
		else
		{
			jdbcTemplate.update(sqlText);
		}
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * params		SqlParams类型，用于指定执行的SQL所需要的参数
	 * args			TzSQLData类型的数组，用户指定接收SQL执行结果的变量数组，注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	private void querySqlExec(String sqlText,SqlParams params,TzSQLData...args) throws TzSystemException
	{
		//获取结果集
		SqlRowSet rs = null;
		
		if(params != null && params.getValue() != null)
		{
			rs = jdbcTemplate.queryForRowSet(sqlText, params.getValue());
		}
		else
		{
			rs = jdbcTemplate.queryForRowSet(sqlText);
		}
		
		//获取结果集的列数
		int colCount = rs.getMetaData().getColumnCount();
		
		//如果接收结果的参数为空，则直接抛出异常
		if(colCount > 0 && args == null)
		{
			throw new TzSystemException("error: there is no argument to receive the resultset.\nSQL:" + sqlText);
		}
		
		//如果结果集的列数与接收结果集的参数个数不一致，则抛出异常
		if(colCount > 0 && colCount > args.length)
		{
			throw new TzSystemException("error: there are not enough arguments to receive the resultset.\nSQL:" + sqlText);
		}
		else if(colCount > 0 && colCount < args.length)
		{
			throw new TzSystemException("error: there are too many arguments to receive the resultset.\nSQL:" + sqlText);
		}
		
		//获取结果集
		if(colCount > 0 && rs.first() == true)
		{
			for(int i=0;i<colCount;i++)
			{
				args[i].setValue(rs, i + 1);
			}
		}
		else if(colCount > 0)
		{
			for(int i=0;i<colCount;i++)
			{
				args[i].clearValue();
			}
		}
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * params		SqlParams类型，用于指定执行的SQL所需要的参数
	 * rec			TzRecord类型的数组，用于以记录的形式返回查询结果的参数
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	public void sqlExec(String sqlText,SqlParams params,TzRecord rec) throws TzSystemException
	{
		String tmpSQLText = sqlText.trim().toUpperCase();
		
		//如果接收结果的参数为空，则直接抛出异常
		if(rec == null)
		{
			throw new TzSystemException("error: there is no record object to receive the resultset.\nSQL:" + sqlText);
		}
		
		//如果是非查询SQL，则直接抛出异常
		if(tmpSQLText.startsWith("SELECT") == false)
		{
			throw new TzSystemException("error: the sql is not a query sql.\nSQL:" + sqlText);
		}
		
		//获取结果集
		SqlRowSet rs = null;
		if(params != null && params.getValue() != null)
		{
			rs = jdbcTemplate.queryForRowSet(sqlText, params.getValue());
		}
		else
		{
			rs = jdbcTemplate.queryForRowSet(sqlText);
		}
		
		//获取结果集
		rec.setColList(rs);
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * rec			TzRecord类型的数组，用于以记录的形式返回查询结果的参数
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	public void sqlExec(String sqlText,TzRecord rec) throws TzSystemException
	{
		sqlExec(sqlText,null,rec);
	}
	
	/**
	 * 参数说明：
	 * sqlText		String类型，用于指定需要执行的SQL语句文本
	 * args			Object类型的数组，用户指定执行SQL所需要参数列表，注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn
	 * 返回结果：
	 * 无返回结果，SQL的执行结果通过args参数列表中的参数返回。注意，该方法将会把遇到的异常向上直接抛出
	 */
	public TzSQLObject createSQLObject(String sqlText,Object...args) throws TzSystemException
	{
		TzSQLObject tmpSQLObject = null;
		
		String tmpSQLText = sqlText.trim().toUpperCase();
		//如果是非查询SQL，则直接抛出异常
		if(tmpSQLText.startsWith("SELECT") == false)
		{
			throw new TzSystemException("error: the sql is not a query sql.\nSQL:" + sqlText);
		}
		
		//获取结果集
		SqlRowSet rs = null;
		if(args != null)
		{
			rs = jdbcTemplate.queryForRowSet(sqlText, args);
		}
		else
		{
			rs = jdbcTemplate.queryForRowSet(sqlText);
		}
		
		tmpSQLObject = new TzSQLObject();
		tmpSQLObject.setValue(rs, sqlText);
		
		return tmpSQLObject;
	}
	
	/**
	 * 参数说明：
	 * sqlObjectName		String类型，用户指定SQL对象的名称。SQL对象名称的格式为：SQL.xxx.xxx.xxx。其中“SQL.”为固定前缀，其对应
	 *                      /WEB-INF/classes/sql/xxx/xxx/xxx.sql文件中的内容，大小写敏感。注意，SQL对象对应的文件必须以小写的“.sql”结尾。
	 * refreshFlag			boolean类型，用于指定本次调用是否重新从对应的文件中读取SQL对象的内容，否则直接从缓存中获取。
	 * 返回结果：
	 * 返回结果String类型的SQL对象对应的文本内容，如果找不到对应的SQL对象，则抛出TzSystemException类型的异常。
	 */
	public String getSQLText(String sqlObjectName,boolean refreshFlag) throws TzSystemException
	{
		return sqlObjectManager.getSQLText(sqlObjectName,refreshFlag);
	}
	
	/**
	 * 参数说明：
	 * sqlObjectName		String类型，用户指定SQL对象的名称。SQL对象名称的格式为：SQL.xxx.xxx.xxx。其中“SQL.”为固定前缀，其对应
	 *                      /WEB-INF/classes/sql/xxx/xxx/xxx.sql文件中的内容，大小写敏感。注意，SQL对象对应的文件必须以小写的“.sql”结尾。
	 * 返回结果：
	 * 返回结果String类型的SQL对象对应的文本内容，如果找不到对应的SQL对象，则抛出TzSystemException类型的异常。
	 */
	public String getSQLText(String sqlObjectName) throws TzSystemException
	{
		boolean refreshFlag = false;
		return sqlObjectManager.getSQLText(sqlObjectName,refreshFlag);
	}
	
	/**
	 * 参数说明：
	 * htmlObjectName		String类型，用户指定HTML对象的名称。HTML对象名称的格式为：HTML.xxx.xxx.xxx。其中“HTML.”为固定前缀，其对应
	 *                      /WEB-INF/classes/html/xxx/xxx/xxx.html文件中的内容，大小写敏感.注意，HTML对象对应的文件必须以小写的“.html”结尾。
	 * refreshFlag			boolean类型，用于指定本次调用是否重新从对应的文件中读取HTML对象的内容，否则直接从缓存中获取。
	 * args					String类型的数组，用于合成HTML代码所需要参数列表，系统将用该参数指定的字符串顺序替换%bind(:n)。例如，使用arg[0]替换所有的%bind(:1),
	 *                      使用arg[1]替换所有的%bind(:2)，...，使用arg[n-1]替换所有的%bind(:n)，但“\%bind(:n)”不会被替换，即只要“%bind(:n)”前面
	 *                      添加了“\”，则该字符串将被系统忽略而不会被替换。注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn。
	 * 返回结果：
	 * 返回结果String类型的SQL对象对应的文本内容，如果找不到对应的SQL对象，则抛出TzSystemException类型的异常。
	 */
	public String getHTMLText(String htmlObjectName,boolean refreshFlag,String...args) throws TzSystemException
	{
		String htmlText = htmlObjectManager.getHTMLText(htmlObjectName,refreshFlag);
		
		if(args != null)
		{
			for(int i=0;i<args.length;i++)
			{
				String repStr1 = "(?<!\\\\)(?i)%bind\\(\\:" + (i + 1) + "\\)";
				String repStr2 = "\\\\(?i)%bind\\(\\:" + (i + 1) + "\\)";
				String repStr3 = "%bind(:" + (i + 1) + ")";
				
				htmlText = htmlText.replaceAll(repStr1, args[i]);
				htmlText = htmlText.replaceAll(repStr2, repStr3);
			}
		}
		
		return htmlText;
	}
	
	/**
	 * 参数说明：
	 * htmlObjectName		String类型，用户指定HTML对象的名称。HTML对象名称的格式为：HTML.xxx.xxx.xxx。其中“HTML.”为固定前缀，其对应
	 *                      /WEB-INF/classes/html/xxx/xxx/xxx.html文件中的内容，大小写敏感.注意，HTML对象对应的文件必须以小写的“.html”结尾。
	 * args					String类型的数组，用于合成HTML代码所需要参数列表，系统将用该参数指定的字符串顺序替换%bind(:n)。例如，使用arg[0]替换所有的%bind(:1),
	 *                      使用arg[1]替换所有的%bind(:2)，...，使用arg[n-1]替换所有的%bind(:n)，但“\%bind(:n)”不会被替换，即只要“%bind(:n)”前面
	 *                      添加了“\”，则该字符串将被系统忽略而不会被替换。注意，这个变量数组可以采用个数不确定的参数列表的形式，例如p1,p2,p3...pn。
	 * 返回结果：
	 * 返回结果String类型的SQL对象对应的文本内容，如果找不到对应的SQL对象，则抛出TzSystemException类型的异常。
	 */
	public String getHTMLText(String htmlObjectName, String...args) throws TzSystemException
	{
		boolean refreshFlag = false;
		
		String htmlText = this.getHTMLText(htmlObjectName, refreshFlag, args);
		
		return htmlText;
	}
	
	/**
	 * 参数说明：
	 * tblName				String类型，指定需要创建的表记录对象的名称。
	 * 返回结果：
	 * 返回结果TzRecord类型的指定表名称的实例对象。
	 */
	public TzRecord createRecord(String tblName) throws TzException
	{
		if(tblName == null || tblName.trim().equals("") == true)
		{
			throw new TzException("failed to create the record because the specified record name is null.");
		}
		
		TzRecord tmpRecord = new TzRecord();
		
		try
		{
			Method method = tmpRecord.getClass().getDeclaredMethod("readyForCreateRecord", String.class,TZGDObject.class,JdbcTemplate.class);
			method.setAccessible(true);
			method.invoke(tmpRecord,tblName,this,this.jdbcTemplate);
		}
		catch(Exception e)
		{
			throw new TzException("an error occurred when try to create a record by name [" + tblName + "].\n" + e.toString());
		}
		
		return tmpRecord;
	}
	
	/**
	 * 参数说明：
	 * orgId				String类型，指定需要创建的Job进程归属的机构。
	 * procName				String类型，指定需要创建的Job进程的类别。
	 * 返回结果：
	 * 返回结果BaseEngine类型的Job进程实例对象。
	 */
	public BaseEngine createEngineProcess(String orgId,String procName)
	{
		BaseEngine tmpEngineProcess = null;
		
		//todo
		
		return tmpEngineProcess;
	}
}
