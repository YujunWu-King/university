package com.tranzvision.gd.TZPaymentBundle.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPaymentBundle.dao.PsTzPaymentAccountTMapper;
import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentAccountT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZPaymentBundle.service.TzPaymentAccountManageImpl")
public class TzPaymentAccountManageImpl extends FrameworkImpl
{

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsTzPaymentAccountTMapper psTzPaymentAccountTMapper;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	//新增账户操作
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("==执行增加操作tzUpdate()方法==");
		for(int i=0;i<actData.length;i++){
			System.out.println("actData:"+actData[i]);
		}
		
		//数据为空直接报异常
		if(actData==null||actData.length<0){
			errMsg[0]="1";
			errMsg[1]="账户数据不能为空!";
			return null;
		}
		
		//将页面传入数据，转成Map
		try{
			JacksonUtil jsonUtil=new JacksonUtil();
			Map<String,Object>paramsMap=new HashMap<String,Object>();
			jsonUtil.json2Map(actData[0]);
			paramsMap=jsonUtil.getMap();
			
			PsTzPaymentAccountT psTzPaymentAccountT=new PsTzPaymentAccountT();
	
			psTzPaymentAccountT.setTzJgId(tzLoginServiceImpl.getLoginedManagerOrgid(request));
			if(paramsMap.get("accountId")!=null){
				psTzPaymentAccountT.setTzAccountId(paramsMap.get("accountId").toString());
			}
			if(paramsMap.get("accountName")!=null){
				psTzPaymentAccountT.setTzAccountName(paramsMap.get("accountName").toString());
			}
			if(paramsMap.get("accountKey")!=null){
				psTzPaymentAccountT.setTzAccountKey(paramsMap.get("accountKey").toString());
			}
			if(paramsMap.get("accountState")!=null){
				psTzPaymentAccountT.setTzAccountState(paramsMap.get("accountState").toString());
			}
			if(paramsMap.get("accountDescribe")!=null){
				psTzPaymentAccountT.setTzAccountDescribe(paramsMap.get("accountDescribe").toString());
			}
			if(paramsMap.get("accountPlatform")!=null){
				psTzPaymentAccountT.setTzPlatformId(paramsMap.get("accountPlatform").toString());
			}
			//时间 操作人 放入数据库
			psTzPaymentAccountT.setRowLastmantDttm(new Date());
			psTzPaymentAccountT.setRowLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			//增加和编辑处理
			if(psTzPaymentAccountTMapper.selectByPrimaryKey(paramsMap.get("accountId").toString())!=null){
				
				psTzPaymentAccountTMapper.updateByPrimaryKeySelective(psTzPaymentAccountT);
			}
			else{
				psTzPaymentAccountT.setRowAddedDttm(new Date());
				psTzPaymentAccountT.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
				psTzPaymentAccountTMapper.insert(psTzPaymentAccountT);
			}
			Map<String,Object>returnMap=new HashMap<String,Object>();
			returnMap.put("result", "SUCCESS");
			return new JacksonUtil().Map2json(returnMap);
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0]="1";
			errMsg[1]=e.toString();
		}
		return null; 
	}


	//支付账户管理 列表显示
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		final String SQL="SELECT TZ_PLATFORM_NAME FROM PS_TZ_PAYMENT_PLATFORM_T WHERE TZ_PLATFORM_ID=?";
		// 返回值;
		System.out.println("==进入支付账户管理tzQueryList()方法==");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		returnMap.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_ACCOUNT_ID","TZ_ACCOUNT_NAME","TZ_ACCOUNT_KEY","TZ_ACCOUNT_STATE","TZ_ACCOUNT_DESCRIBE","TZ_PLATFORM_ID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("accountId", rowList[0]);
					mapList.put("accountName", rowList[1]);
					mapList.put("accountKey", rowList[2]);
					mapList.put("accountState", rowList[3]);
					mapList.put("accountDescribe", rowList[4]);
					//mapList.put("accountPlatform", rowList[5]);
					//将查出的支付平台的ID为条件，查出支付平台的Name
					String platformId=rowList[5];
					String platFormName=jdbcTemplate.queryForObject(SQL, new Object[]{platformId},"String");
					mapList.put("accountPlatform", platFormName);
					listData.add(mapList);
				}

				returnMap.replace("total", obj[0]);
				returnMap.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(returnMap) ;
	}


	//删除支付账户处理
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		
		System.out.println("==执行删除操作tzDelete()方法==");
		try{
			for(int i=0;i<actData.length;i++){
				System.out.println("actData:"+actData[i]);
				Map<String,Object>paramMap=new HashMap<String,Object>();
				JacksonUtil jsonUtil=new JacksonUtil();
				jsonUtil.json2Map(actData[i]);
				paramMap=jsonUtil.getMap();
				if(paramMap.get("accountId")!=null){
					psTzPaymentAccountTMapper.deleteByPrimaryKey(paramMap.get("accountId").toString());
				}
			}
			Map<String,Object>returnMap=new HashMap<String,Object>();
			returnMap.put("result", "SUCCESS");
			return new JacksonUtil().Map2json(returnMap);
		}
		catch(Exception e){
			e.printStackTrace();
			errMsg[0]="1";
			errMsg[1]=e.toString();
		}
		return null;
	}
	
	//用于获取 平台下拉框操作
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		//map 格式的JSON
		System.out.println("执行TzPaymentPlatformManageImpl的tzOther()");
		try{
			JacksonUtil jsonUtil=new JacksonUtil();
			Map<String,Object> returnMap=new HashMap<String,Object>();
			
			List<Map<String,Object>>resultList=new ArrayList<Map<String,Object>>();
			final String SQL="SELECT TZ_PLATFORM_ID  as platformId,TZ_PLATFORM_NAME as platformName FROM PS_TZ_PAYMENT_PLATFORM_T";
			resultList=jdbcTemplate.queryForList(SQL);
			returnMap.put("root", resultList);
			return jsonUtil.Map2json(returnMap);
		}
		catch(Exception e){
			errorMsg[0]="1";
			errorMsg[1]=e.toString();
			e.printStackTrace();
		}
		return null;
	}


	//查询支付账户  用于编辑的时候 加载信息
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		//final String SQL="SELECT TZ_PLATFORM_NAME FROM PS_TZ_PAYMENT_PLATFORM_T WHERE TZ_PLATFORM_ID=?";
		System.out.println("进入TzPaymentAccountManageImpl中的tzQuery()方法");
		System.out.println("strParams:"+strParams);
		try{
			Map<String,Object>returnMap=new HashMap<String,Object>();
			
			JacksonUtil jsonUtil=new JacksonUtil();
			Map<String,Object>paramsMap=new HashMap<String,Object>();
			jsonUtil.json2Map(strParams);
			paramsMap=jsonUtil.getMap();
	
			if(paramsMap.get("accountId")!=null){
				PsTzPaymentAccountT  psTzPaymentAccountT =new PsTzPaymentAccountT();
				psTzPaymentAccountT=psTzPaymentAccountTMapper.selectByPrimaryKey(paramsMap.get("accountId").toString());
				returnMap.put("accountId", paramsMap.get("accountId").toString());
				returnMap.put("accountName", psTzPaymentAccountT.getTzAccountName());
				returnMap.put("accountKey", psTzPaymentAccountT.getTzAccountKey());
				returnMap.put("accountDescribe", psTzPaymentAccountT.getTzAccountDescribe());
				returnMap.put("accountState", psTzPaymentAccountT.getTzAccountState());
				returnMap.put("accountPlatform", psTzPaymentAccountT.getTzPlatformId());
				Map<String,Object>map=new  HashMap<String,Object>();
				map.put("formData", returnMap);
				return jsonUtil.Map2json(map);
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0]="1";
			errMsg[1]=e.toString();
		}
		return null;
	}
	
	
}
