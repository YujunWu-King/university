package com.tranzvision.gd.util.imaScaling;

public class Msg {
	private String ImaUrl;
	private String name;
	private String nameWithPre;

	public Msg()
	{
	}

	public String getName()
	{
		return name;
	}

	public String getNameWithPre()
	{
		return nameWithPre;
	}

	public void setNameWithPre(String nameWithPre)
	{
		this.nameWithPre = nameWithPre;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImaUrl()
	{
		return ImaUrl;
	}

	public void setImaUrl(String imaUrl)
	{
		ImaUrl = imaUrl;
	}
}
