package com.tranzvision.gd.TZChargeManage.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCostRelatedAEBundle.dao.TzJfPlanTMapper;
import com.tranzvision.gd.TZCostRelatedAEBundle.dao.TzKsJxjTMapper;
import com.tranzvision.gd.TZCreateClueBundle.service.impl.TzCreateClueServiceImpl;

import com.tranzvision.gd.WorkflowActionsBundle.service.impl.WorkFlowPublicImpl;
import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/*import org.json.JSONArray;
import org.json.JSONObject;*/

/**
 * @Auther: ZY
 * @Date: 2019/4/24 10:36
 * @Description: 招生项目所属，录取与缴费业务类
 */
@Service("com.tranzvision.gd.TZChargeManage.service.impl.AdmissionAndPayImpl")
public class AdmissionAndPayImpl extends FrameworkImpl {
    @Autowired
    private SqlQuery sqlQuery;
    @Autowired
    private TzLoginServiceImpl tzLoginServiceImpl;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private GetSeqNum getSeqNum;
    @Autowired
    private FliterForm fliterForm;
    @Autowired
	private GetHardCodePoint getHardCodePoint;
    @Autowired
    private WorkFlowPublicImpl workFlowPublic;
    @Autowired
    private TzJfPlanTMapper tzJfPlanTMapper;
    @Autowired
    private TzKsJxjTMapper tzKsJxjTMapper;
    @Autowired
    private TzCreateClueServiceImpl tzCreateClueServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
    /* 查询录取与缴费列表 */
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
            String[][] orderByArr = new String[][] {new String[]{"TZ_JFZT","ASC"}};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_CLASS_ID", "TZ_CLASS_NAME", "OPRID", "TZ_APP_INS_ID", "TZ_MSH_ID", "TZ_PRJ_ID",
                    "TZ_REALNAME", "TZ_COMPANY_NAME", "TZ_MIAN_REALNAME","TZ_LQ_RESULT","tzms_class_name",
                    "tzms_stu_xjzt", "CLASS_MASTER", "TZ_SYNC_EDU", "ACCEPTANCE_LETTER","KP_COUNT","TZ_JFZT","TZ_FS_DT","TZ_EM_ZHUTI","FILE_NAME",
                    "TZ_COMMENT1","TZ_JXJ_NAME","TZ_TUITION_STANDARD"};

            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

            if (obj != null) {
                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                for (int i = 0; i < list.size(); i++) {
                    String[] rowList = list.get(i);
                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("classID", rowList[0]);
                    mapList.put("classAppName", rowList[1]);
                    mapList.put("oprid", rowList[2]);
                    mapList.put("appInsId", rowList[3]);
                    mapList.put("mshId", rowList[4]);
                    mapList.put("proId", rowList[5]);
                    mapList.put("name", rowList[6]);
                    mapList.put("companyName", rowList[7]);
                    mapList.put("responsible", rowList[8]);
                    mapList.put("lqResult", rowList[9]);
                    mapList.put("className", rowList[10]);
                    mapList.put("stuStatus", rowList[11]);
                    mapList.put("classTea", rowList[12]);
                    mapList.put("syncEdu", rowList[13]);
                    mapList.put("acceptanceLetter", rowList[14]);
                    mapList.put("kpCount", rowList[15]);
                    //查询dynamics关联信息
                    //mapList.putAll(getDynamicsInfo(rowList[3]));
                    //查询学费标准,奖学金金额,退费金额,应收金额，已缴金额，到账总计
                    mapList.putAll(getAmount(rowList[3]));
                    mapList.put("jfStat", rowList[16]);
                    //待定主要负责人，录取通知函
                    //邮件原模板招生通知（1620）发送时间
                    mapList.put("fsDt", rowList[17]);
                    //邮件主题
                    mapList.put("emZhuTi", rowList[18]);
                    //录取通知书确认函名称
                    mapList.put("fileName", rowList[19]);
                    //职务
                    mapList.put("zw", rowList[20]);
                    //奖学金类别名称
                    mapList.put("jxjName", rowList[21]);
                    //学费标准
                    mapList.put("TZ_JF_BZ_JE",rowList[22]);
                    listData.add(mapList);
                }
                mapRet.replace("total", obj[0]);
                mapRet.replace("root", listData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jacksonUtil.Map2json(mapRet);
    }


    @Override
    public String tzOther(String oprType, String strParams, String[] errorMsg) {
        String strRet = "";
        try{
            //当前登录的机构;
            //String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
            if("queryScholarship".equals(oprType)) {
                //查询奖学金相关
                strRet = this.queryScholarship(strParams, errorMsg);
            }
            /*if("queryScholarshipList".equals(oprType)) {
                //查询奖学金List
                strRet = this.queryScholarshipList(strParams, errorMsg);
            }*/
            if("queryAmount".equals(oprType)) {
                //查询缴费标准金额
                strRet = this.queryAmount(strParams, errorMsg);
            }
            if("savePayPlan".equals(oprType)) {
                //批量保存缴费计划
                strRet = this.savePayPlan(strParams, errorMsg);
            }
            if("saveScholarship".equals(oprType)) {
                //批量设置奖学金
                strRet = this.saveScholarship(strParams, errorMsg);
            }
            if("queryLX".equals(oprType)) {
                //查询是否存在立项项目
                strRet = this.queryLX(strParams, errorMsg);
            }
            if("syncLX".equals(oprType)) {
                //同步立项项目
                strRet = this.syncLX(strParams, errorMsg);
            }
            if("syncEdu".equals(oprType)) {
                //同步学生信息
                strRet = this.syncEdu(strParams, errorMsg);
            }
            if("cancelSyncStu".equals(oprType)) {
            	//撤销同步学生信息
            	strRet = this.cancelSyncStu(strParams, errorMsg);
            }
            if("changeStuStatus".equals(oprType)) {
                //变更学籍状态
                strRet = this.changeStuStatus(strParams, errorMsg);
            }
            if("setResund".equals(oprType)) {
                //设置允许退费
                strRet = this.setResund(strParams, errorMsg);
            }
            if("queryRefund".equals(oprType)){
                //查询设置退费list
                strRet = this.queryRefund(strParams, errorMsg);
            }
            if("saveRefund".equals(oprType)){
                //查询设置退费list
                strRet = this.saveRefund(strParams, errorMsg);
            }
            if("getNewRefundWindows".equals(oprType)){
                //新建退学退费表单
                strRet = this.getNewRefundWindows(strParams, errorMsg);
            }
            if("previewNotice".equals(oprType)){
                //新建退学退费表单
                strRet = this.previewNotice(strParams, errorMsg);
            }
            if("export".equals(oprType)){
                //导出
                strRet = this.export(strParams, errorMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return strRet;
    }
    /**
     * 导出缴费
     * @param strParams
     * @param errorMsg
     * @return
     */
    
    private String export(String strParams, String[] errorMsg) {
    	Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
        	jacksonUtil.json2Map(strParams);
        	List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
        	String classID="";
        	String appInsId ="";
        	String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request).toLowerCase();
        	if(list != null && list.size() > 0){
        		String downloadPath = getSysHardCodeVal.getDownloadPath();
				String expDirPath = downloadPath + "/" + orgid + "/admissionPayExcel";
				String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
				System.out.println("expDirPath"+expDirPath);
				System.out.println("absexpDirPath"+absexpDirPath);
				// 表头
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				dataCellKeys.add(new String[]{ "name", "姓名" });
				dataCellKeys.add(new String[]{ "mshId", "报名号" });
				dataCellKeys.add(new String[]{ "companyName", "公司" });
				dataCellKeys.add(new String[]{ "responsible", "主要负责人" });
				dataCellKeys.add(new String[]{ "syncEdu", "同步教务" });
				
				dataCellKeys.add(new String[]{ "className", "班级" });
				dataCellKeys.add(new String[]{ "classTea", "班主任" });
				dataCellKeys.add(new String[]{ "lqResult", "录取结果" });
				dataCellKeys.add(new String[]{ "jfStat", "缴费状态" });
				dataCellKeys.add(new String[]{ "stuStatus", "学籍状态" });
				
				
				dataCellKeys.add(new String[]{ "TZ_JF_BZ_JE", "学费标准" });
				dataCellKeys.add(new String[]{ "TZ_JF_TZ_JE", "学费调整" });
				dataCellKeys.add(new String[]{ "TZ_JXJ_JE", "奖学金金额" });
				dataCellKeys.add(new String[]{ "TZ_TF_JIE", "退费金额" });
				dataCellKeys.add(new String[]{ "TZ_JF_BQYS", "应收金额" });
				
				dataCellKeys.add(new String[]{ "TZ_JF_BQSS", "已缴金额" });
				dataCellKeys.add(new String[]{ "TZ_JF_BQWS", "未缴金额" });
				dataCellKeys.add(new String[]{ "payPlanStr", "缴费计划" });
				dataCellKeys.add(new String[]{ "kpCount", "开票状态信息" });
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				for(int num_1 = 0; num_1 < list.size(); num_1 ++){
					Map<String, Object> map = list.get(num_1);
		            classID = (String)map.get("classID");
		            appInsId = (String)map.get("appInsId");
		            Map<String, Object> mapDataex = new HashMap<String, Object>();
					Map<String, Object> admissPayMap = sqlQuery.queryForMap(
							"SELECT TZ_MSH_ID,TZ_REALNAME,TZ_COMPANY_NAME,TZ_MIAN_REALNAME,TZ_LQ_RESULT,tzms_class_name,tzms_stu_xjzt,CLASS_MASTER,TZ_SYNC_EDU,KP_COUNT,TZ_JFZT from TZ_AD_PAY_VW where TZ_APP_INS_ID=?",
							new Object[] {appInsId});
		            if (admissPayMap != null) {
						mapDataex.put("name", admissPayMap.get("TZ_REALNAME") == null ? ""
								: admissPayMap.get("TZ_REALNAME").toString());
						mapDataex.put("mshId", admissPayMap.get("TZ_MSH_ID") == null ? ""
								: admissPayMap.get("TZ_MSH_ID").toString());
						mapDataex.put("companyName",admissPayMap.get("TZ_COMPANY_NAME") == null ? ""
								: admissPayMap.get("TZ_COMPANY_NAME").toString());
						mapDataex.put("responsible", admissPayMap.get("TZ_MIAN_REALNAME") == null ? ""
								: admissPayMap.get("TZ_MIAN_REALNAME").toString());
						mapDataex.put("syncEdu", admissPayMap.get("TZ_SYNC_EDU") == null ? ""
								: admissPayMap.get("TZ_SYNC_EDU").toString());
						
						mapDataex.put("className", admissPayMap.get("tzms_class_name") == null ? ""
								: admissPayMap.get("tzms_class_name").toString());
						mapDataex.put("classTea",admissPayMap.get("CLASS_MASTER") == null ? ""
								: admissPayMap.get("CLASS_MASTER").toString());
						mapDataex.put("lqResult", admissPayMap.get("TZ_LQ_RESULT") == null ? ""
								: admissPayMap.get("TZ_LQ_RESULT").toString());
						String TZ_JFZT=admissPayMap.get("TZ_JFZT") == null ? ""
								: admissPayMap.get("TZ_JFZT").toString();
						String TZ_JFZT_desc="";
						if("1".equals(TZ_JFZT)){
							TZ_JFZT_desc="部分缴费";
						}else if("2".equals(TZ_JFZT)){
							TZ_JFZT_desc="已缴费";
						}else{
							TZ_JFZT_desc="未缴费";
						}
						mapDataex.put("jfStat",TZ_JFZT_desc);
						String xjzt = admissPayMap.get("tzms_stu_xjzt") == null ? "" : admissPayMap.get("tzms_stu_xjzt").toString();
						String xjztDesc = sqlQuery.queryForObject(
								"select TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZMS_XJZT' AND TZ_ZHZ_ID=?",
								new Object[] { xjzt }, "String");
						mapDataex.put("stuStatus",xjztDesc );
						
						mapDataex.put("kpCount",admissPayMap.get("KP_COUNT") == null ? ""
								: Integer.parseInt(admissPayMap.get("KP_COUNT").toString())>0?"已开票":"未开票");
						mapDataex.putAll(getAmount(appInsId));
						
					}
					dataList.add(mapDataex);
				}
				/* 将文件上传之前，先重命名该文件 */
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String sDttm = datetimeFormate.format(dt);
				String strUseFileName = "AdmissionAndPay"+sDttm + "_" + classID + "." + "xlsx"; 
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = request.getContextPath() + excelHandle.getExportExcelPath();
					returnJsonMap.replace("formData", urlExcel);
				}
			}
        	
        	
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}


	//预览录取通知书
    private String previewNotice(String strParams, String[] errorMsg) {
    	Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String appInsId = jacksonUtil.getString("appInsId");
			String serv = "https://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
			
			String sql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?";
			String OPRID = sqlQuery.queryForObject(sql, new Object[] { appInsId }, "String");
			String parm = OPRID + "-" + appInsId;
		
			sql = "SELECT TZ_MD5_ID FROM PS_TZ_PARA_MD5_TBL WHERE TZ_PARA=? and TZ_TYPE=?";
			String md5 = sqlQuery.queryForObject(sql, new Object[] { parm, "0001" }, "String");
			
			System.out.println("================md5:"+md5);
			//有就读取 没有就新增
			if (md5 == null || "".equals(md5)) {
				md5 = Sha3DesMD5.md5(parm);
				sql = "insert into PS_TZ_PARA_MD5_TBL values (?,?,?,null)";
				sqlQuery.update(sql, new Object[] { md5, parm, "0001" });
			}
			String ret = serv + "/dispatcher?classid=viewLetter&TZ_APP_INS_ID=" + md5;
			returnJsonMap.replace("formData", ret);
			return jacksonUtil.Map2json(returnJsonMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "系统异常，请联系管理员！";
	}


	/**
     * 发起退费
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String saveRefund(String strParams, String[] errorMsg) {
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String refund = "";
        if(jacksonUtil.containsKey("refund")) {
            refund = jacksonUtil.getString("refund");
        }
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(refund);
        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            JSONObject refundJSONObject = JSONObject.fromObject(appInsIdJSONArray.get(i));
            /**这里调用发起退费流程Begin**/

            /**这里调用发起退费流程End**/

        }
        return jacksonUtil.Map2json(resultMap);
    }

    /**
     * ZY 新建申请退费工作流
     * @param strParams
     * @param errMsg
     * @return
     */
    public String getNewRefundWindows(String strParams, String[] errMsg) {
        // 返回值;
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("formData", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            String appInsId = jacksonUtil.getString("appInsId");
            String wfcldn_tid = "";
            //1.查询工作流配置模板表
            String queryWfSql="select top 1 tzms_wfcldn_tid from tzms_wfcldn_tBase where tzms_wfclname like '%退学退费%'";
            wfcldn_tid=sqlQuery.queryForObject(queryWfSql, new Object[] {}, "String");
            String queryUserInfo = "SELECT TOP 1 A.tzms_edp_wrk_tId AS tzms_back_people, A.tzms_name AS tzms_back_peoplename, B.tzms_stu_defn_tId AS tzms_people_name, B.tzms_name AS tzms_people_namename , CASE  WHEN B.tzms_dz_addr IS NOT NULL THEN B.tzms_dz_addr ELSE A.tzms_addres_info END AS tzms_contenct_street , CASE  WHEN B.tzms_phone IS NOT NULL THEN B.tzms_phone ELSE A.tzms_mobile END AS tzms_phone, A.tzms_recruit_name AS tzms_belong_project , CASE A.tzms_gender WHEN '0' THEN '男' WHEN '1' THEN '女' ELSE '' END AS tzms_stusextext, A.tzms_postal AS tzms_youbian_num, B.tzms_stu_id AS tzms_schoolnum, C.tzms_cls_defn_tid AS tzms_class_name, C.tzms_class_name AS tzms_class_namename FROM tzms_edp_wrk_t A LEFT JOIN tzms_stu_defn_t B ON A.tzms_dynamcis_stuid = B.tzms_stu_defn_tid LEFT JOIN tzms_cls_defn_t C ON B.tzms_stu_xjbj = C.tzms_cls_defn_tid WHERE A.tzms_app_ins_id = ?";
            Map<String,Object> parameterMap = sqlQuery.queryForMap(queryUserInfo,new Object[]{appInsId});
            String url = workFlowPublic.getNewWflinsWindowsURLForParameter(wfcldn_tid,parameterMap);

            returnJsonMap.put("formData",url);
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return jacksonUtil.Map2json(returnJsonMap);
    }

    /**
     * 批量发起退费
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String queryRefund(String strParams, String[] errorMsg) {
        List<Map<String,Object>> listData  = new ArrayList<Map<String,Object>>();
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String appInsIds = "";
        if(jacksonUtil.containsKey("appInsIds")) {
            appInsIds = jacksonUtil.getString("appInsIds");
        }
        String querySql = "SELECT A.TZ_APP_INS_ID,C.tzms_name,C.tzms_gender, C.tzms_stu_id, D.tzms_class_name, '' reason, '' payee, '' openingBank, '' branchBank, '' joinLine, '' num, 0 amount, '' dept, '' kp FROM PS_TZ_FORM_WRK_T A LEFT JOIN TZ_KSHSTU_GX_T B ON A.TZ_APP_INS_ID = B.TZ_APP_INS_ID LEFT JOIN tzms_stu_defn_t C ON B.TZ_STU_BH = C.tzms_stu_defn_tid LEFT JOIN tzms_cls_defn_t D ON C.tzms_stu_xjbj = D.tzms_cls_defn_tId WHERE A.TZ_APP_INS_ID = ?";
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new JSONArray(appInsIds);
        //查询报名表相关用户信息，用于同步教务
        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            String appInsId = String.valueOf(appInsIdJSONArray.get(i));
            Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{appInsId});
            if(map!=null){
                listData.add(map);
            }
        }
        resultMap.put("listData",listData);
        return jacksonUtil.Map2json(resultMap);
    }


    /**
     * 批量设置允许退费
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String setResund(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String appInsIds = "";
        if(jacksonUtil.containsKey("appInsIds")) {
            appInsIds = jacksonUtil.getString("appInsIds");
        }
        String updateSql = "UPDATE PS_TZ_FORM_WRK_T SET TZ_IS_REFUND = 'Y' WHERE TZ_APP_INS_ID = ?";
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new JSONArray(appInsIds);
        //查询报名表相关用户信息，用于同步教务
        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            String appInsId = String.valueOf(appInsIdJSONArray.get(i));
            sqlQuery.update(updateSql,new Object[]{appInsId});
        }
        return jacksonUtil.Map2json(resultMap);
    }


    /**
     * 变更学籍状态 针对存在教务的学生
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String changeStuStatus(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String appInsIds = "";
        if(jacksonUtil.containsKey("appInsIds")) {
            appInsIds = jacksonUtil.getString("appInsIds");
        }
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        String status = "";
        if(jacksonUtil.containsKey("status")) {
            status = jacksonUtil.getString("status");
        }
        String updateSql = "UPDATE tzms_stu_defn_t SET tzms_stu_xjzt = ? WHERE convert(varchar(36),tzms_stu_defn_tid) = (SELECT TZ_STU_BH FROM TZ_KSHSTU_GX_T WHERE TZ_APP_INS_ID = ?)";
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new JSONArray(appInsIds);
        //查询报名表相关用户信息，用于同步教务
        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            String appInsId = String.valueOf(appInsIdJSONArray.get(i));
            sqlQuery.update(updateSql,new Object[]{status,appInsId});
        }
        return jacksonUtil.Map2json(resultMap);
    }
    
    
    /**
     * 撤销同步教务
     * 撤销同步，如果该考生对应的招生班级的立项号的状态是已开班，或者这个考生的报名表编号在TZ_XYXFFT_TBL中存在，则不允许撤销同步，
     * 分别提示：当前班级已开班，不允许撤销同步，或者提示当前考生已经开始摊销，不允许撤销同步
     */
    public String cancelSyncStu(String strParams, String[] errorMsg) {
    	 Map<String, Object> resultMap = new HashMap<>();
         JacksonUtil jacksonUtil = new JacksonUtil();
         jacksonUtil.json2Map(strParams);
         String appInsIds = null;
         try {
			 if(jacksonUtil.containsKey("appInsIds")) {
				 appInsIds = (String) jacksonUtil.getString("appInsIds");
			 }
			 String classID = "";
			 if(jacksonUtil.containsKey("classID")) {
			     classID = jacksonUtil.getString("classID");
			 }
			 System.out.println("classID:" + classID);
			 List<Map<String, Object>> failCancelStu = new ArrayList<Map<String, Object>>();
			 String[] arr = {};
			 if(StringUtils.isNotEmpty(appInsIds)) {
				 arr = appInsIds.split(",");
			 }
			 if(arr.length > 0) {
				 for(String appInsId : arr) {
					//查询是否已经同步
					String sql = "SELECT TZ_SYNC_EDU FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
					String TZ_SYNC_EDU = sqlQuery.queryForObject(sql, new Object[] {appInsId}, "String");
					if("未同步".equals(TZ_SYNC_EDU)) {
						continue;
					}
					 
					sql = "SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
					String TZ_CLASS_ID = sqlQuery.queryForObject(sql, new Object[] {appInsId}, "String");
					System.out.println("TZ_CLASS_ID:" + TZ_CLASS_ID);
					sql = "SELECT TZ_LXBH FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
			        String TZ_LXBH = sqlQuery.queryForObject(sql,new Object[]{TZ_CLASS_ID},"String");
			        
			        System.out.println("TZ_LXBH:" + TZ_LXBH);
			        sql = "select tzms_kbzt from tzms_lxxmb_def_t where tzms_item_num = ?";
					String tzms_kbzt = sqlQuery.queryForObject(sql, new Object[] {TZ_LXBH}, "String");
					System.out.println("tzms_kbzt:" + tzms_kbzt);
					
					sql = "select tzms_dynamcis_stuid from tzms_edp_wrk_t where tzms_app_ins_id = ?";
					String tzms_stu_defn_tid =  sqlQuery.queryForObject(sql, new Object[] {appInsId}, "String");
					System.out.println("tzms_stu_defn_tid:" + tzms_stu_defn_tid);
					
					sql = "select tzms_name from tzms_stu_defn_tbase where tzms_stu_defn_tid = ?";
					String tzms_name = sqlQuery.queryForObject(sql, new Object[] {tzms_stu_defn_tid}, "String");
					if("2".equals(tzms_kbzt)) {
						//errorMsg[0] = "1";
						//errorMsg[1] = "当前班级已开班，不允许撤销同步";
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", tzms_name);
						map.put("msg", "当前班级已开班，不允许撤销同步");
						failCancelStu.add(map);
						break;
					}
					
					sql = "select count(*) from TZ_XYXFFT_TBL where TZ_APP_INS_ID = ?";
					int count = sqlQuery.queryForObject(sql, new Object[] {appInsId}, "int");
					if(count != 0) {
						//errorMsg[0] = "1";
						//errorMsg[1] = "当前考生已经开始摊销，不允许撤销同步";
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", tzms_name);
						map.put("msg", "当前班级已开班，不允许撤销同步");
						failCancelStu.add(map);
						break;
					}
					
					//开始撤销教务
					String updateSql = "delete from TZ_KSHSTU_GX_T where TZ_APP_INS_ID = ?";
					sqlQuery.update(updateSql, new Object[] {appInsId});
					
					//并更新DYNAMIC报名表实体中的学生ID。
					updateSql = "UPDATE tzms_edp_wrk_t SET tzms_dynamcis_stuid = null  WHERE tzms_app_ins_id = ?";
					sqlQuery.update(updateSql, new Object[] {appInsId});

					updateSql = "UPDATE PS_TZ_FORM_WRK_T SET TZ_SYNC_EDU='未同步' WHERE TZ_APP_INS_ID = ?";
					sqlQuery.update(updateSql, new Object[] {appInsId});
					
					//删除学生
					deleteStu(tzms_stu_defn_tid);
					
					//根据招生班级查所属项目分类
			        String queryPrjSql  = "SELECT C.TZ_DYPRJ_TYPE_ID,C.TZ_PRJ_TYPE_NAME,A.TZ_CLASS_NAME,A.TZ_SCHOOL_YEAR FROM PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T B,PS_TZ_PRJ_TYPE_T C WHERE A.TZ_PRJ_ID = B.TZ_PRJ_ID AND B.TZ_PRJ_TYPE_ID = C.TZ_PRJ_TYPE_ID AND A.TZ_CLASS_ID = ?";
			        Map<String, Object> proMap = sqlQuery.queryForMap(queryPrjSql, new Object[] {classID});
			        String TZ_PRJ_TYPE_NAME = "";
			        String TZ_CLASS_NAME = "";
			        if(proMap != null) {
			        	TZ_PRJ_TYPE_NAME = proMap.get("TZ_PRJ_TYPE_NAME") == null ? "":proMap.get("TZ_PRJ_TYPE_NAME").toString();
			        	TZ_CLASS_NAME = proMap.get("TZ_CLASS_NAME") == null ? "":proMap.get("TZ_CLASS_NAME").toString();
			        }

					String content = TZ_PRJ_TYPE_NAME + "项目，" + tzms_name + "学生，被招生管理员撤回了同步到教务的数据。该学生在教务中的一切数据都被删除。";
					tzCreateClueServiceImpl.sendEmail("TZ_CANCEL_STU", "招生管理员撤回学生教务数据通知", content);
				}
			 }
			 resultMap.put("stu", failCancelStu);
			 resultMap.put("flag", "success");
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "撤销失败，" + e.getMessage();
			resultMap.put("flag", "fail");
		}
    	return jacksonUtil.Map2json(resultMap);
    }

    /**
     * 删除学生信息
     * @param tzms_stu_defn_tid
     */
    public void deleteStu(String tzms_stu_defn_tid) {
    	String deleteSql = "";
    	if(StringUtils.isNotEmpty(tzms_stu_defn_tid)) {
    		deleteSql = "delete from tzms_stu_phone_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_email_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_addr_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_osns_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_cer_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_tutor_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stuzt_his_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_edu_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_work_tbase where tzms_psn_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stu_family_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		deleteSql = "delete from tzms_stupos_his_tbase where tzms_person_id = ?";
    		sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    		//删除学生信息
    		deleteSql = "delete from tzms_stu_defn_tbase where tzms_stu_defn_tid = ?";
			sqlQuery.update(deleteSql, new Object[] {tzms_stu_defn_tid});
    	}
    }
    
    /**
     * 同步教务 调用张浪接口完成
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String syncEdu(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String appInsId = "";
        if(jacksonUtil.containsKey("appInsId")) {
            appInsId = jacksonUtil.getString("appInsId");
        }
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }

        //查询是否已经同步
        String sql = "SELECT TZ_SYNC_EDU FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
        String TZ_SYNC_EDU = sqlQuery.queryForObject(sql, new Object[] {appInsId}, "String");
        if("已同步".equals(TZ_SYNC_EDU)) {
        	return null;
        }

        //录取结果
        sql = "SELECT ISNULL(A.TZ_LQ_RESULT,'') TZ_LQ_RESULT FROM PS_TZ_FORM_WRK_T B LEFT JOIN TZ_MS_LQJG_T A ON A.TZ_BMB_ID = B.TZ_MSH_ID WHERE B.TZ_APP_INS_ID=?";
        String TZ_LQ_RESULT = sqlQuery.queryForObject(sql, new Object[] {appInsId}, "String");

        TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
        String dynURL = "";
		String dynUserName = "";
		String dynUserPswd = "";
		String domain = "";
		try {
			dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
			dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
			dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
			domain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		boolean flag = false;
		String result = "";
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		//模拟登陆
		tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);

        String querySql = "SELECT TZ_LXBH FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
        String TZ_LXBH = sqlQuery.queryForObject(querySql,new Object[]{classID},"String");
        TZ_LXBH = TZ_LXBH == null ? "":TZ_LXBH;

        //根据招生班级查所属项目分类
        String queryPrjSql  = "SELECT C.TZ_DYPRJ_TYPE_ID,C.TZ_PRJ_TYPE_NAME,A.TZ_CLASS_NAME,A.TZ_SCHOOL_YEAR FROM PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T B,PS_TZ_PRJ_TYPE_T C WHERE A.TZ_PRJ_ID = B.TZ_PRJ_ID AND B.TZ_PRJ_TYPE_ID = C.TZ_PRJ_TYPE_ID AND A.TZ_CLASS_ID = ?";
        Map<String, Object> proMap = sqlQuery.queryForMap(queryPrjSql, new Object[] {classID});
        String TZ_DYPRJ_TYPE_ID = "";
        String TZ_PRJ_TYPE_NAME = "";
        String TZ_CLASS_NAME = "";
        String TZ_SCHOOL_YEAR = "";
        if(proMap != null) {
        	TZ_DYPRJ_TYPE_ID = proMap.get("TZ_DYPRJ_TYPE_ID") == null ? "":proMap.get("TZ_DYPRJ_TYPE_ID").toString();
        	TZ_PRJ_TYPE_NAME = proMap.get("TZ_PRJ_TYPE_NAME") == null ? "":proMap.get("TZ_PRJ_TYPE_NAME").toString();
        	TZ_CLASS_NAME = proMap.get("TZ_CLASS_NAME") == null ? "":proMap.get("TZ_CLASS_NAME").toString();
        	TZ_SCHOOL_YEAR = proMap.get("TZ_SCHOOL_YEAR") == null ? "":proMap.get("TZ_SCHOOL_YEAR").toString();
        }

        /**
         * 如果该招生班级的项目类型是EE公开课，那么需要先查询一下这个招生班级对应的立项号是否已经有对应的教务班级
         * （dynamic教务班级 tzms_cls_defn_t_tzms_lxxmb_def_tBase），如果没有，那么需要创建一个dynamics班级，班级名称就是招生班级名称，
         * 开始日期，结束日期，就是立项的开始日期、结束日期，项目分类，是招生班级对应的项目分类对应的dynamic项目分类。
         * 并且后面 同步的学生，对应的教务班级都是这个班级
         */
        String tzms_cls_defn_tid = "";
        if("EE".equals(TZ_PRJ_TYPE_NAME)) {
        	querySql = "SELECT A.tzms_cls_defn_tid FROM tzms_cls_defn_t_tzms_lxxmb_def_tBase A, tzms_lxxmb_def_tBase B WHERE A.tzms_lxxmb_def_tid=B.tzms_lxxmb_def_tId AND B.tzms_item_num=?";
        	tzms_cls_defn_tid = sqlQuery.queryForObject(querySql, new Object[] {TZ_LXBH}, "String");

        	if(StringUtils.isBlank(tzms_cls_defn_tid)) {
        		querySql = "SELECT tzms_lxxmb_def_tId, tzms_cls_start, tzms_cls_end FROM tzms_lxxmb_def_tBase WHERE tzms_item_num=?";
        		Map<String, Object> lxMap = sqlQuery.queryForMap(querySql, new Object[] {TZ_LXBH});
        		String tzms_cls_start = "";
        		String tzms_cls_end = "";
        		String tzms_lxxmb_def_tId = "";
        		if(lxMap != null) {
        			tzms_lxxmb_def_tId = lxMap.get("tzms_lxxmb_def_tId") == null ? "":lxMap.get("tzms_lxxmb_def_tId").toString();
        			tzms_cls_start = lxMap.get("tzms_cls_start") == null ? "":lxMap.get("tzms_cls_start").toString();
        			tzms_cls_end = lxMap.get("tzms_cls_end") == null ? "":lxMap.get("tzms_cls_end").toString();
        		}

        		Map<String, Object> classMap = new HashMap<>();
        		classMap.put("tzms_class_name", TZ_CLASS_NAME);
        		if(StringUtils.isNoneBlank(tzms_cls_start)) {
        			classMap.put("tzms_cls_start_dt", tzms_cls_start.substring(0, 10));
        		}
        		if(StringUtils.isNoneBlank(tzms_cls_end)) {
        			classMap.put("tzms_cls_end_dt", tzms_cls_end.substring(0, 10));
        		}
        		if(StringUtils.isNoneBlank(TZ_SCHOOL_YEAR)) {
        			classMap.put("tzms_year_id@odata.bind", "/tzms_school_year_ts(" + TZ_SCHOOL_YEAR + ")");
        		}
        		classMap.put("tzms_pro_classify_uniqueid@odata.bind", "/tzms_pro_classify_ts(" + TZ_DYPRJ_TYPE_ID + ")");

        		System.out.println("sync tzms_cls_defn_ts");
        		String url2 = domain + "/api/data/v8.2/tzms_cls_defn_ts";
        		String postData = jacksonUtil.Map2json(classMap);
        		//System.out.println("postData2:" + postData);
        		try {
        			flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url2, postData);
        			result = tzADFSObject.getWebAPIResult();
        		} catch (Exception e1) {
        			e1.printStackTrace();
        			flag = false;
        			result = tzADFSObject.getErrorMessage();
        			mapRet.put("flag", flag);
        			mapRet.put("result", result);
        			return jacksonUtil.Map2json(mapRet);
        		}
        		//System.out.println("result:" + result);
        		jacksonUtil.json2Map(result);
        		tzms_cls_defn_tid = jacksonUtil.getString("tzms_cls_defn_tid");
        		System.out.println("tzms_cls_defn_tid:" + tzms_cls_defn_tid);
        		if(StringUtils.isBlank(tzms_cls_defn_tid)) {
        			mapRet.put("flag", "fail");
        			mapRet.put("result", "同步失败，创建班级失败！");
        			return jacksonUtil.Map2json(mapRet);
        		}

        		System.out.println("sync tzms_cls_defn_t_tzms_lxxmb_def_ts");
        		//项目班级表
        		Map<String, Object> classProMap = new HashMap<String, Object>();
        		classProMap.put("entityId1", tzms_cls_defn_tid);
        		classProMap.put("entityName1", "tzms_cls_defn_t");
        		classProMap.put("entityId2", tzms_lxxmb_def_tId);
        		classProMap.put("entityName2", "tzms_lxxmb_def_t");
        		classProMap.put("RelationshipName", "tzms_cls_defn_t_tzms_lxxmb_def_t");
        		postData = jacksonUtil.Map2json(classProMap);

        		url2 = domain + "/ISV/TZEMSServices/Services/CreateManyToMany.asmx/createManyToMany";
        		//System.out.println("post2:" + postData);
        		try {
        			flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url2, postData);
        			result = tzADFSObject.getWebAPIResult();
        		} catch (Exception e1) {
        			e1.printStackTrace();
        			flag = false;
        			result = tzADFSObject.getErrorMessage();
        			mapRet.put("flag", flag);
        			mapRet.put("result", result);
        			return jacksonUtil.Map2json(mapRet);
        		}
        		//System.out.println("result:" + result);
        	}
        }


        //入学年月  学生所属年月，是通过班级所属学年找学年的第一天作为学生的所属年月
        //querySql = "SELECT TZ_RX_DT FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
        //String TZ_RX_DT = sqlQuery.queryForObject(querySql,new Object[]{classID},"String");
        //TZ_RX_DT = TZ_RX_DT == null ? "":TZ_RX_DT.replaceAll("-", "").substring(0, 6);
        querySql = "SELECT tzms_start_date FROM tzms_school_year_t WHERE tzms_school_year_tId = ?";
        String TZ_RX_DT = sqlQuery.queryForObject(querySql,new Object[]{TZ_SCHOOL_YEAR},"String");
        TZ_RX_DT = TZ_RX_DT == null ? "":TZ_RX_DT.replaceAll("-", "").substring(0, 6);

        //JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new JSONArray(appInsIds);

        //查找学生ownerId,根据班级的管理人员查询教职员表的所属部门的数据管理员，如果没有找到，默认是EEDAdmin
        String ownerId = "";
        querySql = "SELECT TOP 1 OPRID FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID=?";
        String adminOprid = sqlQuery.queryForObject(querySql, new Object[] {classID}, "String");
        if(StringUtils.isNoneBlank(adminOprid)) {
        	querySql = "SELECT A.tzms_data_mng_userid FROM tzms_org_structure_tree_t A, tzms_org_user_t B, tzms_tea_defn_t C WHERE C.tzms_tea_defn_tid = B.tzms_tea_defnid AND B.tzms_org_uniqueid = A.tzms_org_structure_tree_tid AND C.tzms_oprid=?";
        	ownerId = sqlQuery.queryForObject(querySql, new Object[] {adminOprid}, "String");
        }

        if(StringUtils.isBlank(ownerId)) {
        	querySql = "SELECT systemuserid FROM systemuser WHERE FullName=?";
        	ownerId = sqlQuery.queryForObject(querySql, new Object[] {"EED管理员"}, "String");
        }


		String tzms_stu_defn_tid = "";

        try {
			//查询报名表相关用户信息，用于同步教务
        	querySql = "SELECT B.TZ_REALNAME,B.TZ_GENDER,B.TZ_FIRST_NAME,B.TZ_LAST_NAME,B.TZ_COMPANY_NAME,B.NATIONAL_ID,B.NATIONAL_ID_TYPE,B.TZ_COMMENT4,B.BIRTHDATE,C.TZ_EMAIL,C.TZ_MOBILE FROM  PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B,ps_tz_aq_yhxx_tbl C WHERE A.OPRID = B.OPRID AND A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
			//for (int i = 0; i < appInsIdJSONArray.size(); i++) {
			    //String appInsId = String.valueOf(appInsIdJSONArray.get(i));
			    Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{appInsId});
			    if(map!=null){
			        String tzms_name = map.get("TZ_REALNAME") == null ? "": String.valueOf(map.get("TZ_REALNAME"));
			        String TZ_GENDER = map.get("TZ_GENDER") == null ? "": String.valueOf(map.get("TZ_GENDER"));
			        int tzms_gender = 1;
			        if("M".equals(TZ_GENDER))tzms_gender = 0;
			        String tzms_first_name = map.get("TZ_FIRST_NAME") == null ? "": String.valueOf(map.get("TZ_FIRST_NAME"));
			        String tzms_last_name = map.get("TZ_LAST_NAME") == null ? "": String.valueOf(map.get("TZ_LAST_NAME"));
			        String tzms_comp_name = map.get("TZ_COMPANY_NAME") == null ? "": String.valueOf(map.get("TZ_COMPANY_NAME"));
			        String tzms_id_num = map.get("NATIONAL_ID") == null ? "": String.valueOf(map.get("NATIONAL_ID"));
			        //String tzms_comp_name = map.get("NATIONAL_ID_TYPE") == null ? "": String.valueOf(map.get("NATIONAL_ID_TYPE"));
			        String tzms_comp_position = map.get("TZ_COMMENT4") == null ? "": String.valueOf(map.get("TZ_COMMENT4"));
			        String tzms_birthdate = map.get("BIRTHDATE") == null ? "": String.valueOf(map.get("BIRTHDATE"));
			        String TZ_EMAIL = map.get("TZ_EMAIL") == null ? "": String.valueOf(map.get("TZ_EMAIL"));
			        String TZ_MOBILE = map.get("TZ_MOBILE") == null ? "": String.valueOf(map.get("TZ_MOBILE"));

					String TZ_HYZK = "", TZ_ZZMM = "", TZ_TXDZ = "", TZ_JJLXR_NAME = "", TZ_JJLXR_PHONE = "", TZ_JJLXR_EMAIL = "",
							TZ_GZNX = "", TZ_GLNX = "", TZ_DATE = "", TZ_GZDW_CHN = "", TZ_GZDW_EN = "", TZ_ZW_CHN = "",
							TZ_ZW_YW = "", TZ_DWRS = "", TZ_XSRS = "", TZ_GSZZC = "", TZ_GSXSE = "", TZ_NSDS = "", TZ_NLL = "",
							TZ_SS_FLG = "", TZ_GP_DM = "", TZ_GSWZ = "", TZ_DWHH = "", TZ_DWXZ = "", TZ_GZLY = "", TZ_ZGXLXZ = "",
							TZ_ZZJB = "",TZ_ZFJB = "", TZ_ZGXL = "", TZ_ZGXW = "", TZ_ZGZY = "";

					//查询考生扩展基本信息表
					querySql = "SELECT * FROM PS_TZ_KS_KZ_INF_T WHERE TZ_APP_INS_ID = ?";
					Map<String, Object> infoMap = sqlQuery.queryForMap(querySql,new Object[]{appInsId});

					//查询选项集的值
					String queryComboSql = "SELECT TOP 1 FIELD_VALUE FROM tzms_combo_option_v WHERE Field_Name = ? AND FIELD_LABEL=?";

					String url = domain + "/api/data/v8.2/tzms_stu_defn_ts";
					if(infoMap != null) {
						//婚姻状况
						TZ_HYZK = infoMap.get("TZ_HYZK") == null ? "" : infoMap.get("TZ_HYZK").toString();
						TZ_HYZK = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_marital_status", TZ_HYZK}, "String");
						//int TZ_HYZK2 = Integer.parseInt(TZ_HYZK == null ? "2" : TZ_HYZK);
						int TZ_HYZK2 = Integer.parseInt(TZ_HYZK == null ? "-1" : TZ_HYZK);
						//政治面貌
						TZ_ZZMM = infoMap.get("TZ_ZZMM") == null ? "" : infoMap.get("TZ_ZZMM").toString();
						TZ_ZZMM = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_policital", TZ_ZZMM}, "String");
						//int TZ_ZZMM2 = Integer.parseInt(TZ_ZZMM == null ? "12" : TZ_ZZMM);
						int TZ_ZZMM2 = Integer.parseInt(TZ_ZZMM == null ? "-1" : TZ_ZZMM);
						//通讯地址
						TZ_TXDZ = infoMap.get("TZ_TXDZ") == null ? "" : infoMap.get("TZ_TXDZ").toString();
						//紧急联系人姓名
						TZ_JJLXR_NAME = infoMap.get("TZ_JJLXR_NAME") == null ? "" : infoMap.get("TZ_JJLXR_NAME").toString();
						//紧急联系人手机
						TZ_JJLXR_PHONE = infoMap.get("TZ_JJLXR_PHONE") == null ? "" : infoMap.get("TZ_JJLXR_PHONE").toString();
						//紧急联系人邮箱
						TZ_JJLXR_EMAIL = infoMap.get("TZ_JJLXR_EMAIL") == null ? "" : infoMap.get("TZ_JJLXR_EMAIL").toString();
						//全职工作年限
						TZ_GZNX = infoMap.get("TZ_GZNX") == null ? "" : infoMap.get("TZ_GZNX").toString();
						//管理岗位工作年限
						TZ_GLNX = infoMap.get("TZ_GLNX") == null ? "" : infoMap.get("TZ_GLNX").toString();
						//当前工作入职日期
						TZ_DATE = infoMap.get("TZ_DATE") == null ? "" : infoMap.get("TZ_DATE").toString();
						//工作单位中文
						TZ_GZDW_CHN = infoMap.get("TZ_GZDW_CHN") == null ? "" : infoMap.get("TZ_GZDW_CHN").toString();
						//工作单位英文
						TZ_GZDW_EN = infoMap.get("TZ_GZDW_EN") == null ? "" : infoMap.get("TZ_GZDW_EN").toString();
						//职务中文
						TZ_ZW_CHN = infoMap.get("TZ_ZW_CHN") == null ? "" : infoMap.get("TZ_ZW_CHN").toString();
						//职务英文
						TZ_ZW_YW = infoMap.get("TZ_ZW_YW") == null ? "" : infoMap.get("TZ_ZW_YW").toString();
						//单位员工人数
						TZ_DWRS = infoMap.get("TZ_DWRS") == null ? "" : infoMap.get("TZ_DWRS").toString();
						TZ_DWRS = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_comp_size_option", TZ_DWRS}, "String");
						//int TZ_DWRS2 = Integer.parseInt(TZ_DWRS == null ? "8" : TZ_DWRS);
						int TZ_DWRS2 = Integer.parseInt(TZ_DWRS == null ? "-1" : TZ_DWRS);

						//直接下属人数
						TZ_XSRS = infoMap.get("TZ_XSRS") == null ? "0" : infoMap.get("TZ_XSRS").toString();
						//公司总资产
						TZ_GSZZC = infoMap.get("TZ_GSZZC") == null ? "0" : infoMap.get("TZ_GSZZC").toString();
						//公司上年销售额
						TZ_GSXSE = infoMap.get("TZ_GSXSE") == null ? "0" : infoMap.get("TZ_GSXSE").toString();
						//年所得税缴纳款
						TZ_NSDS = infoMap.get("TZ_NSDS") == null ? "0" : infoMap.get("TZ_NSDS").toString();
						//年利润
						TZ_NLL = infoMap.get("TZ_NLL") == null ? "0" : infoMap.get("TZ_NLL").toString();
						//是否上市
						TZ_SS_FLG = infoMap.get("TZ_SS_FLG") == null ? "" : infoMap.get("TZ_SS_FLG").toString();
						boolean TZ_SS_FLG2 = false;
						if("是".equals(TZ_SS_FLG)) {
							TZ_SS_FLG2 = true;
						}
						//上市代码
						TZ_GP_DM = infoMap.get("TZ_GP_DM") == null ? "" : infoMap.get("TZ_GP_DM").toString();
						//公司网站
						TZ_GSWZ = infoMap.get("TZ_GSWZ") == null ? "" : infoMap.get("TZ_GSWZ").toString();
						//单位行业及细分
						TZ_DWHH = infoMap.get("TZ_DWHH") == null ? "" : infoMap.get("TZ_DWHH").toString();
						//一级公司行业 默认都是其他行业
						String industry1 = "49";
						//金融行业细分 默认都是其他行业
						String industry2 = "114";
						if(TZ_DWHH.indexOf("|") != -1) {
							String[] industrys = TZ_DWHH.split("|");
							industry1 = industrys[0];
							industry2 = industrys[1];
						}
						industry1 = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_blt_industry", industry1}, "String");
						//int industry3 = Integer.parseInt(industry1 == null ? "49" : industry1);
						int industry3 = Integer.parseInt(industry1 == null ? "-1" : industry1);
						industry2 = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_blt_industry2_option", industry2}, "String");
						//int industry4 = Integer.parseInt(industry2 == null ? "114" : industry2);
						int industry4 = Integer.parseInt(industry2 == null ? "-1" : industry2);

						//单位性质
						TZ_DWXZ = infoMap.get("TZ_DWXZ") == null ? "" : infoMap.get("TZ_DWXZ").toString();
						TZ_DWXZ = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_comp_nature", TZ_DWXZ}, "String");
						//int TZ_DWXZ2 = Integer.parseInt(TZ_DWXZ == null ? "10" : TZ_DWXZ);
						int TZ_DWXZ2 = Integer.parseInt(TZ_DWXZ == null ? "-1" : TZ_DWXZ);
						//工作领域
						TZ_GZLY = infoMap.get("TZ_GZLY") == null ? "" : infoMap.get("TZ_GZLY").toString();
						TZ_GZLY = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_functional_area", TZ_GZLY}, "String");
						//int TZ_GZLY2 = Integer.parseInt(TZ_GZLY == null ? "1" : TZ_GZLY);
						int TZ_GZLY2 = Integer.parseInt(TZ_GZLY == null ? "-1" : TZ_GZLY);
						//职务级别
						TZ_ZZJB = infoMap.get("TZ_ZZJB") == null ? "" : infoMap.get("TZ_ZZJB").toString();
						TZ_ZZJB = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_job_level", TZ_ZZJB}, "String");
						//int TZ_ZZJB2 = Integer.parseInt(TZ_ZZJB == null ? "1" : TZ_ZZJB);
						int TZ_ZZJB2 = Integer.parseInt(TZ_ZZJB == null ? "-1" : TZ_ZZJB);
						//政府级别
						TZ_ZFJB = infoMap.get("TZ_ZFJB") == null ? "" : infoMap.get("TZ_ZFJB").toString();
						//最高学历
						TZ_ZGXL = infoMap.get("TZ_ZGXL") == null ? "" : infoMap.get("TZ_ZGXL").toString();
						TZ_ZGXL = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_edu", TZ_ZGXL}, "String");
						//int TZ_ZGXL2 = Integer.parseInt(TZ_ZGXL == null ? "7" : TZ_ZGXL);
						int TZ_ZGXL2 = Integer.parseInt(TZ_ZGXL == null ? "-1" : TZ_ZGXL);
						//最高学位
						TZ_ZGXW = infoMap.get("TZ_ZGXW") == null ? "" : infoMap.get("TZ_ZGXW").toString();
						TZ_ZGXW = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_dgr", TZ_ZGXW}, "String");
						//int TZ_ZGXW2 = Integer.parseInt(TZ_ZGXW == null ? "4" : TZ_ZGXW);
						int TZ_ZGXW2 = Integer.parseInt(TZ_ZGXW == null ? "-1" : TZ_ZGXW);
						//最高学历专业
						TZ_ZGZY = infoMap.get("TZ_ZGZY") == null ? "" : infoMap.get("TZ_ZGZY").toString();
						//最高学历性质
						TZ_ZGXLXZ = infoMap.get("TZ_ZGXLXZ") == null ? "" : infoMap.get("TZ_ZGXLXZ").toString();

						//同步学生基本信息
						Map<String, Object> paramMap = new HashMap<>();
						paramMap.put("tzms_name", tzms_name);
						paramMap.put("tzms_first_name", tzms_first_name);
						paramMap.put("tzms_last_name", tzms_last_name);
						paramMap.put("tzms_gender", tzms_gender);
						paramMap.put("tzms_id_num", tzms_id_num);
						paramMap.put("tzms_comp_name", tzms_comp_name);
						if(StringUtils.isNoneBlank(tzms_birthdate)) {
							paramMap.put("tzms_birthdate", tzms_birthdate);
						}
						paramMap.put("tzms_comp_position", tzms_comp_position);
						if(TZ_HYZK2 != -1) {
							paramMap.put("tzms_marital_status", TZ_HYZK2);
						}
						if(TZ_ZZMM2 != -1) {
						paramMap.put("tzms_policital", TZ_ZZMM2);
						}
						paramMap.put("tzms_dz_addr", TZ_TXDZ);
						paramMap.put("tzms_contact_name", TZ_JJLXR_NAME);
						paramMap.put("tzms_contact_phone", TZ_JJLXR_PHONE);
						paramMap.put("tzms_contact_email", TZ_JJLXR_EMAIL);
						if(TZ_ZGXW2 != -1) {
							paramMap.put("tzms_highest_dgr", TZ_ZGXW2);
						}
						if(TZ_ZGXL2 != -1) {
							paramMap.put("tzms_highest_edu", TZ_ZGXL2);
						}
						paramMap.put("tzms_zy_code", TZ_ZGZY);
						paramMap.put("tzms_phone", TZ_MOBILE);
						paramMap.put("tzms_email", TZ_EMAIL);
						paramMap.put("tzms_lqjg", TZ_LQ_RESULT);
						//入学年月
						if(StringUtils.isNoneBlank(TZ_RX_DT)) {
							paramMap.put("tzms_stu_rxny", TZ_RX_DT);
						}
						//立项编号
						paramMap.put("tzms_lxbh", TZ_LXBH);
						//项目分类
						if(StringUtils.isNoneBlank(TZ_DYPRJ_TYPE_ID)) {
							paramMap.put("tzms_psn_type@odata.bind", "/tzms_pro_classify_ts(" + TZ_DYPRJ_TYPE_ID + ")");
						}
						if(StringUtils.isNoneBlank(ownerId)) {
							paramMap.put("ownerid@odata.bind", "/systemusers(" + ownerId + ")");
						}
						if(StringUtils.isNoneBlank(tzms_cls_defn_tid)) {
							paramMap.put("tzms_stu_xjbj@odata.bind", "/tzms_cls_defn_ts(" + tzms_cls_defn_tid + ")");
						}


						String postData = jacksonUtil.Map2json(paramMap);
						//System.out.println("postData:" + postData);
						try {
							flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
							result = tzADFSObject.getWebAPIResult();
						} catch (Exception e1) {
							e1.printStackTrace();
							flag = false;
							result = tzADFSObject.getErrorMessage();
							mapRet.put("flag", flag);
							mapRet.put("result", result);
							return jacksonUtil.Map2json(mapRet);
						}
						//System.out.println("result:" + result);

						jacksonUtil.json2Map(result);
						tzms_stu_defn_tid = jacksonUtil.getString("tzms_stu_defn_tid");
						System.out.println("tzms_stu_defn_tid:" + tzms_stu_defn_tid);

						System.out.println("sync PHONE-----------------");
						//同步手机信息
						url = domain + "/api/data/v8.2/tzms_stu_phone_ts";
						Map<String, Object> phoneMap = new HashMap<String, Object>();
						phoneMap.put("tzms_phone", TZ_MOBILE);
						phoneMap.put("tzms_main_flg", true);
						phoneMap.put("tzms_person_id@odata.bind", "/tzms_stu_defn_ts(" + tzms_stu_defn_tid + ")");
						postData = jacksonUtil.Map2json(phoneMap);
						try {
							flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
							result = tzADFSObject.getWebAPIResult();
						} catch (Exception e1) {
							e1.printStackTrace();
							flag = false;
							result = tzADFSObject.getErrorMessage();
							mapRet.put("flag", flag);
							mapRet.put("result", result);
							return jacksonUtil.Map2json(mapRet);
						}
						//System.out.println("result:" + result);

						System.out.println("SYNC EMAIL-----------------");
						//同步邮箱信息
						url = domain + "/api/data/v8.2/tzms_stu_email_ts";
						Map<String, Object> emailMap = new HashMap<String, Object>();
						emailMap.put("tzms_email_addr", TZ_EMAIL);
						emailMap.put("tzms_main_flg", true);
						emailMap.put("tzms_person_id@odata.bind", "/tzms_stu_defn_ts(" + tzms_stu_defn_tid + ")");
						postData = jacksonUtil.Map2json(emailMap);
						try {
							flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
							result = tzADFSObject.getWebAPIResult();
						} catch (Exception e1) {
							e1.printStackTrace();
							flag = false;
							result = tzADFSObject.getErrorMessage();
							mapRet.put("flag", flag);
							mapRet.put("result", result);
							return jacksonUtil.Map2json(mapRet);
						}
						//System.out.println("result:" + result);

						System.out.println("SYNC WORK-----------------");
						//同步学生工作信息
						url = domain + "/api/data/v8.2/tzms_stu_work_ts";
						Map<String, Object> workMap = new HashMap<>();
						workMap.put("tzms_comp_name", TZ_GZDW_CHN);
						workMap.put("tzms_comp_oth_name", TZ_GZDW_EN);
						if(TZ_DWXZ2 != -1) {
							workMap.put("tzms_comp_nature", TZ_DWXZ2);
						}
						if(TZ_GZLY2 != -1) {
							workMap.put("tzms_functional_area", TZ_GZLY2);
						}
						if(TZ_ZZJB2 != -1) {
							workMap.put("tzms_job_level", TZ_ZZJB2);
						}
						workMap.put("tzms_job_position", TZ_ZW_CHN);
						workMap.put("tzms_pos_eng_name", TZ_ZW_YW);
						workMap.put("tzms_pr_expr", true);
						workMap.put("tzms_comp_listed", TZ_SS_FLG2);
						workMap.put("tzms_total_assets", TZ_GSZZC);
						workMap.put("tzms_annual_sales", TZ_GSXSE);
						workMap.put("tzms_annual_profit", TZ_NLL);
						if(TZ_DWRS2 != -1) {
							workMap.put("tzms_comp_size", TZ_DWRS2);
						}
						if(industry3 != -1) {
							workMap.put("tzms_blt_industry", industry3);
						}
						if(industry4 != -1) {
							workMap.put("tzms_blt_industry2", industry4);
						}
						workMap.put("tzms_psn_id@odata.bind", "/tzms_stu_defn_ts(" + tzms_stu_defn_tid + ")");

						postData = jacksonUtil.Map2json(workMap);
						try {
							flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
							result = tzADFSObject.getWebAPIResult();
						} catch (Exception e1) {
							e1.printStackTrace();
							flag = false;
							result = tzADFSObject.getErrorMessage();
							mapRet.put("flag", flag);
							mapRet.put("result", result);
							return jacksonUtil.Map2json(mapRet);
						}
						//System.out.println("result:" + result);
					}else {
						mapRet.put("flag", "fail");
						mapRet.put("result", "同步失败，考生扩展信息表不存在该考生！");
						return jacksonUtil.Map2json(mapRet);
					}


					//查询考生工作经历扩展表
					querySql = "SELECT * FROM PS_TZ_KS_GZJL_T WHERE TZ_APP_INS_ID = ?";
					List<Map<String, Object>> workList = sqlQuery.queryForList(querySql, new Object[] {appInsId});
					//单位名称、开始日期、结束日期、部门、职务
					String TZ_DWMC = "", TZ_START_DATE = "", TZ_END_DATE = "", TZ_BM = "", TZ_ZW = "";
					if(workList != null && workList.size() > 0) {
						//同步学生工作信息
						url = domain + "/api/data/v8.2/tzms_stu_work_ts";
						for (Map<String, Object> map2 : workList) {
							if(map2 != null) {
								TZ_DWMC = map2.get("TZ_DWMC") == null ? "" : map2.get("TZ_DWMC").toString();
								TZ_START_DATE = map2.get("TZ_START_DATE") == null ? "" : map2.get("TZ_START_DATE").toString();
								TZ_END_DATE = map2.get("TZ_END_DATE") == null ? "" : map2.get("TZ_END_DATE").toString();
								TZ_BM = map2.get("TZ_BM") == null ? "" : map2.get("TZ_BM").toString();
								TZ_ZW = map2.get("TZ_ZW") == null ? "" : map2.get("TZ_ZW").toString();

			    				Map<String, Object> workMap = new HashMap<>();
			    				workMap.put("tzms_comp_name", TZ_DWMC);
			    				if(StringUtils.isNoneBlank(TZ_START_DATE)) {
			    					workMap.put("tzms_sta_dt", TZ_START_DATE);
			    				}
			    				if(StringUtils.isNoneBlank(TZ_END_DATE)) {
			    					workMap.put("tzms_end_dt", TZ_END_DATE);
			    				}
			    				workMap.put("tzms_job_position", TZ_ZW);
			    				workMap.put("tzms_psn_id@odata.bind", "/tzms_stu_defn_ts(" + tzms_stu_defn_tid + ")");

			    				System.out.println("SYNC WORK2-----------------");
			    				String postData = jacksonUtil.Map2json(workMap);
			    				try {
			    					flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
			    					result = tzADFSObject.getWebAPIResult();
			    				} catch (Exception e1) {
			    					e1.printStackTrace();
			    					flag = false;
			    					result = tzADFSObject.getErrorMessage();
			    					mapRet.put("flag", flag);
									mapRet.put("result", result);
			    					return jacksonUtil.Map2json(mapRet);
			    				}
			    				//System.out.println("result:" + result);
							}
						}
					}

					//查询考生教育经历扩展表
					querySql = "SELECT * FROM PS_TZ_KS_YJJL_T WHERE TZ_APP_INS_ID = ?";
					List<Map<String, Object>> eduList = sqlQuery.queryForList(querySql, new Object[] {appInsId});
					//开始时间、结束时间、学校名称、所获学历、所获学位、所学专业、学历编号、学位编号、学位获得时间
					String TZ_START_DATE2 = "", TZ_END_DATE2 = "", TZ_XXMC = "", TZ_SHXL = "", TZ_SHXW = "", TZ_SXZY = "", TZ_XLBH = "", TZ_XWBH = "", TZ_GETDATE = "";
					if(eduList != null && eduList.size() > 0) {
						//同步学生教育信息
						url = domain + "/api/data/v8.2/tzms_stu_edu_ts";
						for (Map<String, Object> map2 : eduList) {
							if(map2 != null) {
								TZ_START_DATE2 = map2.get("TZ_START_DATE") == null ? "" : map2.get("TZ_START_DATE").toString();
								TZ_END_DATE2 = map2.get("TZ_END_DATE") == null ? "" : map2.get("TZ_END_DATE").toString();
								TZ_XXMC = map2.get("TZ_XXMC") == null ? "" : map2.get("TZ_XXMC").toString();
								TZ_SHXL = map2.get("TZ_SHXL") == null ? "" : map2.get("TZ_SHXL").toString();
								TZ_SHXL = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_edu", TZ_SHXL}, "String");
			    				int TZ_SHXL2 = Integer.parseInt(TZ_SHXL == null ? "-1" : TZ_SHXL);
								TZ_SHXW = map2.get("TZ_SHXW") == null ? "" : map2.get("TZ_SHXW").toString();
								TZ_SHXW = sqlQuery.queryForObject(queryComboSql, new Object[] {"tzms_dgr", TZ_SHXW}, "String");
								int TZ_SHXW2 = Integer.parseInt(TZ_SHXW == null ? "-1" : TZ_SHXW);
								TZ_SXZY = map2.get("TZ_SXZY") == null ? "" : map2.get("TZ_SXZY").toString();
								TZ_XLBH = map2.get("TZ_XLBH") == null ? "" : map2.get("TZ_XLBH").toString();
								TZ_XWBH = map2.get("TZ_XWBH") == null ? "" : map2.get("TZ_XWBH").toString();
								TZ_GETDATE = map2.get("TZ_GETDATE") == null ? "" : map2.get("TZ_GETDATE").toString();

								Map<String, Object> eduMap = new HashMap<>();
								eduMap.put("tzms_sch_name", TZ_XXMC);
								if(StringUtils.isNoneBlank(TZ_START_DATE2)) {
									eduMap.put("tzms_admissn_dt", TZ_START_DATE2);
								}
								if(StringUtils.isNoneBlank(TZ_END_DATE2)) {
									eduMap.put("tzms_udrgrd_dt", TZ_END_DATE2);
								}
								eduMap.put("tzms_specialty", TZ_SXZY);
								eduMap.put("tzms_degr_code", TZ_XLBH);
								eduMap.put("tzms_edu_code", TZ_XWBH);
								if(TZ_SHXW2 != -1) {
									eduMap.put("tzms_obt_degr", TZ_SHXW2);
								}
								if(TZ_SHXL2 != -1) {
									eduMap.put("tzms_obt_educ", TZ_SHXL2);
								}
								eduMap.put("tzms_person_id@odata.bind", "/tzms_stu_defn_ts(" + tzms_stu_defn_tid + ")");

								System.out.println("SYNC EDU-----------------");
			    				String postData = jacksonUtil.Map2json(eduMap);
			    				try {
			    					flag= tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData);
			    					result = tzADFSObject.getWebAPIResult();
			    				} catch (Exception e1) {
			    					e1.printStackTrace();
			    					flag = false;
			    					result = tzADFSObject.getErrorMessage();
			    					mapRet.put("flag", flag);
									mapRet.put("result", result);
			    					return jacksonUtil.Map2json(mapRet);
			    				}
			    				//System.out.println("result:" + result);
							}
						}
					}
			    //}

				if(StringUtils.isNoneBlank(tzms_stu_defn_tid)) {
					/**
					 * 同步成功后，需要将获取取的学生ID，写入考生学生关系表TZ_KSHSTU_GX_T，
					 * 并更新DYNAMIC报名表实体中的学生ID。
					 * 同时将工作表（TZ_FORM_WRK_T）中的同步状态改成已同步。
					 */
					querySql = "select count(*) from TZ_KSHSTU_GX_T where TZ_APP_INS_ID = ?";
					int count = sqlQuery.queryForObject(querySql, new Object[] {appInsId}, "int");
					String updateSql = "insert into TZ_KSHSTU_GX_T VALUES(?,?,?,?,?)";
					if(count > 0) {
						updateSql = "update TZ_KSHSTU_GX_T set TZ_STU_BH=?,TZ_PRO_LXBH=?,TZ_OPE_DTTM=?,TZ_OPE_OPRID=? where TZ_APP_INS_ID = ?";
						sqlQuery.update(updateSql, new Object[] {tzms_stu_defn_tid, TZ_LXBH, new Date(), tzOprid, appInsId});
					}else {
						sqlQuery.update(updateSql, new Object[] {appInsId, tzms_stu_defn_tid, TZ_LXBH, new Date(), tzOprid});
					}

					//TODO 并更新DYNAMIC报名表实体中的学生ID。
					updateSql = "UPDATE tzms_edp_wrk_t SET tzms_dynamcis_stuid=? WHERE tzms_app_ins_id = ?";
					sqlQuery.update(updateSql, new Object[] {tzms_stu_defn_tid, appInsId});


					updateSql = "UPDATE PS_TZ_FORM_WRK_T SET TZ_SYNC_EDU='已同步' WHERE TZ_APP_INS_ID = ?";
					sqlQuery.update(updateSql, new Object[] {appInsId});
					mapRet.put("flag", "success");
				}else {
					mapRet.put("flag", "fail");
					mapRet.put("result", "同步失败，参数错误！");
					return jacksonUtil.Map2json(mapRet);
				}
			}else {
				mapRet.put("flag", "fail");
				mapRet.put("result", "同步失败，没有该用户！");
				return jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        return jacksonUtil.Map2json(resultMap);
    }


    /**
     * 查询招生班级是否有立项项目
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String queryLX(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        String querySql = "SELECT TZ_LXBH FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
        String TZ_LXBH = sqlQuery.queryForObject(querySql,new Object[]{classID},"String");
        TZ_LXBH = TZ_LXBH == null ? "":TZ_LXBH;
        resultMap.put("TZ_LXBH",TZ_LXBH);
        return jacksonUtil.Map2json(resultMap);
    }

    /**
     * 同步立项项目
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String syncLX(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        String bj_lxbh = "";
        if(jacksonUtil.containsKey("bj_lxbh")) {
            bj_lxbh = jacksonUtil.getString("bj_lxbh");
        }
        if(bj_lxbh.length()>0){
            //先同步立项项目
            String updateSql = "UPDATE PS_TZ_CLASS_INF_T SET TZ_LXBH = ? WHERE TZ_CLASS_ID = ?";
            sqlQuery.update(updateSql,new Object[]{bj_lxbh,classID});
        }
        return jacksonUtil.Map2json(resultMap);
    }

    /**
     * 批量保存奖学金
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String saveScholarship(String strParams, String[] errorMsg) {
    	String msg = "";
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String appInsIds = "";
        if(jacksonUtil.containsKey("appInsIds")) {
            appInsIds = jacksonUtil.getString("appInsIds");
        }
        String values = "";
        if(jacksonUtil.containsKey("values")) {
            values = jacksonUtil.getString("values");
        }
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new JSONArray(appInsIds);
        JSONObject valueJSONObject =  JSONObject.fromObject(values);
        //JSONObject valueJSONObject = new  JSONObject(values);
        String scholarship = valueJSONObject.getString("TZ_JXJ_ID");
        String remark = valueJSONObject.getString("TZ_NOTE");
        String explanation = valueJSONObject.getString("TZ_EXPLANATION");
        String querySql = "SELECT 'Y' FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = ? AND TZ_JXJ_ID = ?";
        List<String> scholarshipIdList = new ArrayList<>();
        if(scholarship!=null){
            scholarshipIdList.add(scholarship);
        }
        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            String appInsId = String.valueOf(appInsIdJSONArray.get(i));
            if(appInsId!=null){
                msg += saveScholarshipAPI(appInsId,scholarshipIdList,"batch",errorMsg,remark,explanation);
                msg += refreshPayPlanAPI(appInsId,errorMsg);
                /*String flag = sqlQuery.queryForObject(querySql,new Object[]{appInsId,scholarship},"String");
                if(flag!=null&&"Y".equals(flag)){

                }else {
					msg += this.saveSingleScholarship(appInsId,scholarship);
                }*/
            }
        }
		resultMap.put("msg",msg);
        return jacksonUtil.Map2json(resultMap);
    }

	/**
	 * 为单个报名表设置奖学金并更新缴费计划
	 * @param appInsId
	 * @param scholarship
	 * @return
	 */
	public String saveSingleScholarship(String appInsId,String scholarship){
		//获取学生姓名，及所属立项编号
		String querySql = "SELECT C.TZ_REALNAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T C WHERE A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
		String  TZ_REALNAME = sqlQuery.queryForObject(querySql,new Object[]{appInsId},"String");
		TZ_REALNAME = TZ_REALNAME == null ? "" :TZ_REALNAME;
    	String msg = "";
    	//查询已获得的奖学金总额
		String queryJXJSql = "SELECT SUM(ISNULL(B.TZ_JXJ_JE,0)) AS TZ_JXJ_JE_ALL FROM TZ_KS_JXJ_T A LEFT JOIN TZ_JXJ_DEFN_T B ON A.TZ_JXJ_ID = B.TZ_JXJ_ID WHERE A.TZ_APP_INS_ID = ?";
		String JXJ_ALL_FLOAT = sqlQuery.queryForObject(queryJXJSql,new Object[]{appInsId},"String");
        double JXJ_ALL = JXJ_ALL_FLOAT == null ? 0d : Double.parseDouble(JXJ_ALL_FLOAT);
		//获取缴费计划状态为已缴费或部分缴费 且 费用类型为学费的所有 已减免金额
		String queryJMSql = "SELECT SUM(ISNULL(TZ_JF_JM_JE,0)) AS TZ_JF_JM_JE_ALL FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND (TZ_JF_STAT = '2' OR TZ_JF_STAT ='3') AND TZ_JF_TYPE = '2'";
		String TZ_JF_JM_JE_ALL_FLOAT = sqlQuery.queryForObject(queryJMSql,new Object[]{appInsId},"String");
        double TZ_JF_JM_JE_ALL = TZ_JF_JM_JE_ALL_FLOAT == null ? 0d : Double.parseDouble(TZ_JF_JM_JE_ALL_FLOAT);
		//计算剩余可减免金额
		double JXJ_Surplus = JXJ_ALL - TZ_JF_JM_JE_ALL;
		//获取当前新增奖学金
		String JXJ_ADD_FLOAT = sqlQuery.queryForObject("SELECT ISNULL(TZ_JXJ_JE,0) AS JXJ_ADD FROM TZ_JXJ_DEFN_T WHERE TZ_JXJ_ID = ?",new Object[]{scholarship},"String");
        double JXJ_ADD = JXJ_ADD_FLOAT == null ? 0d : Double.parseDouble(JXJ_ADD_FLOAT);
		JXJ_Surplus += JXJ_ADD;
		//查询所有未交费且费用类型为学费应缴金额
		String YJ_JE_FLOAT = sqlQuery.queryForObject("SELECT SUM(ISNULL(TZ_JF_BZ_JE,0)-ISNULL(TZ_JF_TZ_JE,0)) AS YJ_JE FROM TZ_JF_PLAN_T  WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ? AND TZ_JF_STAT = ? AND TZ_JF_TYPE = '2'",new Object[]{appInsId,"1","1"},"String");
        double YJ_JE = YJ_JE_FLOAT == null ? 0d : Double.parseDouble(YJ_JE_FLOAT);
		if(JXJ_Surplus>YJ_JE){
			//奖学金总额超出应收金额
			msg = TZ_REALNAME + "当前奖学金总金额超出缴费计划应收总金额\n";
		}else {
			//新增奖学金
			String updateSql = "INSERT INTO TZ_KS_JXJ_T(TZ_APP_INS_ID,TZ_JXJ_ID) VALUES(CONVERT(bigint,?),?)";
			sqlQuery.update(updateSql,new Object[]{appInsId,scholarship});
			//获取所有未缴费且费用类型为学费的缴费计划
			List<Map<String,Object>> jfPlanList = sqlQuery.queryForList("SELECT TZ_JFPL_ID,ISNULL(TZ_JF_BZ_JE,0) AS TZ_JF_BZ_JE,ISNULL(TZ_JF_TZ_JE,0) AS TZ_JF_TZ_JE  FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ? AND TZ_JF_STAT = ? AND TZ_JF_TYPE = '2'",new Object[]{appInsId,"1","1"});
			if(jfPlanList!=null && jfPlanList.size()>0){
				for (int i = 0; i < jfPlanList.size(); i++) {
					String TZ_JFPL_ID =  String.valueOf(jfPlanList.get(i).get("TZ_JFPL_ID"));
					double  TZ_JF_BZ_JE =  jfPlanList.get(i).get("TZ_JF_BZ_JE") == null ? 0d: Double.parseDouble(String.valueOf(jfPlanList.get(i).get("TZ_JF_BZ_JE")));
					double TZ_JF_TZ_JE =  jfPlanList.get(i).get("TZ_JF_TZ_JE") == null ? 0d: Double.parseDouble(String.valueOf(jfPlanList.get(i).get("TZ_JF_TZ_JE")));
					double TZ_JF_JM_JE = 0d;
					if(TZ_JF_BZ_JE-TZ_JF_TZ_JE >= JXJ_Surplus){
						TZ_JF_JM_JE = JXJ_Surplus;
					}else {
						TZ_JF_JM_JE = TZ_JF_BZ_JE - TZ_JF_TZ_JE;
					}
					JXJ_Surplus = JXJ_Surplus - TZ_JF_JM_JE;
					double TZ_JF_BQYS = TZ_JF_BZ_JE - TZ_JF_TZ_JE - TZ_JF_JM_JE;
					String updatePlanSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_JM_JE = ?,TZ_JF_BQYS = ? WHERE TZ_JFPL_ID = ?";
					sqlQuery.update(updatePlanSql,new Object[]{TZ_JF_JM_JE,TZ_JF_BQYS,TZ_JFPL_ID});

				}
			}
		}
    	return msg;
	}

    /**
     * 查询班级缴费标准金额
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String queryAmount(String strParams, String[] errorMsg) {
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        String querySql = "SELECT TZ_TUITION_STANDARD FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ? ";
        String TZ_TUITION_STANDARD_FLOAT = sqlQuery.queryForObject(querySql,new Object[]{classID},"String");
        double TZ_TUITION_STANDARD = TZ_TUITION_STANDARD_FLOAT == null ? 0d: Double.parseDouble(TZ_TUITION_STANDARD_FLOAT);
        return String.valueOf(TZ_TUITION_STANDARD);
    }

    /**
     * 批量保存缴费计划
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String savePayPlan(String strParams, String[] errorMsg) {
        String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String payPlan = "";
        if(jacksonUtil.containsKey("payPlan")) {
            payPlan = jacksonUtil.getString("payPlan");
        }
        String appInsIds = "";
        if(jacksonUtil.containsKey("appInsIds")) {
            appInsIds = jacksonUtil.getString("appInsIds");
        }
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
        	classID = jacksonUtil.getString("classID");
        }
        JSONArray appInsIdJSONArray =  JSONArray.fromObject(appInsIds);
        //JSONArray appInsIdJSONArray = new  JSONArray(appInsIds);
		String msg = "";

        for (int i = 0; i < appInsIdJSONArray.size(); i++) {
            String appInsId = String.valueOf(appInsIdJSONArray.get(i));
            //msg += setSinglePayPlan(appInsId,payPlan,tzOprid);
            JSONArray payPlanJSONArray = JSONArray.fromObject(payPlan);
            /*List<Map<String,Object>> payPlanList = new ArrayList<>();
            for (int j = 0; j < payPlanJSONArray.size(); j++) {
                Map<String,Object> payPlanMap = JSONObject.fromObject(payPlanJSONArray.get(j));
                payPlanList.add(payPlanMap);
            }*/
            msg += savePayPlanAPI(appInsId,payPlanJSONArray,"batch",errorMsg);
            msg += refreshPayPlanAPI(appInsId,errorMsg);
        }
		resultMap.put("msg",msg);
        return jacksonUtil.Map2json(resultMap);
    }

	/**
	 * 为单个报名报名表创建缴费计划
	 * @param appInsId
	 * @param payPlanStr
	 * @param tzOprid
	 */
    public String setSinglePayPlan(String appInsId,String  payPlanStr,String tzOprid){
    	String msg = "";
        //获取学生姓名，及所属立项编号
        String querySql = "SELECT C.TZ_REALNAME,B.TZ_LXBH FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_REG_USER_T C WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
        Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{appInsId});
        String TZ_REALNAME = map.get("TZ_REALNAME")==null?"": String.valueOf(map.get("TZ_REALNAME"));
        String TZ_LXBH = map.get("TZ_LXBH")==null?"": String.valueOf(map.get("TZ_LXBH"));
        JSONArray payPlanJSONArray = JSONArray.fromObject(payPlanStr);
        //JSONArray payPlanJSONArray = new JSONArray(payPlanStr);
        /**校验缴费总金额-总学费调整是否小于奖学金总额**/
		/**小于则设置不成功**/
		double TZ_JF_BZ_JE_ALL = 0d;//缴费计划标准总金额
		double TZ_JF_TZ_JE_ALL = 0d;//缴费计划学费调整总金额
		double JXJ_ALL = 0d;        //奖学金总金额
		double TZ_JF_JM_JE_ALL = 0d;    //缴费计划已减免总金额
		String queryJXJSql = "SELECT SUM(ISNULL(B.TZ_JXJ_JE,0)) AS TZ_JXJ_JE_ALL FROM TZ_KS_JXJ_T A LEFT JOIN TZ_JXJ_DEFN_T B ON A.TZ_JXJ_ID = B.TZ_JXJ_ID WHERE A.TZ_APP_INS_ID = ?";
        String JXJ_ALL_FLOAT = sqlQuery.queryForObject(queryJXJSql,new Object[]{appInsId},"String");
		JXJ_ALL = JXJ_ALL_FLOAT == null ? 0d : Double.parseDouble(JXJ_ALL_FLOAT);
		String queryJMSql = "SELECT SUM(ISNULL(TZ_JF_JM_JE,0)) AS TZ_JF_JM_JE_ALL FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND (TZ_JF_STAT = '2' OR TZ_JF_STAT ='3') AND TZ_JF_TYPE = '2'";
		String TZ_JF_JM_JE_ALL_FLOAT = sqlQuery.queryForObject(queryJMSql,new Object[]{appInsId},"String");
		TZ_JF_JM_JE_ALL = TZ_JF_JM_JE_ALL_FLOAT == null ? 0d : Double.parseDouble(TZ_JF_JM_JE_ALL_FLOAT);
		for (int i = 0; i < payPlanJSONArray.size(); i++) {
			JSONObject payPlan = JSONObject.fromObject(payPlanJSONArray.get(i));
			TZ_JF_BZ_JE_ALL += payPlan.get("TZ_JF_BZ_JE") == null ? 0d : Double.parseDouble(payPlan.getString("TZ_JF_BZ_JE"));
			TZ_JF_TZ_JE_ALL += payPlan.get("TZ_JF_TZ_JE") == null ? 0d : Double.parseDouble(payPlan.getString("TZ_JF_TZ_JE"));
		}
		//判断剩余奖学金金额 小于总金额-调整总金额
		if(TZ_JF_BZ_JE_ALL-TZ_JF_TZ_JE_ALL>JXJ_ALL-TZ_JF_JM_JE_ALL){
            //删除当前报名表的所有缴费计划
            String delSql = "DELETE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = 1 AND TZ_JF_STAT = '1'";
            sqlQuery.update(delSql,new Object[]{appInsId});
            //开始新增缴费计划
			double JXJ_Surplus = JXJ_ALL - TZ_JF_JM_JE_ALL;
			String insertSql = "INSERT INTO TZ_JF_PLAN_T(TZ_JFPL_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_OBJ_PRJ_ID,TZ_OBJ_NAME,TZ_JF_TYPE,TZ_JF_DATE,TZ_JF_BZ_JE,TZ_JF_STAT,ROW_ADDED_OPRID,ROW_ADDED_DTTM,TZ_JF_TZ_JE,TZ_REMARKS,TZ_JF_JM_JE,TZ_JF_BQYS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			for (int i = 0; i < payPlanJSONArray.size(); i++) {
				JSONObject payPlan = JSONObject.fromObject(payPlanJSONArray.get(i));
				String TZ_JF_TYPE = String.valueOf(payPlan.get("TZ_JF_TYPE"));
				double TZ_JF_BZ_JE = payPlan.get("TZ_JF_BZ_JE") == null ? 0d : Double.parseDouble(payPlan.getString("TZ_JF_BZ_JE"));
				double TZ_JF_TZ_JE = payPlan.get("TZ_JF_TZ_JE") == null ? 0d : Double.parseDouble(payPlan.getString("TZ_JF_TZ_JE"));
				double TZ_JF_JM_JE = 0d;//应减免金额
				double TZ_JF_BQYS = 0d;//应收金额
                if(TZ_JF_TYPE!=null && "2".equals(TZ_JF_TYPE)){
                    //缴费类型为学费 才可进行奖学金减免
                    if(TZ_JF_BZ_JE - TZ_JF_TZ_JE >= JXJ_Surplus){
                        //单条缴费计划应收金额大于等于 奖学金剩余金额
                        TZ_JF_JM_JE = JXJ_Surplus;
                    }else {
                        //单条缴费计划应收金额小于 奖学金剩余金额
                        TZ_JF_JM_JE = TZ_JF_BZ_JE - TZ_JF_TZ_JE;
                    }
                }
				JXJ_Surplus = JXJ_Surplus - TZ_JF_JM_JE;
				TZ_JF_BQYS = TZ_JF_BZ_JE - TZ_JF_TZ_JE - TZ_JF_JM_JE;
				String TZ_JFPL_ID = "JF" + getNum(String.valueOf(getSeqNum.getSeqNum("TZ_JF_PLAN_T", "TZ_JFPL_ID")),8);
				sqlQuery.update(insertSql,new Object[]{TZ_JFPL_ID,appInsId,1,TZ_LXBH,TZ_REALNAME,payPlan.get("TZ_JF_TYPE"),payPlan.get("TZ_JF_DATE"),TZ_JF_BZ_JE,1,tzOprid,new Date(),TZ_JF_TZ_JE,payPlan.get("TZ_REMARKS"),TZ_JF_JM_JE,TZ_JF_BQYS});
			}
		}else {
			msg = TZ_REALNAME + "当前剩余奖学金金额大于（标准计划总金额-学费调整总金额）\n";
		}

		return msg;
    }


    /**
     * 查询符合条件的奖学金  所属学年，所属项目分类一致 及其相关信息 总额，剩余额度
     * @param strParams
     * @param errorMsg
     * @return LIST
     */
    public String queryScholarshipList(String strParams, String[] errorMsg) {
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        //String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B,PS_TZ_CLASS_INF_T C,PS_TZ_PRJ_INF_T D,PS_TZ_PRJ_TYPE_T E WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND B.TZ_PRJ_TYPE = E.TZ_DYPRJ_TYPE_ID AND B.TZ_XN_ID = C.TZ_SCHOOL_YEAR AND C.TZ_PRJ_ID = D.TZ_PRJ_ID AND D.TZ_PRJ_TYPE_ID = E.TZ_PRJ_TYPE_ID AND C.TZ_CLASS_ID = ?";
        String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND TZ_JXJ_YX_STAT = '1' AND B.TZ_PRJ_TYPE LIKE ? AND B.TZ_XN_ID LIKE ? ";
        List<Map<String,Object>> listdata = sqlQuery.queryForList(querySql,new Object[]{"%%","%%"});
        if(listdata!=null&&listdata.size()>0){
            resultMap.put("listData",listdata);
        }else {
            resultMap.put("listData",new ArrayList<Map<String,Object>>());

        }
        return jacksonUtil.Map2json(resultMap);
    }

    /**
     * 查询奖学金相关信息 金额
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String queryScholarship(String strParams, String[] errorMsg) {
        Map<String ,Object> resultMap = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.json2Map(strParams);
        String classID = "";
        if(jacksonUtil.containsKey("classID")) {
            classID = jacksonUtil.getString("classID");
        }
        //查询所属分类，学年
        String queryClassYearSql = "SELECT B.tzms_schyear,A.TZ_TUITION_STANDARD,A.TZ_SCHOOL_YEAR FROM PS_TZ_CLASS_INF_T A,tzms_school_year_t B WHERE A.TZ_SCHOOL_YEAR = B.tzms_school_year_tId AND A.TZ_CLASS_ID = ?";
        Map<String,Object> classMap = sqlQuery.queryForMap(queryClassYearSql,new Object[]{classID});
        String TZ_XN_ID = "";
        String TZ_SCHOOL_YEAR = "";
        double TZ_TUITION_STANDARD = 0d;
        TZ_XN_ID = classMap.get("tzms_schyear")== null ? "": String.valueOf(classMap.get("tzms_schyear"));
        TZ_SCHOOL_YEAR = classMap.get("TZ_SCHOOL_YEAR")== null ? "": String.valueOf(classMap.get("TZ_SCHOOL_YEAR"));
        TZ_TUITION_STANDARD = classMap.get("TZ_TUITION_STANDARD")== null ? 0d: Double.parseDouble(String.valueOf(classMap.get("TZ_TUITION_STANDARD")));
        resultMap.put("TZ_XN_ID",TZ_XN_ID);

        String queryProTypeSql = "SELECT D.tzms_pro_classify_name,D.tzms_pro_classify_tId FROM PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T B,PS_TZ_PRJ_TYPE_T C,tzms_pro_classify_t D WHERE A.TZ_PRJ_ID = B.TZ_PRJ_ID AND B.TZ_PRJ_TYPE_ID = C.TZ_PRJ_TYPE_ID AND C.TZ_DYPRJ_TYPE_ID = D.tzms_pro_classify_tId AND A.TZ_CLASS_ID = ?";
        Map<String,Object> prjMap = sqlQuery.queryForMap(queryProTypeSql,new Object[]{classID});
        String TZ_JXJ_TYPE_NAME = "";
        String tzms_pro_classify_tId = "";
        TZ_JXJ_TYPE_NAME = prjMap.get("tzms_pro_classify_name")== null ? "": String.valueOf(prjMap.get("tzms_pro_classify_name"));
        tzms_pro_classify_tId = prjMap.get("tzms_pro_classify_tId")== null ? "": String.valueOf(prjMap.get("tzms_pro_classify_tId"));
        resultMap.put("TZ_JXJ_TYPE_NAME",TZ_JXJ_TYPE_NAME);

        //查询所属总额，剩余额度
        String lqCountSql = "SELECT count(1) FROM PS_TZ_FORM_WRK_T A, TZ_MS_LQJG_T B WHERE A.TZ_CLASS_ID = ? AND A.TZ_MSH_ID = B.TZ_BMB_ID AND (B.TZ_LQ_RESULT = ? OR B.TZ_LQ_RESULT = ?)";
        double lqCount = sqlQuery.queryForObject(lqCountSql,new Object[]{classID,"录取","预录取"},"double");
        String TZ_JXJ_ZEZB_FLOAT  = sqlQuery.queryForObject("SELECT TZ_JXJ_ZEZB FROM TZ_JXJ_CTR_T WHERE TZ_XN_ID = ? AND TZ_JXJ_PRJ_TYPE = ?",new Object[]{TZ_SCHOOL_YEAR,tzms_pro_classify_tId},"String");
        double TZ_JXJ_ZEZB = TZ_JXJ_ZEZB_FLOAT == null ? 0d : Double.parseDouble(TZ_JXJ_ZEZB_FLOAT);
        double TZ_FP_COUNT = TZ_TUITION_STANDARD * lqCount * TZ_JXJ_ZEZB;
        resultMap.put("TZ_FP_COUNT",TZ_FP_COUNT);

        String JXJYCOUNTSql = "SELECT SUM(A.TZ_JXJ_JE) FROM TZ_JXJ_DEFN_T A,TZ_KS_JXJ_T B,TZ_JXJ_TYPE_T C,PS_TZ_FORM_WRK_T D,TZ_MS_LQJG_T E WHERE A.TZ_JXJ_ID = B.TZ_JXJ_ID AND A.TZ_JXJ_TYPE_ID = C.TZ_JXJ_TYPE_ID AND B.TZ_APP_INS_ID = D.TZ_APP_INS_ID AND D.TZ_MSH_ID = E.TZ_BMB_ID AND C.TZ_PRJ_TYPE = ? AND C.TZ_XN_ID = ? AND D.TZ_CLASS_ID = ? AND (E.TZ_LQ_RESULT = ? OR E.TZ_LQ_RESULT = ?)";
        String JXJ_COUNT_FLOAT  = sqlQuery.queryForObject(JXJYCOUNTSql,new Object[]{tzms_pro_classify_tId,TZ_SCHOOL_YEAR,classID,"录取","预录取"},"String");
        double JXJ_COUNT = JXJ_COUNT_FLOAT == null ? 0d : Double.parseDouble(JXJ_COUNT_FLOAT);
        double TZ_FP_SURPLUS = TZ_FP_COUNT - JXJ_COUNT;

        resultMap.put("TZ_FP_SURPLUS",TZ_FP_SURPLUS);

        //String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME,A.TZ_JXJ_JE FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B,PS_TZ_CLASS_INF_T C,PS_TZ_PRJ_INF_T D,PS_TZ_PRJ_TYPE_T E WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND B.TZ_PRJ_TYPE = E.TZ_DYPRJ_TYPE_ID AND B.TZ_XN_ID = C.TZ_SCHOOL_YEAR AND C.TZ_PRJ_ID = D.TZ_PRJ_ID AND D.TZ_PRJ_TYPE_ID = E.TZ_PRJ_TYPE_ID AND C.TZ_CLASS_ID = ?";
        //String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME,A.TZ_JXJ_JE,B.TZ_PRJ_TYPE,B.TZ_XN_ID FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND A.TZ_JXJ_ID = ?";
        //Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{TZ_JXJ_ID});
        //String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME,A.TZ_JXJ_JE FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND TZ_JXJ_YX_STAT = '1' AND B.TZ_PRJ_TYPE LIKE ? AND B.TZ_XN_ID LIKE ? ";
        String querySql = "SELECT A.TZ_JXJ_ID,A.TZ_JXJ_NAME,A.TZ_JXJ_JE,A.TZ_NOTE FROM TZ_JXJ_DEFN_T A,TZ_JXJ_TYPE_T B WHERE A.TZ_JXJ_TYPE_ID = B.TZ_JXJ_TYPE_ID AND TZ_JXJ_YX_STAT = '1' AND B.TZ_PRJ_TYPE = ? AND B.TZ_XN_ID = ? ";
        //System.out.println("TZ_SCHOOL_YEAR=========="+tzms_pro_classify_tId);
        //System.out.println("TZ_SCHOOL_YEAR=========="+TZ_SCHOOL_YEAR);
        List<Map<String,Object>> listdata = sqlQuery.queryForList(querySql,new Object[]{tzms_pro_classify_tId,TZ_SCHOOL_YEAR});
        System.out.println(listdata);
        if(listdata!=null&&listdata.size()>0){
            resultMap.put("listData",listdata);
        }else {
            resultMap.put("listData",new ArrayList<Map<String,Object>>());
        }


        return jacksonUtil.Map2json(resultMap);
    }


    /**
     * 获取关联dynamics学生信息
     * @param appInsId
     */
    public Map<String,Object>  getDynamicsInfo(String appInsId){
        Map<String,Object>  resultMap = new HashMap<>();
        String querySql = "SELECT B.tzms_class_name,B.tzms_tea_charge,A.tzms_stu_xjzt FROM tzms_stu_defn_t A,tzms_cls_defn_t B,TZ_KSHSTU_GX_T D WHERE A.tzms_stu_xjbj = B.tzms_cls_defn_tId AND convert(varchar(36),A.tzms_stu_defn_tid) = D.TZ_STU_BH AND D.TZ_APP_INS_ID = ?";
        Map<String,Object> mapDynamicsInfo = sqlQuery.queryForMap(querySql,new Object[]{appInsId});
        if(mapDynamicsInfo!=null){
            resultMap.put("className",mapDynamicsInfo.get("tzms_class_name"));
            resultMap.put("classTea",mapDynamicsInfo.get("tzms_tea_charge"));
            resultMap.put("stuStatus",mapDynamicsInfo.get("tzms_stu_xjzt"));
        }else {
            resultMap.put("className","");
            resultMap.put("classTea","");
            resultMap.put("stuStatus","");
        }
        return resultMap;
    }

    /**
     * 获取相关金额
     * @param appInsId
     * @return
     */
    public Map<String,Object> getAmount(String appInsId){
        Map<String,Object>  resultMap = new HashMap<>();
        //double TZ_JF_BZ_JE = 0d; //标准金额
        double TZ_JF_TZ_JE = 0d; //调整金额
        double TZ_JF_BQYS = 0d; //应收金额
        double TZ_JF_BQYJ = 0d; //已缴金额
        double TZ_JF_BQYT = 0d; //退费金额
        double TZ_JF_BQSS = 0d; //实收金额
        double TZ_JF_BQWS = 0d; //未收金额
        double TZ_JXJ_JE = 0d;   //奖学金总额
        double TZ_DZ_JIE = 0d;   //到账总计
        String payPlanStr = "";
        //查询标准金额，调整金额，已缴金额
        String querySql = "SELECT SUM(ISNULL(TZ_JF_BZ_JE,0)) TZ_JF_BZ_JE,SUM(ISNULL(TZ_JF_TZ_JE,0)) TZ_JF_TZ_JE,SUM(ISNULL(TZ_JF_BQYS,0)) TZ_JF_BQYS,SUM(ISNULL(TZ_JF_BQSS,0)) TZ_JF_BQYJ,SUM(ISNULL(TZ_JF_BQYT,0)) TZ_JF_BQYT, SUM(ISNULL(TZ_JF_BQSS,0)) TZ_JF_BQSS,SUM(ISNULL(TZ_JF_JM_JE,0)) TZ_JF_JM_JE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ?";
        Map<String,Object> planMap =  sqlQuery.queryForMap(querySql,new Object[]{appInsId,1});
        //查询奖学金总额
       /* String querySql2 = "SELECT SUM(ISNULL(TZ_JXJ_JE,0)) TZ_JXJ_JE FROM TZ_JXJ_DEFN_T A,TZ_KS_JXJ_T B WHERE A.TZ_JXJ_ID = B.TZ_JXJ_ID AND B.TZ_APP_INS_ID = ? ";
        Object TZ_JXJ_JE_OBJ = sqlQuery.queryForObject(querySql2,new Object[]{appInsId},"Object");

        if(TZ_JXJ_JE_OBJ!=null){
            TZ_JXJ_JE = Double.parseDouble(String.valueOf(TZ_JXJ_JE_OBJ));
        }*/
        //查询到账总计
        String querySql3 = "SELECT SUM(ISNULL(TZ_DZ_JIE,0)) TZ_DZ_JIE FROM TZ_DZJL_TBL WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ?";
        Object TZ_DZ_JIE_OBJ = sqlQuery.queryForObject(querySql3,new Object[]{appInsId,1},"Object");
        if(TZ_DZ_JIE_OBJ!=null){
            TZ_DZ_JIE = Double.parseDouble(String.valueOf(TZ_DZ_JIE_OBJ));
        }
        if(planMap!=null){
            //TZ_JF_BZ_JE = planMap.get("TZ_JF_BZ_JE")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_BZ_JE")));
            TZ_JF_TZ_JE = planMap.get("TZ_JF_TZ_JE")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_TZ_JE")));
			TZ_JF_BQYS = planMap.get("TZ_JF_BQYS")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_BQYS")));
            TZ_JF_BQYJ = planMap.get("TZ_JF_BQYJ")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_BQYJ")));
			TZ_JF_BQYT = planMap.get("TZ_JF_BQYT")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_BQYT")));
            TZ_JF_BQSS = planMap.get("TZ_JF_BQSS")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_BQSS")));
            TZ_JXJ_JE = planMap.get("TZ_JF_JM_JE")==null?0d: Double.parseDouble(String.valueOf(planMap.get("TZ_JF_JM_JE")));
            TZ_JF_BQWS = TZ_JF_BQYS - TZ_JF_BQSS;
        }
        //计算应收金额
        //TZ_JF_BQYS = TZ_JF_BZ_JE - TZ_JF_TZ_JE - TZ_JXJ_JE;
        //获取缴费计划
        String queryPayPlanSql = "SELECT TZ_JF_BQYS,CONVERT(varchar,TZ_JF_DATE,23) TZ_JF_DATE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ?";
        List<Map<String,Object>> payPlanList = sqlQuery.queryForList(queryPayPlanSql,new Object[]{appInsId,"1"});
        if(payPlanList !=null && payPlanList.size()>0) {
            DecimalFormat df = new DecimalFormat("###,000.00");
            StringBuffer str = new StringBuffer();
            for (Map<String,Object> payPlanMap:payPlanList){
                String YS = df.format(payPlanMap.get("TZ_JF_BQYS")==null?0d: Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_BQYS"))));
                String TZ_JF_DATE = payPlanMap.get("TZ_JF_DATE")==null? "": String.valueOf(payPlanMap.get("TZ_JF_DATE"));
                if(str.length() == 0){
                    str.append(YS + "," + TZ_JF_DATE);
                }else {
                    str.append("|" + YS + "," + TZ_JF_DATE);
                }
            }
            payPlanStr = String.valueOf(str);
        }

        //resultMap.put("TZ_JF_BZ_JE",TZ_JF_BZ_JE);
        resultMap.put("TZ_JF_TZ_JE",TZ_JF_TZ_JE);
        resultMap.put("TZ_JF_BQYS",TZ_JF_BQYS);
        resultMap.put("TZ_JF_BQYJ",TZ_JF_BQYJ);
        resultMap.put("TZ_JF_BQYT",TZ_JF_BQYT);
        resultMap.put("TZ_JF_BQSS",TZ_JF_BQSS);
        resultMap.put("TZ_JF_BQWS",TZ_JF_BQWS);
        resultMap.put("TZ_JXJ_JE",TZ_JXJ_JE);
        resultMap.put("TZ_DZ_JIE",TZ_DZ_JIE);
        resultMap.put("TZ_TF_JIE",TZ_JF_BQYT);
        resultMap.put("payPlanStr",payPlanStr);

        return  resultMap;

    }


    public static String getNum(String  str,int length){
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < length-str.length(); i++) {
            s.append("0");
        }
        return s.toString()+str;
    }

    /**
     * 设置缴费计划
     * 此方法暂不进行缴费计划刷新（此方法为新增缴费计划，请勿传已有的缴费计划）
     * @param objId
     * @param payPlanList
     * @param operation 设置奖学金增加方式 single：单个设置（属性为更新，新增）；batch : 批量设置（属性为新增）
     *                 默认为：batch
     * @param errorMsg
     * @return
     */
    public String savePayPlanAPI(String objId, List<Map<String,Object>> payPlanList,String operation, String[] errorMsg){
        String msg = "";
        try{
            String tzOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
            /**获取学生姓名，及所属立项编号**/
            String querySql = "SELECT C.TZ_REALNAME,B.TZ_LXBH FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_REG_USER_T C WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
            Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{objId});
            String TZ_REALNAME = map.get("TZ_REALNAME") == null?"": String.valueOf(map.get("TZ_REALNAME"));
            String TZ_LXBH = map.get("TZ_LXBH") == null ? "": String.valueOf(map.get("TZ_LXBH"));
            String insertPayPlanSql = "INSERT INTO TZ_JF_PLAN_T(TZ_JFPL_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_OBJ_PRJ_ID,TZ_OBJ_NAME,TZ_JF_TYPE,TZ_JF_DATE,TZ_JF_BZ_JE,TZ_JF_STAT,ROW_ADDED_OPRID,ROW_ADDED_DTTM,TZ_JF_TZ_JE,TZ_REMARKS,TZ_JF_JM_JE,TZ_JF_BQYS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String updatePayPlanSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_BZ_JE = ? ,TZ_JF_TZ_JE = ? ,TZ_JF_DATE = ?,TZ_JF_TYPE = ?,TZ_REMARKS = ? WHERE TZ_JFPL_ID = ?";
            if(operation == null || !"single".equals(operation)){
                //批量设置
                /**判断用户当前实例是否存在已缴费或部分缴费的缴费计划，若存在，不进行缴费计划批量设置**/
                String queryPayPlanCountSql = "SELECT COUNT(1) FROM TZ_JF_PLAN_T WHERE  TZ_OBJ_ID = ? AND (TZ_JF_STAT = '2'  OR TZ_JF_STAT = '3') ";
                int count = sqlQuery.queryForObject(queryPayPlanCountSql,new Object[]{objId},"int");
                if(count > 0){
                    return msg;
                }
                /**=1=删除所有未缴费的缴费计划**/
                String delSql = "DELETE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = 1 AND TZ_JF_STAT = '1'";
                sqlQuery.update(delSql,new Object[]{objId});
                /**
                 * =2= 新增新的缴费计划
                 **/
                if(payPlanList != null && payPlanList.size() > 0){
                    payPlanList.removeAll(Collections.singleton(null));
                    for (int i = 0; i < payPlanList.size(); i++) {
                        Map<String,Object> payPlanMap = payPlanList.get(i);
                        double TZ_JF_BZ_JE = payPlanMap.get("TZ_JF_BZ_JE") == null ? 0d : Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_BZ_JE")));
                        double TZ_JF_TZ_JE = payPlanMap.get("TZ_JF_TZ_JE") == null ? 0d : Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_TZ_JE")));
                        String TZ_JFPL_ID = "JF" + getNum(String.valueOf(getSeqNum.getSeqNum("TZ_JF_PLAN_T", "TZ_JFPL_ID")),8);
                        sqlQuery.update(insertPayPlanSql,new Object[]{TZ_JFPL_ID,objId,1,TZ_LXBH,TZ_REALNAME,payPlanMap.get("TZ_JF_TYPE"),payPlanMap.get("TZ_JF_DATE"),TZ_JF_BZ_JE,1,tzOprid,new Date(),TZ_JF_TZ_JE,payPlanMap.get("TZ_REMARKS"),0,0});
                    }
                }
            }else if("single".equals(operation)){
                //为单个用户设置不，先进行未缴费删除，在更新（已缴费，部分缴费）及新增
                /**=1=删除所有未缴费的缴费计划**/
                String delSql = "DELETE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = 1 AND TZ_JF_STAT = '1'";
                sqlQuery.update(delSql,new Object[]{objId});
                /**
                 * =2= 更新已缴费，部分缴费；新增未缴费
                 **/
                if(payPlanList != null && payPlanList.size() > 0){
                    payPlanList.removeAll(Collections.singleton(null));
                    for (int i = 0; i < payPlanList.size(); i++) {
                        Map<String,Object> payPlanMap = payPlanList.get(i);
                        String TZ_JFPL_ID = payPlanMap.get("TZ_JFPL_ID") == null ? "" : String.valueOf(payPlanMap.get("TZ_JFPL_ID"));
                        String TZ_JF_STAT = payPlanMap.get("TZ_JF_STAT") == null ? "" : String.valueOf(payPlanMap.get("TZ_JF_STAT"));
                        String TZ_JF_TYPE = payPlanMap.get("TZ_JF_TYPE") == null ? "" : String.valueOf(payPlanMap.get("TZ_JF_TYPE"));
                        double TZ_JF_BZ_JE = payPlanMap.get("TZ_JF_BZ_JE") == null ? 0d : Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_BZ_JE")));
                        double TZ_JF_TZ_JE = payPlanMap.get("TZ_JF_TZ_JE") == null ? 0d : Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_TZ_JE")));
                        if(TZ_JFPL_ID.length()>0 && TZ_JF_STAT != null && ("2".equals(TZ_JF_STAT) || "3".equals(TZ_JF_STAT))){
                            //更新
                            sqlQuery.update(updatePayPlanSql,new Object[]{TZ_JF_BZ_JE,TZ_JF_TZ_JE,payPlanMap.get("TZ_JF_DATE"),TZ_JF_TYPE,payPlanMap.get("TZ_REMARKS"),TZ_JFPL_ID});
                        }else {
                            //新增
                            TZ_JFPL_ID = "JF" + getNum(String.valueOf(getSeqNum.getSeqNum("TZ_JF_PLAN_T", "TZ_JFPL_ID")),8);
                            sqlQuery.update(insertPayPlanSql,new Object[]{TZ_JFPL_ID,objId,1,TZ_LXBH,TZ_REALNAME,payPlanMap.get("TZ_JF_TYPE"),payPlanMap.get("TZ_JF_DATE"),TZ_JF_BZ_JE,1,tzOprid,new Date(),TZ_JF_TZ_JE,payPlanMap.get("TZ_REMARKS"),0,0});
                        }

                    }
                }

            }
            /**=3= 重置已缴费计划中的 减免，应收，实收，缴费状态**/
            String updateJFSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_JM_JE = ? , TZ_JF_BQYS = ISNULL(TZ_JF_BZ_JE,0) - ISNULL(TZ_JF_TZ_JE,0) - ISNULL(TZ_JF_BQYT,0) ,TZ_JF_BQSS = ?  ,TZ_JF_STAT = ? WHERE TZ_OBJ_ID = ?";
            sqlQuery.update(updateJFSql,new Object[]{0,0,"1",objId});
        }catch (Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }
        return msg;
    }
    
    /**
     * 设置奖学金
     * 重构
     */
    public String saveScholarshipAPI(String objId, List<String> scholarshipIdList, String operation, String[] errorMsg,String remark,String explanation){
        String msg = "";
        try{
            //获取学生姓名
            String queryNameSql = "SELECT C.TZ_REALNAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T C WHERE A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
            String  TZ_REALNAME = sqlQuery.queryForObject(queryNameSql,new Object[]{objId},"String");
            TZ_REALNAME = TZ_REALNAME == null ? "" :TZ_REALNAME;
            /**=1=判断新的奖学金是否超出应收总额**/
            //查询所有应收总额（原有减免不参与计算，标准-调整-退费，仅计算缴费计划为学费的类型）
            String queryYSSql = "SELECT SUM(ISNULL(TZ_JF_BZ_JE,0) - ISNULL(TZ_JF_TZ_JE,0) - ISNULL(TZ_JF_BQYT,0)) AS YS FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_JF_TYPE = ?";
            String TZ_JF_BQYS = sqlQuery.queryForObject(queryYSSql, new Object[]{objId,"2"},"String");
            double TZ_JF_BQYS_DOUBLE = TZ_JF_BQYS == null ? 0d:Double.parseDouble(TZ_JF_BQYS);
            //获取新增奖学金的奖学金IdStrs
            StringBuffer jxjIdStrs = new StringBuffer();
            if(scholarshipIdList != null && scholarshipIdList.size() > 0 ){
                for (int i = 0; i < scholarshipIdList.size(); i++) {
                    String tzJxjId = scholarshipIdList.get(i);
                    if(tzJxjId != null && tzJxjId.length() > 0){
                        if(jxjIdStrs.length() == 0){
                            jxjIdStrs.append("'"+tzJxjId+"'");
                        }else {
                            jxjIdStrs.append(",'"+tzJxjId+"'");
                        }
                    }
                }
            }
            if(operation == null || !"single".equals(operation)){
                //新增奖学金需查询原有奖学金
                String queryHasJXJIdStrsSql = "SELECT STUFF((select ','''+TZ_JXJ_ID +'''' FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = ? for xml path('')),1,1,'')";
                String hasJXJIdStrs = sqlQuery.queryForObject(queryHasJXJIdStrsSql,new Object[]{objId},"String");
                if(jxjIdStrs.length() > 0){
                    jxjIdStrs.append(hasJXJIdStrs == null ? "" : ","+hasJXJIdStrs);
                }else {
                    jxjIdStrs.append(hasJXJIdStrs == null ? "" : hasJXJIdStrs);
                }
            }
            //获取奖学金总额
            double scholarshipMoney = 0d;
            if(jxjIdStrs.length()>0){
            	//学费标准
            	String classIDsql = "SELECT TZ_CLASS_ID from ps_tz_form_wrk_t WHERE TZ_APP_INS_ID = ?";
            	String classID = sqlQuery.queryForObject(classIDsql,new Object[]{Long.parseLong(objId)},"String");
        		String queryXFBZSql = "SELECT TZ_TUITION_STANDARD FROM ps_tz_class_inf_t WHERE TZ_CLASS_ID = ?";
        		String xfbz = sqlQuery.queryForObject(queryXFBZSql,new Object[]{classID},"String");
            	String queryJXJSql = "SELECT SUM ( CASE WHEN TZ_JXJ_LX = '1' THEN TZ_ZKL*CAST(? as decimal(9,2)) ELSE TZ_JXJ_JE END ) AS 'TZ_JXJ_JE' FROM TZ_JXJ_DEFN_T WHERE TZ_JXJ_ID IN ("+jxjIdStrs.toString()+")";
                String scholarshipStr = sqlQuery.queryForObject(queryJXJSql,new Object[]{xfbz},"String");
                scholarshipMoney = scholarshipStr == null ? 0d : Double.parseDouble(scholarshipStr);
            }
            if(scholarshipMoney > TZ_JF_BQYS_DOUBLE){
                msg = TZ_REALNAME + "奖学金超出缴费计划应收总金额！";
                return msg;
            }
            /**=2=进行考生奖学金关联**/
            String  insertSql = "INSERT INTO TZ_KS_JXJ_T(TZ_APP_INS_ID,TZ_JXJ_ID,TZ_REMARKS,TZ_EXPLANATION) VALUES(CONVERT(bigint,?),?,?,?)";
            String  updateSqlRemark = "UPDATE TZ_KS_JXJ_T SET TZ_REMARKS=? WHERE TZ_APP_INS_ID = CONVERT(bigint,?) AND TZ_JXJ_ID = ?";
            String  updateSqlExplanation = "UPDATE TZ_KS_JXJ_T SET TZ_EXPLANATION=? WHERE TZ_APP_INS_ID = CONVERT(bigint,?)";
            switch (operation){
                case "single":
                    //删除历史奖学金关系,重新新增
                    String deleteSql = "DELETE FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = ?";
                    sqlQuery.update(deleteSql,new Object[]{objId});
                    if(scholarshipIdList != null && scholarshipIdList.size()>0){
                        for (String scholarshipId:scholarshipIdList) {
                            sqlQuery.update(insertSql,new Object[]{objId,scholarshipId,remark,explanation});
                        }
                    }
                    break;
                case "batch":
                default:
                    //遍历奖学金，有则更新，无则新增
                    String queryScholarshipSql = "SELECT 'Y' FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = CONVERT(bigint,?) AND TZ_JXJ_ID = ?";
                    if(scholarshipIdList != null && scholarshipIdList.size()>0){
                        for (String scholarshipId:scholarshipIdList) {
                            String flag = sqlQuery.queryForObject(queryScholarshipSql,new Object[]{objId,scholarshipId},"String");
                            if(flag == null || !"Y".equals(flag)){
                                sqlQuery.update(insertSql,new Object[]{objId,scholarshipId,remark,explanation});
                            }else{
                            	sqlQuery.update(updateSqlRemark,new Object[]{remark,objId,scholarshipId});
                            }
                            sqlQuery.update(updateSqlExplanation,new Object[]{explanation,objId});
                        }
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return msg;
    }

    /**
     * 设置奖学金
     * 此方法暂不进行奖学金刷新
     * 此方法会校验奖学金总和是否超出缴费计划总应收金额，若超出则不进行新增或重置（重要）
     * @param objId
     * @param scholarshipIdList
     * @param operation  设置奖学金增加方式 single：单个设置（属性为重置）；batch : 批量设置（属性为新增）
     *                 默认为：batch
     * @param errorMsg
     * @return
     */
    public String saveScholarshipAPI(String objId, List<String> scholarshipIdList, String operation, String[] errorMsg){
        String msg = "";
        try{
            //获取学生姓名
            String queryNameSql = "SELECT C.TZ_REALNAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T C WHERE A.OPRID = C.OPRID AND A.TZ_APP_INS_ID = ?";
            String  TZ_REALNAME = sqlQuery.queryForObject(queryNameSql,new Object[]{objId},"String");
            TZ_REALNAME = TZ_REALNAME == null ? "" :TZ_REALNAME;
            /**=1=判断新的奖学金是否超出应收总额**/
            //查询所有应收总额（原有减免不参与计算，标准-调整-退费，仅计算缴费计划为学费的类型）
            String queryYSSql = "SELECT SUM(ISNULL(TZ_JF_BZ_JE,0) - ISNULL(TZ_JF_TZ_JE,0) - ISNULL(TZ_JF_BQYT,0)) AS YS FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_JF_TYPE = ?";
            String TZ_JF_BQYS = sqlQuery.queryForObject(queryYSSql, new Object[]{objId,"2"},"String");
            double TZ_JF_BQYS_DOUBLE = TZ_JF_BQYS == null ? 0d:Double.parseDouble(TZ_JF_BQYS);
            //获取新增奖学金的奖学金IdStrs
            StringBuffer jxjIdStrs = new StringBuffer();
            if(scholarshipIdList != null && scholarshipIdList.size() > 0 ){
                for (int i = 0; i < scholarshipIdList.size(); i++) {
                    String tzJxjId = scholarshipIdList.get(i);
                    if(tzJxjId != null && tzJxjId.length() > 0){
                        if(jxjIdStrs.length() == 0){
                            jxjIdStrs.append("'"+tzJxjId+"'");
                        }else {
                            jxjIdStrs.append(",'"+tzJxjId+"'");
                        }
                    }
                }
            }
            if(operation == null || !"single".equals(operation)){
                //新增奖学金需查询原有奖学金
                String queryHasJXJIdStrsSql = "SELECT STUFF((select ','''+TZ_JXJ_ID +'''' FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = ? for xml path('')),1,1,'')";
                String hasJXJIdStrs = sqlQuery.queryForObject(queryHasJXJIdStrsSql,new Object[]{objId},"String");
                if(jxjIdStrs.length() > 0){
                    jxjIdStrs.append(hasJXJIdStrs == null ? "" : ","+hasJXJIdStrs);
                }else {
                    jxjIdStrs.append(hasJXJIdStrs == null ? "" : hasJXJIdStrs);
                }
            }
            //获取奖学金总额
            double scholarshipMoney = 0d;
            if(jxjIdStrs.length()>0){
            	//学费标准
            	String classIDsql = "SELECT TZ_CLASS_ID from ps_tz_form_wrk_t WHERE TZ_APP_INS_ID = ?";
            	String classID = sqlQuery.queryForObject(classIDsql,new Object[]{Long.parseLong(objId)},"String");
        		String queryXFBZSql = "SELECT TZ_TUITION_STANDARD FROM ps_tz_class_inf_t WHERE TZ_CLASS_ID = ?";
        		String xfbz = sqlQuery.queryForObject(queryXFBZSql,new Object[]{classID},"String");
            	String queryJXJSql = "SELECT SUM ( CASE WHEN TZ_JXJ_LX = '1' THEN TZ_ZKL*CAST(? as decimal(9,2)) ELSE TZ_JXJ_JE END ) AS 'TZ_JXJ_JE' FROM TZ_JXJ_DEFN_T WHERE TZ_JXJ_ID IN ("+jxjIdStrs.toString()+")";
                String scholarshipStr = sqlQuery.queryForObject(queryJXJSql,new Object[]{xfbz},"String");
                scholarshipMoney = scholarshipStr == null ? 0d : Double.parseDouble(scholarshipStr);
            }
            if(scholarshipMoney > TZ_JF_BQYS_DOUBLE){
                msg = TZ_REALNAME + "奖学金超出缴费计划应收总金额！";
                return msg;
            }
            /**=2=进行考生奖学金关联**/
            String  insertSql = "INSERT INTO TZ_KS_JXJ_T(TZ_APP_INS_ID,TZ_JXJ_ID) VALUES(CONVERT(bigint,?),?)";
            switch (operation){
                case "single":
                    //删除历史奖学金关系,重新新增
                    String deleteSql = "DELETE FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = ?";
                    sqlQuery.update(deleteSql,new Object[]{objId});
                    if(scholarshipIdList != null && scholarshipIdList.size()>0){
                        for (String scholarshipId:scholarshipIdList) {
                            sqlQuery.update(insertSql,new Object[]{objId,scholarshipId});
                        }
                    }
                    break;
                case "batch":
                default:
                    //遍历奖学金，有则不变，无则新增
                    String queryScholarshipSql = "SELECT 'Y' FROM TZ_KS_JXJ_T WHERE TZ_APP_INS_ID = CONVERT(bigint,?) AND TZ_JXJ_ID = ?";
                    if(scholarshipIdList != null && scholarshipIdList.size()>0){
                        for (String scholarshipId:scholarshipIdList) {
                            String flag = sqlQuery.queryForObject(queryScholarshipSql,new Object[]{objId,scholarshipId},"String");
                            if(flag == null || !"Y".equals(flag)){
                                sqlQuery.update(insertSql,new Object[]{objId,scholarshipId});
                            }
                        }
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return msg;
    }

    /**
     * 此方法刷新缴费计划（减免，应收，实收，状态）
     * 计算奖学金减免更新应收
     * 计算到账更新实收及状态
     * @param objId
     * @param errorMsg
     * @return
     */
    public String refreshPayPlanAPI(String objId, String[] errorMsg){
        try{
            /**
             * =1= 更新所有缴费计划的 减免，应收（依据标准，调整，退费，奖学金计算）
             **/
            //查询奖学金总额
        	String classIDsql = "SELECT TZ_CLASS_ID from ps_tz_form_wrk_t WHERE TZ_APP_INS_ID = ?";
        	String classID = sqlQuery.queryForObject(classIDsql,new Object[]{Long.parseLong(objId)},"String");
        	String queryXFBZSql = "SELECT TZ_TUITION_STANDARD FROM ps_tz_class_inf_t WHERE TZ_CLASS_ID = ?";
    		String xfbz = sqlQuery.queryForObject(queryXFBZSql,new Object[]{classID},"String");
            String queryscholarshipSql = "SELECT SUM ( CASE WHEN TZ_JXJ_LX = '1' THEN TZ_ZKL*? ELSE TZ_JXJ_JE END ) AS 'TZ_JXJ_JE' FROM TZ_JXJ_DEFN_T A LEFT JOIN TZ_KS_JXJ_T B ON A.TZ_JXJ_ID = B.TZ_JXJ_ID WHERE CAST ( B.TZ_APP_INS_ID AS VARCHAR (15)) = ?";
            String scholarshipMoneyStr =  sqlQuery.queryForObject(queryscholarshipSql,new Object[]{xfbz,objId},"String");
            double scholarshipMoney = scholarshipMoneyStr == null ? 0d : Double.parseDouble(scholarshipMoneyStr);
            if(scholarshipMoney > 0d){
                //获取缴费计划（费用类型为学费 可进行奖学金减免）
                String queryPayPlanSql = "SELECT TZ_JFPL_ID,(ISNULL(TZ_JF_BZ_JE,0) - ISNULL(TZ_JF_TZ_JE,0) - ISNULL(TZ_JF_BQYT,0)) AS TZ_JF_BQYS  FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_JF_TYPE = ?";
                List<Map<String,Object>> payPlanList = sqlQuery.queryForList(queryPayPlanSql,new Object[]{objId,"2"});
                String updatePayPlanJMSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_JM_JE = ?,TZ_JF_BQYS = ? WHERE TZ_JFPL_ID = ?";
                if(payPlanList!=null && payPlanList.size() > 0){
                    for (int i = 0; i < payPlanList.size(); i++) {
                        Map<String,Object> payPlanMap = payPlanList.get(i);
                        String TZ_JFPL_ID = payPlanMap.get("TZ_JFPL_ID") == null ? "": String.valueOf(payPlanMap.get("TZ_JFPL_ID"));
                        double TZ_JF_BQYS = payPlanMap.get("TZ_JF_BQYS") == null ? 0d: Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_BQYS")));
                        if(scholarshipMoney <= TZ_JF_BQYS){
                            sqlQuery.update(updatePayPlanJMSql,new Object[]{scholarshipMoney,TZ_JF_BQYS-scholarshipMoney,TZ_JFPL_ID});
                        }else {
                            sqlQuery.update(updatePayPlanJMSql,new Object[]{TZ_JF_BQYS,0,TZ_JFPL_ID});
                        }
                        scholarshipMoney = scholarshipMoney - TZ_JF_BQYS;
                        if(scholarshipMoney <= 0d){
                            //奖学金用完跳出循环
                            break;
                        }

                    }
                }
            }
            //此处刷新所有缴费计划应收
            String updatePayPlanSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_BQYS = ISNULL(TZ_JF_BZ_JE,0) - ISNULL(TZ_JF_TZ_JE,0) - ISNULL(TZ_JF_JM_JE,0) - ISNULL(TZ_JF_BQYT,0) ,TZ_JF_STAT = ? WHERE TZ_OBJ_ID = ?";
            sqlQuery.update(updatePayPlanSql,new Object[]{"1",objId});

            /**
             * =2= 更新所有缴费计划的 实收，缴费状态（依据应收，到账金额计算）缴费计划费用类型不限
             **/
            //查询所有到账总金额
            String queryBillMoneySql = "SELECT SUM(ISNULL(TZ_DZ_JIE,0)) AS TZ_DZ_JIE FROM TZ_DZJL_TBL WHERE TZ_OBJ_ID = ?";
            String billMoneyStr = sqlQuery.queryForObject(queryBillMoneySql,new Object[]{objId},"String");
            double billMoney = billMoneyStr == null ? 0d : Double.parseDouble(billMoneyStr);
            if(billMoney > 0d){
                //查询所有缴费计划（仅关注缴费计划应收计算实收）
                String queryPayPlanDZSql = "SELECT TZ_JFPL_ID,ISNULL(TZ_JF_BQYS,0) AS TZ_JF_BQYS  FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ?  ORDER BY TZ_JF_DATE,TZ_JF_TYPE";
                List<Map<String,Object>> payPlanDZList = sqlQuery.queryForList(queryPayPlanDZSql,new Object[]{objId});
                String updatePayPlanDZSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_BQSS = ?,TZ_JF_STAT = ? WHERE TZ_JFPL_ID = ?";
                for (Map<String,Object> payPlanMap:payPlanDZList) {
                    if(payPlanMap!=null){
                        String TZ_JFPL_ID = payPlanMap.get("TZ_JFPL_ID") == null ? "": String.valueOf(payPlanMap.get("TZ_JFPL_ID"));
                        double TZ_JF_BQYS = payPlanMap.get("TZ_JF_BQYS") == null ? 0d: Double.parseDouble(String.valueOf(payPlanMap.get("TZ_JF_BQYS")));
                        if(billMoney < TZ_JF_BQYS){
                            //到账剩余总额小于单条应收
                            sqlQuery.update(updatePayPlanDZSql,new Object[]{billMoney,"2",TZ_JFPL_ID});
                        }else if(billMoney == TZ_JF_BQYS){
                            //到账剩余总额等于单条应收
                            sqlQuery.update(updatePayPlanDZSql,new Object[]{billMoney,"3",TZ_JFPL_ID});
                        }else{
                            //到账剩余总额大于单条应收
                            sqlQuery.update(updatePayPlanDZSql,new Object[]{TZ_JF_BQYS,"3",TZ_JFPL_ID});
                        }
                        billMoney = billMoney - TZ_JF_BQYS;
                        if(billMoney <= 0d){
                            //到账金额使用完成退出循环
                            break;
                        }
                    }
                }
            }
            /**
             * =3= 更新学生总缴费状态
             **/
            String JFQuerySql = "SELECT SUM(CASE  WHEN TZ_JF_STAT = '1' OR TZ_JF_STAT IS NULL OR TZ_JF_STAT = '' THEN 1 ELSE 0 END) AS UnpaidFees, SUM(CASE TZ_JF_STAT WHEN '2' THEN 1 ELSE 0 END) AS PartialPayment , SUM(CASE TZ_JF_STAT WHEN '3' THEN 1 ELSE 0 END) AS Paymented, SUM(1) AS PayCount FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ?";
            Map<String,Object> jfMap = sqlQuery.queryForMap(JFQuerySql,new Object[]{objId});
            if (jfMap!=null){
                String UnpaidFees = String.valueOf(jfMap.get("UnpaidFees"));
                String PartialPayment = String.valueOf(jfMap.get("PartialPayment"));
                String Paymented = String.valueOf(jfMap.get("Paymented"));
                String PayCount = String.valueOf(jfMap.get("PayCount"));
                String TZ_JFZT = "";
                if(Paymented.equals(PayCount)) {
                    TZ_JFZT = "2";//已缴费
                }else if(UnpaidFees.equals(PayCount)) {
                    TZ_JFZT = "0";//未缴费
                }else {
                    TZ_JFZT = "1";//部分缴费
                }
                String updateJFSql = "UPDATE PS_TZ_FORM_WRK_T SET TZ_JFZT = ? WHERE TZ_APP_INS_ID = ?";
                sqlQuery.update(updateJFSql,new Object[]{TZ_JFZT,objId});
            }
        }catch (Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }
        return "";
    }
}
