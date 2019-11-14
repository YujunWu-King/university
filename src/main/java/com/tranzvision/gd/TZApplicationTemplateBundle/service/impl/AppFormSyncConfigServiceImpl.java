package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/7/3 11:48
 * @Description:  报名报同步配置
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormSyncConfigServiceImpl")
public class AppFormSyncConfigServiceImpl extends FrameworkImpl {

    @Autowired
    private FliterForm fliterForm;
    @Autowired
    private GetSeqNum getSeqNum;
    @Autowired
    private SqlQuery jdbcTemplate;

    /* 查询项目分类列表 */
    @Override
    public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        mapRet.put("root", listData);
        JacksonUtil jacksonUtil = new JacksonUtil();

        // 排序字段如果没有不要赋值
        String[][] orderByArr = new String[][] { { "TZ_APPSYNC_ID", "ASC" } };

        // json数据要的结果字段;
        String[] resultFldArray = { "TZ_APPSYNC_ID", "TZ_APPSYNC_DESC", "TZ_SYNC_TABLE", "TZ_SYNC_FILED" , "TZ_IS_ENABLE" };

        // 可配置搜索通用函数;
        Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

        if (obj != null && obj.length > 0) {
            ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
            for (int i = 0; i < list.size(); i++) {
                String[] rowList = list.get(i);
                Map<String, Object> mapList = new HashMap<String, Object>();
                mapList.put("TZ_APPSYNC_ID", rowList[0]);
                mapList.put("TZ_APPSYNC_DESC", rowList[1]);
                mapList.put("TZ_SYNC_TABLE", rowList[2]);
                mapList.put("TZ_SYNC_FILED", rowList[3]);
                mapList.put("TZ_IS_ENABLE", rowList[4]);
                listData.add(mapList);
            }
            mapRet.replace("total", obj[0]);
            mapRet.replace("root", listData);
        }

