/**
 * 
 */
package com.tranzvision.gd.util.sql;

import java.util.Map;
import java.util.Properties;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

/**
 * @author LiGang
 * 2015/11/3
 */
@Service
public class SQLObjectManager
{
	private String basePath;
	
	private Lock lock;
	
	private Map<String,String> sqlObjects;
	
	@Autowired
	private GetCookieSessionProps getCookieSessionProps;
	
	public SQLObjectManager()
	{
		Resource resource = new ClassPathResource("conf/cookieSession.properties");
		try
		{
			Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
			basePath = System.getProperty(webAppRootKey) + "WEB-INF" + File.separator + "classes" + File.separator + "sql" + File.separator;
			lock = new ReentrantLock();
			sqlObjects = new HashMap<String,String>();
			
			init();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void init()
	{
		try
		{
			File root = new File(basePath);
			
			readSQLObjects(root,null);
		}
		catch(Exception e)
		{
			;
		}
	}
	
	private void readSQLObjects(File root,String prefix) throws TzSystemException
	{
		File[] files = root.listFiles();
		
		for(File file:files)
		{
			String fName = file.getName();
			
			String tmpPrefix = "";
			if(prefix == null)
			{
				tmpPrefix = fName;
			}
			else
			{
				tmpPrefix = prefix.trim();
				if(tmpPrefix.equals("") == true)
				{
					tmpPrefix = fName;
				}
				else
				{
					tmpPrefix += "." + fName;
				}
			}
			
			if(file.isDirectory() == true)
			{//递归读取子目录下的SQL对象
				readSQLObjects(file,tmpPrefix);
			}
			else
			{
				//去掉文件后缀
				if(tmpPrefix.toUpperCase().endsWith(".SQL") == true)
				{
					tmpPrefix = tmpPrefix.substring(0, tmpPrefix.length() - 4);
				}
				
				if(sqlObjects.containsKey(tmpPrefix) == false)
				{
					if(fName.toUpperCase().endsWith(".SQL") == true)
					{
						sqlObjects.put(tmpPrefix, readFileContent(file));
					}
				}
			}
		}
	}
	
	private String readFileContent(File file) throws TzSystemException
	{
		StringBuffer strbuff = new StringBuffer();
		Reader reader = null;
		
		try
		{
			reader = new InputStreamReader(new FileInputStream(file));
			int tmpChar;
			while((tmpChar = reader.read()) != -1)
			{
				strbuff.append((char)tmpChar);
			}
		}
		catch(Exception e)
		{
			throw new TzSystemException("error: can't find the specified SQL object file \n" + e.toString());
		}
		finally
		{
			try
			{
				if(reader != null)
				{
					reader.close();
				}
			}
			catch(Exception e)
			{
				;
			}
		}
		
		return strbuff.toString();
	}
	
	public String getSQLText(String sqlName,boolean refreshFlag) throws TzSystemException
	{
		String sqlText = "";
		String tmpSQLName = sqlName.substring(4);
		
		//调试模式
		if(getCookieSessionProps.getDebug()){
			refreshFlag = true;
		}
		
		if(sqlObjects.containsKey(tmpSQLName) == false || refreshFlag == true)
		{
			lock.lock();
			
			if(sqlObjects.containsKey(tmpSQLName) == false || refreshFlag == true)
			{
				try
				{
					String fName = basePath + tmpSQLName.replace('.', File.separatorChar) + ".sql";
					String sqlContent = readFileContent(new File(fName));
					sqlObjects.put(tmpSQLName, sqlContent);
				}
				catch(Exception e)
				{
					;
				}
			}
			
			lock.unlock();
		}
		
		if(sqlObjects.containsKey(tmpSQLName) == true)
		{
			sqlText = sqlObjects.get(tmpSQLName);
		}
		else
		{
			throw new TzSystemException("error: can't find the specified SQL object \"" + sqlName + "\".");
		}
		
		return sqlText;
	}
}
