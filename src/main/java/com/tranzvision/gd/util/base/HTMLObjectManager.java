/**
 * 
 */
package com.tranzvision.gd.util.base;

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
public class HTMLObjectManager
{
	private String basePath;
	
	private Lock lock;
	
	private Map<String,String> htmlObjects;
	
	@Autowired
	private GetCookieSessionProps getCookieSessionProps;
	
	public HTMLObjectManager()
	{
		Resource resource = new ClassPathResource("conf/cookieSession.properties");
		try
		{
			Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
			basePath = System.getProperty(webAppRootKey) + "WEB-INF" + File.separator + "classes" + File.separator + "html" + File.separator;
			lock = new ReentrantLock();
			htmlObjects = new HashMap<String,String>();
			
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
			
			readHTMLObjects(root,null);
		}
		catch(Exception e)
		{
			;
		}
	}
	
	private void readHTMLObjects(File root,String prefix) throws TzSystemException
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
			{//递归读取子目录下的HTML对象
				readHTMLObjects(file,tmpPrefix);
			}
			else
			{
				//去掉文件后缀
				if(tmpPrefix.toUpperCase().endsWith(".HTML") == true)
				{
					tmpPrefix = tmpPrefix.substring(0, tmpPrefix.length() - 5);
				}
				
				if(htmlObjects.containsKey(tmpPrefix) == false)
				{
					if(fName.toUpperCase().endsWith(".HTML") == true)
					{
						htmlObjects.put(tmpPrefix, readFileContent(file));
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
			throw new TzSystemException("error: can't find the specified HTML object file \n" + e.toString());
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
	
	public String getHTMLText(String htmlName,boolean refreshFlag) throws TzSystemException
	{
		String htmlText = "";
		String tmpHTMLName = htmlName.substring(5);
		
		//调试模式
		if(getCookieSessionProps.getDebug()){
			refreshFlag = true;
		}
		
		if(htmlObjects.containsKey(tmpHTMLName) == false || refreshFlag == true)
		{
			lock.lock();
			
			if(htmlObjects.containsKey(tmpHTMLName) == false || refreshFlag == true)
			{
				try
				{
					String fName = basePath + tmpHTMLName.replace('.', File.separatorChar) + ".html";
					String htmlContent = readFileContent(new File(fName));
					htmlObjects.put(tmpHTMLName, htmlContent);
				}
				catch(Exception e)
				{
					;
				}
			}
			
			lock.unlock();
		}
		
		if(htmlObjects.containsKey(tmpHTMLName) == true)
		{
			htmlText = htmlObjects.get(tmpHTMLName);
		}
		else
		{
			throw new TzSystemException("error: can't find the specified HTML object \"" + htmlName + "\".");
		}
		
		return htmlText;
	}
}
