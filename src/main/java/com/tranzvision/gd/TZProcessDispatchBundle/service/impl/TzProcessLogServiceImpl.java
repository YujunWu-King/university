package com.tranzvision.gd.TZProcessDispatchBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangDi on 2017/4/7.
 */
@Service("com.tranzvision.gd.TZProcessDispatchBundle.service.impl.TzProcessLogServiceImpl")
public class TzProcessLogServiceImpl extends FrameworkImpl{

    @Autowired
    private FliterForm fliterForm;

    @Autowired
    private SqlQuery jdbcTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        mapRet.put("root", listData);
        JacksonUtil jacksonUtil = new JacksonUtil();

        try {

            // 将字符串转换成json;
            jacksonUtil.json2Map(strParams);

            String processInstanceId = jacksonUtil.getString("processInstanceId");
            if(processInstanceId != null && !"".equals(processInstanceId)){

                int total = 0;
                String totalSQL = "select COUNT(1) from TZ_JCYX_LOG_T WHERE TZ_JCSL_ID=?";
                total = jdbcTemplate.queryForObject(totalSQL,new Object[]{processInstanceId}, "Integer");
                String sql = "select TZ_RZ_JB,TZ_RZ_DTTM,TZ_RZ_NR from TZ_JCYX_LOG_T  WHERE TZ_JCSL_ID=?";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { processInstanceId});

                if(list != null){
                    for(int i = 0; i < list.size(); i++){
                        String logLevel = list.get(i).get("TZ_RZ_JB") == null?"":list.get(i).get("TZ_RZ_JB").toString();
                        String dateTime = list.get(i).get("TZ_RZ_DTTM") == null?"":list.get(i).get("TZ_RZ_DTTM").toString();
                        String logDetail = list.get(i).get("TZ_RZ_NR") == null?"":list.get(i).get("TZ_RZ_NR").toString();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("orderNum", i + 1);
                        map.put("logLevel", logLevel);
                        map.put("dateTime", dateTime);
                        map.put("logDetail", logDetail);
                        listData.add(map);
                    }
                    mapRet.replace("total", total);
                    mapRet.replace("root", listData);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return jacksonUtil.Map2json(mapRet);

    }
}
