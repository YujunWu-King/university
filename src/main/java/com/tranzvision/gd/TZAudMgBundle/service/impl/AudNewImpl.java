package com.tranzvision.gd.TZAudMgBundle.service.impl;

import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudDefnTMapper;
import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudListTMapper;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudDefnT;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudListT;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudListTKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：听众管理相关类
 * @author 顾贤达
 * 2017-1-19
 * 
 * 修改：
 * 2017-3-21,从清华迁移并优化，张浪
 */
@Service("com.tranzvision.gd.TZAudMgBundle.service.impl.AudNewImpl")
public class AudNewImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAudDefnTMapper psTzAudDefnTMapper;
	@Autowired
	private PsTzAudListTMapper psTzAudListTMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	
	/* 查询听众管理列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_AUD_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REALNAME", "TZ_DXZT","TZ_MOBILE","TZ_EMAIL","TZ_LYDX_ID","TZ_AUD_ID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("audName", rowList[0]);
					mapList.put("audDxzt", rowList[1]);
					mapList.put("audMobile", rowList[2]);
					mapList.put("audMail", rowList[3]);
					mapList.put("dxID", rowList[4]);
					mapList.put("audID", rowList[5]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

	
	
	/* 获取听众详细信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strAudID = jacksonUtil.getString("audId");
			String strAudName = myURLDecoder(jacksonUtil.getString("audName"));
			String strAudStat = jacksonUtil.getString("audStat");
			String strAudType = jacksonUtil.getString("audType");
			String strAudTips = myURLDecoder(jacksonUtil.getString("audMS"));
			String strAudSql = myURLDecoder(jacksonUtil.getString("audSQL"));
			String strAudLY = jacksonUtil.getString("audLY");
			String straudDynamcisXML = myURLDecoder(jacksonUtil.getString("audDynamcisXML"));
			String straudDynamcisQuery = jacksonUtil.getString("audDynamcisQuery");

			
			System.out.print(strAudLY);
			
			
			
			if (strAudID != null && !"".equals(strAudID) ) {
	
				PsTzAudDefnT psTzAudDefnT=new PsTzAudDefnT();
			
				psTzAudDefnT.setTzAudId(strAudID);
				psTzAudDefnT.setTzAudNam(strAudName);
				psTzAudDefnT.setTzAudStat(strAudStat);
				psTzAudDefnT.setTzAudType(strAudType);
				psTzAudDefnT.setTzAudMs(strAudTips);
				psTzAudDefnT.setTzAudSql(strAudSql);
				psTzAudDefnT.setTzLxfsLy(strAudLY);
				psTzAudDefnT.setTzDynaXML(straudDynamcisXML);
				psTzAudDefnT.setTzDynaEntity(straudDynamcisQuery);
				
				
				returnJsonMap.put("audID", strAudID);
				returnJsonMap.put("audName", psTzAudDefnT.getTzAudNam());
				returnJsonMap.put("audStat", psTzAudDefnT.getTzAudStat());
				returnJsonMap.put("audType", psTzAudDefnT.getTzAudType());
				returnJsonMap.put("audMS", psTzAudDefnT.getTzAudMs());
				returnJsonMap.put("audSQL", psTzAudDefnT.getTzAudSql());
				returnJsonMap.put("audLY", psTzAudDefnT.getTzLxfsLy());
				returnJsonMap.put("audDynamcisXML", psTzAudDefnT.getTzDynaXML());
				returnJsonMap.put("audDynamcisQuery", psTzAudDefnT.getTzDynaEntity());

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取页面信息";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				//来源对象ID;
				String strDxID = jacksonUtil.getString("dxID");
				String strAudID = jacksonUtil.getString("audID");
				
				String comPageSql = "DELETE FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? and TZ_LYDX_ID=?";
				jdbcTemplate.update(comPageSql,new Object[]{strAudID,strDxID});
				//jdbcTemplate.execute("commit");
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/* 新增 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("audID", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return jacksonUtil.Map2json(rtnMap);
		}
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			//机构
			String strJgID = jacksonUtil.getString("audJG");
			
			String strAudName = myURLDecoder(jacksonUtil.getString("audName"));
			String strAudStat = jacksonUtil.getString("audStat");
			String strAudType = jacksonUtil.getString("audType");
			String strTips = myURLDecoder(jacksonUtil.getString("audMS"));
			String strSql = myURLDecoder(jacksonUtil.getString("audSQL"));
			String strLY = jacksonUtil.getString("audLY");
			String straudDynamcisXML = myURLDecoder(jacksonUtil.getString("audDynamcisXML"));
			String straudDynamcisQuery = jacksonUtil.getString("audDynamcisQuery");

			//生成听众ID
			int strAudID=getSeqNum.getSeqNum("PS_TZ_AUD_DEFN_T", "TZ_AUD_ID");
			String strAudId =	String.valueOf(strAudID);
			
			
			//查询听众是否存在;
			String isExistSql = "SELECT count(1) FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=? ";
			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strAudId }, "int");

			if (count == 0) {
				PsTzAudDefnT psTzAudDefnT=new PsTzAudDefnT();
				psTzAudDefnT.setTzAudId(strAudId);
				psTzAudDefnT.setTzJgId(strJgID);
				
				psTzAudDefnT.setTzAudNam(strAudName);
				psTzAudDefnT.setTzAudStat(strAudStat);
				psTzAudDefnT.setTzAudType(strAudType);
				psTzAudDefnT.setTzAudMs(strTips);
				psTzAudDefnT.setTzAudSql(strSql);
				psTzAudDefnT.setTzDynaXML(straudDynamcisXML);
				psTzAudDefnT.setTzDynaEntity(straudDynamcisQuery);
				System.out.println("听众来源"+strLY);
				
				
				psTzAudDefnT.setTzLxfsLy(strLY);
				
				int i = psTzAudDefnTMapper.insert(psTzAudDefnT);
				if(i > 0){
					rtnMap.replace("audID", strAudId);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}
				
				strRet=String.valueOf(strAudID);
				
			} else {
				errMsg[0] = "1";
				errMsg[1] = "听众ID已存在";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/* 修改 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				
				if ("FORM".equals(strFlag)) {

					String strJgID = (String) infoData.get("audJG");
					
					String strAudId = (String) infoData.get("audID");
					
					String strAudName = myURLDecoder((String) infoData.get("audName"));
					
					String strAudStat = (String) infoData.get("audStat");
					
					String strAudType = (String) infoData.get("audType");
					
					String strTips = myURLDecoder((String) infoData.get("audMS"));
					
					String strSql = myURLDecoder((String) infoData.get("audSQL"));
					
					String strLY = (String) infoData.get("audLY");

					String straudDynamcisXML = myURLDecoder((String) infoData.get("audDynamcisXML"));

					String straudDynamcisQuery = (String) infoData.get("audDynamcisQuery");

					

					// 是否已经存在;
					String comExistSql  = "SELECT 'Y'  AS Y  FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=? ";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strAudId }, "String");

					if (!"Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "ID为：" + strAudId + "的信息不存在。";
						return strRet;
					}

					PsTzAudDefnT psTzAudDefnT=new PsTzAudDefnT();
					psTzAudDefnT.setTzAudId(strAudId);
					psTzAudDefnT.setTzJgId(strJgID);
					psTzAudDefnT.setTzAudNam(strAudName);
					
					psTzAudDefnT.setTzAudStat(strAudStat);
					psTzAudDefnT.setTzAudType(strAudType);
					psTzAudDefnT.setTzAudMs(strTips);
					psTzAudDefnT.setTzAudSql(strSql);
					psTzAudDefnT.setTzLxfsLy(strLY);
					psTzAudDefnT.setTzDynaXML(straudDynamcisXML);
					psTzAudDefnT.setTzDynaEntity(straudDynamcisQuery);

					int i = psTzAudDefnTMapper.updateByPrimaryKeySelective(psTzAudDefnT);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}
				if ("GRID".equals(strFlag)) {
					strRet = this.tzUpdateGridInfo(infoData, errMsg);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	//grid 保存
	private String tzUpdateGridInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 听众编号;
			String strAudID = (String) infoData.get("audID");

			String strDxID = (String) infoData.get("dxID");
			String straudDxzt = (String) infoData.get("audDxzt");

			PsTzAudListT psTzAudListT=new PsTzAudListT();
			psTzAudListT.setTzDxzt(straudDxzt);
			
			String comPageSql = "UPDATE PS_TZ_AUD_LIST_T SET TZ_DXZT=? WHERE TZ_AUD_ID=? and TZ_LYDX_ID=?";
							
			jdbcTemplate.update(comPageSql,new Object[]{straudDxzt,strAudID,strDxID});
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	
	/* 执行SQL */
	@Override
	public String tzOther(String oprType, String actData, String[] errorMsg) {		
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData;
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			// 组件ID;
			String SaudSQL = jacksonUtil.getString("audSQL");
			String SaudID = jacksonUtil.getString("audID");
			
			List<Map<String, Object>> resultlist = null;
			resultlist = jdbcTemplate.queryForList(SaudSQL);

			if(resultlist == null){
				errorMsg[0] = "1";
				errorMsg[1] = "请检查SQL语句是否正确";
			}else{
				int count = 0;
				for (Map<String, Object> resultMap : resultlist) {
					if(count == 0){
						int keySize = resultMap.keySet().size();
						if(keySize != 1){
							errorMsg[0] = "1";
							errorMsg[1] = "SQL语句不合法，返回了"+keySize+"个值。";
							break;
						}else{
							String DeleteSql = "DELETE FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? and TZ_DXZT='A'";
							jdbcTemplate.update(DeleteSql,new Object[]{SaudID});
						}
					}
					String InsertID=StringUtils.strip(resultMap.values().toString(),"[]");

					PsTzAudListTKey psTzAudListTKey = new PsTzAudListTKey();
					psTzAudListTKey.setTzAudId(SaudID);
					psTzAudListTKey.setTzLxfsLy("ZCYH");
					psTzAudListTKey.setTzLydxId(InsertID);
					
					PsTzAudListT psTzAudListT = psTzAudListTMapper.selectByPrimaryKey(psTzAudListTKey);
					if(psTzAudListT == null){
						psTzAudListT = new PsTzAudListT();
						psTzAudListT.setTzAudId(SaudID);
						psTzAudListT.setTzLxfsLy("ZCYH");
						psTzAudListT.setTzLydxId(InsertID);
						psTzAudListT.setTzDxzt("A");
						psTzAudListT.setOprid(InsertID);
						
						psTzAudListTMapper.insert(psTzAudListT);
					}
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "错误："+e.toString();
		}

		return strRet;
	}

    /**
     * 返回Dynamics实体的静态听众
     * @return
     */
    public List<Map<String,Object>> getDynamicsStaicAudience(String Audiemce){
        String sql="select TZ_AUD_ID,TZ_AUD_NAM  from PS_TZ_AUD_DEFN_T where TZ_DYNA_ENTITY=? and TZ_LXFS_LY=? and TZ_AUD_TYPE=?";
        String LXFSLY="DYN";//转换值集合配置的TZ_AUD_LY  取Dynamics的转换值DYN
        String AUDTYPE="2";//转换值集合配置的TZ_AUD_TYPE  取静态听众的转换值2
        List<Map<String,Object>> retMap=new ArrayList<Map<String,Object>>();

        switch (Audiemce){
            //dynamics学生实体  转换值集合配置的1：tzms_stu_defn_t 2：tzms_tea_defn_t 3：lead
            case "tzms_stu_defn_t":
                Audiemce="1";
                break;
            //dynamics销售线索实体
            case "tzms_tea_defn_t":
                Audiemce="2";
                break;
            //dynamics教职工实体
            case "lead":
                Audiemce="3";
                break;
                default:
        }
		retMap= jdbcTemplate.queryForList(sql, new Object[]{Audiemce, LXFSLY, AUDTYPE});
        return retMap;
    }

	private String myURLDecoder(String str) {
		if("".equals(str)) {
			return "";
		}
		str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
		str = str.replaceAll("\\+", "%2B");
		try {
			str = URLDecoder.decode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//sql中两个单引号表示一个单引号
		str = str.replaceAll("'", "''");
		return str;
	}
}


