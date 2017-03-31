package com.tranzvision.gd.TZPaymentBundle.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPaymentBundle.dao.PsTzPaymentPlatformTMapper;
import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentPlatformT;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZPaymentBundle.service.TzPaymentPlatformManageImpl")
public class TzPaymentPlatformManageImpl extends FrameworkImpl
{

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsTzPaymentPlatformTMapper psTzPaymentPlatformTMapper;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//新增支付平台操作
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("==进入支付平台管理tzUpdate()方法==");
		for(int i=0;i<actData.length;i++){
			System.out.println("actData:"+actData[i]);
		}
		
		//数据为空直接报异常
		if(actData==null||actData.length<0){
			errMsg[0]="1";
			errMsg[1]="账户数据不能为空!";
			return null;
		}
		//时间转换：字符串转Date
		//SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		
		try{
			//将页面传入数据，转成Map
			JacksonUtil jsonUtil=new JacksonUtil();
			Map<String,Object>paramsMap=new HashMap<String,Object>();
			//更新只有一条数据：
			jsonUtil.json2Map(actData[0]);
			paramsMap=jsonUtil.getMap();
	
			PsTzPaymentPlatformT psTzPaymentPlatformT=new PsTzPaymentPlatformT();
	
			if(paramsMap.get("platformId")!=null){
				psTzPaymentPlatformT.setTzPlatformId(paramsMap.get("platformId").toString());
			}
			if(paramsMap.get("platformName")!=null){
				psTzPaymentPlatformT.setTzPlatformName(paramsMap.get("platformName").toString());
			}
			if(paramsMap.get("platformInterface")!=null){
				psTzPaymentPlatformT.setTzPaymentInterface(paramsMap.get("platformInterface").toString());
			}
			if(paramsMap.get("returnUrl")!=null){
				psTzPaymentPlatformT.setTzReturnUrl(paramsMap.get("returnUrl").toString());
			}
			if(paramsMap.get("dealClass")!=null){
				psTzPaymentPlatformT.setTzDealClass(paramsMap.get("dealClass").toString());
			}
			if(paramsMap.get("platformState")!=null){
				psTzPaymentPlatformT.setTzPlatformState(paramsMap.get("platformState").toString());
			}
			if(paramsMap.get("waitTime")!=null){
				psTzPaymentPlatformT.setTzWaitTime(Integer.valueOf(paramsMap.get("waitTime").toString()));
			}
			if(paramsMap.get("platformDescribe")!=null){
				psTzPaymentPlatformT.setTzPlatformDescribe(paramsMap.get("platformDescribe").toString());
			}
			//时间 操作人 放入数据库
			psTzPaymentPlatformT.setRowLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			psTzPaymentPlatformT.setRowLastmantDttm(new Date());
			//增加和编辑处理
			if(psTzPaymentPlatformTMapper.selectByPrimaryKey(paramsMap.get("platformId").toString())!=null){
				//跟新操作的时候 只需要加入 跟新人和跟新时间
				psTzPaymentPlatformTMapper.updateByPrimaryKeySelective(psTzPaymentPlatformT);
			}
			else{
				
				//第一次插入支付平台数据的时候，加入增加人和增加时间字段
				psTzPaymentPlatformT.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
				psTzPaymentPlatformT.setRowAddedDttm(new Date());
				psTzPaymentPlatformTMapper.insert(psTzPaymentPlatformT);
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


	//支付平台 列表显示
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		System.out.println("==进入支付平台管理tzQueryList()方法==");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		returnMap.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_PLATFORM_ID","TZ_PLATFORM_NAME","TZ_PLATFORM_STATE"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("platformId", rowList[0]);
					mapList.put("platformName", rowList[1]);
					mapList.put("platformState", rowList[2]);
					listData.add(mapList);
				}

				returnMap.replace("total", obj[0]);
				returnMap.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0]="1";
			errorMsg[1]=e.toString();
		}
		
		return jacksonUtil.Map2json(returnMap) ;
	}


	//支付平台账号删除
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		
		System.out.println("==进入支付平台管理tzDelete()方法==");
		try{
			for(int i=0;i<actData.length;i++){
				System.out.println("actData:"+actData[i]);
				Map<String,Object>paramMap=new HashMap<String,Object>();
				JacksonUtil jsonUtil=new JacksonUtil();
				jsonUtil.json2Map(actData[i]);
				paramMap=jsonUtil.getMap();
				if(paramMap.get("platformId")!=null){
					psTzPaymentPlatformTMapper.deleteByPrimaryKey(paramMap.get("platformId").toString());
				}
			}
			Map<String,Object>returnMap=new HashMap<String,Object>();
			returnMap.put("result", "SUCCESS");
			return new JacksonUtil().Map2json(returnMap);
		}
		catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return null;
	}
	//查询支付平台  用于编辑的时候 加载信息
		@Override
		public String tzQuery(String strParams, String[] errMsg) {
			System.out.println("进入TzPaymentPlatformManageImpl中的tzQuery()方法");
			System.out.println("strParams:"+strParams);
			Map<String,Object>returnMap=new HashMap<String,Object>();
			
			JacksonUtil jsonUtil=new JacksonUtil();
			Map<String,Object>paramsMap=new HashMap<String,Object>();
			jsonUtil.json2Map(strParams);
			paramsMap=jsonUtil.getMap();

			if(paramsMap.get("platformId")!=null){
				PsTzPaymentPlatformT  psTzPaymentPlatformT =new PsTzPaymentPlatformT();
				psTzPaymentPlatformT=psTzPaymentPlatformTMapper.selectByPrimaryKey(paramsMap.get("platformId").toString());
				returnMap.put("platformId", paramsMap.get("platformId").toString());
				returnMap.put("platformName", psTzPaymentPlatformT.getTzPlatformName());
				returnMap.put("platformInterface", psTzPaymentPlatformT.getTzPaymentInterface());
				returnMap.put("returnUrl", psTzPaymentPlatformT.getTzReturnUrl());
				returnMap.put("dealClass", psTzPaymentPlatformT.getTzDealClass());
				returnMap.put("platformState", psTzPaymentPlatformT.getTzPlatformState());
				returnMap.put("waitTime", psTzPaymentPlatformT.getTzWaitTime());
				returnMap.put("platformDescribe", psTzPaymentPlatformT.getTzPlatformDescribe());
				
				Map<String,Object>map=new  HashMap<String,Object>();
				map.put("formData", returnMap);
				System.out.println("编辑fromData:"+jsonUtil.Map2json(map));
				return jsonUtil.Map2json(map);
			}
			return null;
		}
}