        return jacksonUtil.Map2json(mapRet);
    }

    @Override
    public String tzQuery(String strParams, String[] errMsg) {
        // 返回值;
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);

            if (jacksonUtil.containsKey("TZ_APPSYNC_ID")) {
                // 项目分类编号;
                String TZ_APPSYNC_ID = jacksonUtil.getString("TZ_APPSYNC_ID");
                String querySql = "SELECT TZ_APPSYNC_ID,TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE FROM PS_TZ_APPSYNC_CONFIG WHERE TZ_APPSYNC_ID = ?";
                Map<String,Object> map = jdbcTemplate.queryForMap(querySql,new Object[]{TZ_APPSYNC_ID});
                if (map != null) {
                    returnJsonMap.putAll(map);
                } else {
                    errMsg[0] = "1";
                    errMsg[1] = "该同步配置数据不存在";
                }

            } else {
                errMsg[0] = "1";
                errMsg[1] = "该同步配置数据不存在";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        return jacksonUtil.Map2json(returnJsonMap);
    }

    // 新增同步配置定义;
    @Override
    public String tzAdd(String[] actData, String[] errMsg) {
        String strRet = "{}";
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("TZ_APPSYNC_ID", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            int num = 0;
            for (num = 0; num < actData.length; num++) {
                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);
                // 信息内容;
                Map<String, Object> infoData = jacksonUtil.getMap("data");
                // 同步设置编号;
                String TZ_APPSYNC_ID = String.valueOf(infoData.get("TZ_APPSYNC_ID"));
                // 同步设置名称;
                String TZ_APPSYNC_DESC = (String) infoData.get("TZ_APPSYNC_DESC");
                // 同步表;
                String TZ_SYNC_TABLE = (String) infoData.get("TZ_SYNC_TABLE");
                // 同步字段;
                String TZ_SYNC_FILED = (String) infoData.get("TZ_SYNC_FILED");
                //同步有效状态
                String TZ_IS_ENABLE = (String) infoData.get("TZ_IS_ENABLE");



                if ("NEXT".equals(TZ_APPSYNC_ID)) {
                    TZ_APPSYNC_ID = "APPSYNC" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_APPSYNC_CONFIG", "TZ_APPSYNC_ID"));
                    String insertSql = "INSERT INTO PS_TZ_APPSYNC_CONFIG(TZ_APPSYNC_ID,TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE) VALUES(?,?,?,?,?) ";
                    jdbcTemplate.update(insertSql,new Object[]{TZ_APPSYNC_ID,TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE});

                } else{
                    String updateSql = "UPDATE PS_TZ_APPSYNC_CONFIG SET TZ_APPSYNC_DESC = ?,TZ_SYNC_TABLE = ?,TZ_SYNC_FILED = ?,TZ_IS_ENABLE = ? WHERE TZ_APPSYNC_ID = ?";
                    jdbcTemplate.update(updateSql,new Object[]{TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE,TZ_APPSYNC_ID});
                }

                returnJsonMap.put("TZ_APPSYNC_ID", TZ_APPSYNC_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        strRet = jacksonUtil.Map2json(returnJsonMap);
        return strRet;
    }

    // 更新同步配置类定义;
    @Override
    public String tzUpdate(String[] actData, String[] errMsg) {
        String strRet = "{}";
        Map<String, Object> returnJsonMap = new HashMap<String, Object>();
        returnJsonMap.put("TZ_APPSYNC_ID", "");
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            int num = 0;
            for (num = 0; num < actData.length; num++) {
                // 表单内容;
                String strForm = actData[num];
                // 将字符串转换成json;
                jacksonUtil.json2Map(strForm);
                // 信息内容;
                Map<String, Object> infoData = jacksonUtil.getMap("data");
                // 同步设置编号;
                String TZ_APPSYNC_ID = String.valueOf(infoData.get("TZ_APPSYNC_ID"));
                // 同步设置名称;
                String TZ_APPSYNC_DESC = (String) infoData.get("TZ_APPSYNC_DESC");
                // 同步表;
                String TZ_SYNC_TABLE = (String) infoData.get("TZ_SYNC_TABLE");
                // 同步字段;
                String TZ_SYNC_FILED = (String) infoData.get("TZ_SYNC_FILED");
                //同步有效状态
                String TZ_IS_ENABLE = (String) infoData.get("TZ_IS_ENABLE");

                if ("NEXT".equals(TZ_APPSYNC_ID)) {
                    TZ_APPSYNC_ID = "APPSYNC" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_APPSYNC_CONFIG", "TZ_APPSYNC_ID"));
                    String insertSql = "INSERT INTO PS_TZ_APPSYNC_CONFIG(TZ_APPSYNC_ID,TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE) VALUES(?,?,?,?,?) ";
                    jdbcTemplate.update(insertSql,new Object[]{TZ_APPSYNC_ID,TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE});

                } else{
                    String updateSql = "UPDATE PS_TZ_APPSYNC_CONFIG SET TZ_APPSYNC_DESC = ?,TZ_SYNC_TABLE = ?,TZ_SYNC_FILED = ?,TZ_IS_ENABLE = ? WHERE TZ_APPSYNC_ID = ?";
                    jdbcTemplate.update(updateSql,new Object[]{TZ_APPSYNC_DESC,TZ_SYNC_TABLE,TZ_SYNC_FILED,TZ_IS_ENABLE,TZ_APPSYNC_ID});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }
        strRet = jacksonUtil.Map2json(returnJsonMap);
        return strRet;
    }

    /**
     * 删除报名同步配置
     */
    @Override
    @Transactional
    public String tzDelete(String[] actData, String[] errMsg) {
        String strRet = "{}";
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {

            int dataLength = actData.length;
            for (int num = 0; num < dataLength; num++) {
                // 表单内容
                String strForm = actData[num];
                // 解析json
                jacksonUtil.json2Map(strForm);

                // 配置编号;
                String TZ_APPSYNC_ID = jacksonUtil.getString("TZ_APPSYNC_ID");

                if (TZ_APPSYNC_ID != null && !"".equals(TZ_APPSYNC_ID)) {
                    Object[] args = new Object[] { TZ_APPSYNC_ID };
                    jdbcTemplate.update("DELETE FROM PS_TZ_APPSYNC_CONFIG WHERE TZ_APPSYNC_ID = ?", args);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = e.toString();
        }

        return strRet;
    }

}

