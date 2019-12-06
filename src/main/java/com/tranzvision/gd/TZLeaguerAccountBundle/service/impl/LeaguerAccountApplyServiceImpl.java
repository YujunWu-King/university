package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/6/21 09:38
 * @Description: 报名表维度查看用户
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountApplyServiceImpl")
public class LeaguerAccountApplyServiceImpl extends FrameworkImpl {
    @Autowired
    private FliterForm fliterForm;
    @Autowired
    private SqlQuery SqlQuery;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TzLoginServiceImpl tzLoginServiceImpl;
    @Autowired
    private PsoprdefnMapper psoprdefnMapper;
    @Autowired
    private com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper PsTzRegUserTMapper;
    @Autowired
    private TZGDObject tzGdObject;

    @Override
    public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        mapRet.put("root", "[]");

        ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            // 排序字段如果没有不要赋值
            String[][] orderByArr = new String[][] {new String[]{"TZ_APP_SUB_DTTM","DESC"}};

            // json数据要的结果字段;
            String[] resultFldArray = { "TZ_APP_INS_ID","TZ_REALNAME" , "TZ_MSH_ID" , "TZ_CLASS_NAME" , "TZ_BATCH_NAME" , "TZ_FILL_PROPORTION" ,
                    "TZ_APP_FORM_STA" ,"TZ_APP_SUB_DTTM","TZ_MOBILE","TZ_EMAIL","MSZG","MSJG","OPRID","TZ_JG_ID"};


            // 可配置搜索通用函数;
            Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

            if (obj != null && obj.length > 0) {

                ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
                String appInsId="(";
                for (int i = 0; i < list.size(); i++) {
                    String[] rowList = list.get(i);

                    Map<String, Object> mapList = new HashMap<String, Object>();
                    mapList.put("TZ_APP_INS_ID", rowList[0]);
                    if(i==(list.size()-1)){
                        appInsId=appInsId+"'"+rowList[0]+"'";
                    }else{
                        appInsId=appInsId+"'"+rowList[0]+"'"+",";
                    }
                    mapList.put("TZ_REALNAME", rowList[1]);
                    mapList.put("TZ_MSH_ID", rowList[2]);
                    mapList.put("TZ_CLASS_NAME", rowList[3]);
                    mapList.put("TZ_BATCH_NAME", rowList[4]);
                    rowList[5]=rowList[5]+"%";
                    mapList.put("TZ_FILL_PROPORTION", rowList[5]);
                    mapList.put("TZ_APP_FORM_STA", rowList[6]);
                    mapList.put("TZ_APP_SUB_DTTM", rowList[7]);
                    mapList.put("TZ_MOBILE", rowList[8]);
                    mapList.put("TZ_EMAIL", rowList[9]);
                    mapList.put("TZ_MSZG", rowList[10]);
                    mapList.put("TZ_MSJG", rowList[11]);
                    mapList.put("OPRID", rowList[12]);
                    mapList.put("TZ_JG_ID", rowList[13]);
                    listData.add(mapList);
                }
                appInsId=appInsId+")";
                System.out.println("appInsIdStr=====>"+appInsId);
                if(!"()".equals(appInsId)){
                    String sql="select TZ_APP_INS_ID,count(*) TJX_NUM from PS_TZ_KS_TJX_TBL " +
                            "where TZ_TJX_APP_INS_ID in (select TZ_APP_INS_ID from PS_TZ_APP_INS_T  where TZ_APP_FORM_STA='U') " +
                            "and TZ_APP_INS_ID in "+appInsId+" group by TZ_APP_INS_ID";
                    System.out.println("sql=====>"+sql);
                    List<Map<String, Object>> tjxMapList = SqlQuery.queryForList(sql);
                    if(tjxMapList.size()>0){
                        for(int i=0;i<tjxMapList.size();i++){
                            String appId=tjxMapList.get(i).get("TZ_APP_INS_ID").toString();
                            String txjNum=tjxMapList.get(i).get("TJX_NUM")==null?"0":tjxMapList.get(i).get("TJX_NUM").toString();
                            for(int j=0;j<listData.size();j++){
                                if(listData.get(j).get("TZ_APP_INS_ID").toString().equals(appId)){
                                    listData.get(j).put("TZ_TJX_NUM",txjNum);
                                    break;
                                }
                            }
                        }
                    }else{
                        System.out.println("tjxMapList.size()<=0");
                        for(int j=0;j<listData.size();j++){
                            listData.get(j).put("TZ_TJX_NUM","0");
                        }
                    }
                }
                mapRet.replace("total", obj[0]);
                mapRet.replace("root", listData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jacksonUtil.Map2json(mapRet);

    }
}
