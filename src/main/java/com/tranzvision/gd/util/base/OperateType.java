package com.tranzvision.gd.util.base;

public enum OperateType {
	KJZY,ZCD,CDSM,ZTYY,BQ,ZJZY,TV,COMBOX,PROMPT,HARDCODE,LOGOUT,LOGIN,QG,QF,U,HTML,JHTML,EJSON,OTHER,CODE,VALIDATECODE;
	
	public static OperateType getOperateType(String operateType){ 
		//return valueOf(operateType.toUpperCase());
		
		OperateType ret_operateType = null;
		try{
			ret_operateType =  valueOf(operateType.toUpperCase());
		}catch(Exception e){
			ret_operateType = valueOf("OTHER");
		}

		return ret_operateType;  
		
	}  
}

