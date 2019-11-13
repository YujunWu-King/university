package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/1/22 16:34
 * @Description: 工作流 查看历史处理意见
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.HistoryOpinionImpl")
public class HistoryOpinionImpl extends FrameworkImpl {
    @Autowired
    private SqlQuery jdbcTemplate;


    /**
     * 查询历史处理意见列表
     * @param strParams
     * @param numLimit
     * @param numStart
     * @param errorMsg
     * @return
     */
    @Override
    public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("total", 0);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        mapRet.put("root", listData);
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            if(jacksonUtil.containsKey("tzms_wflinsid")&&jacksonUtil.containsKey("tzms_stpinsid")){
                //获取步骤实例Id
                // 实例ID;
                String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
                // 步骤实例id;
                //String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
                String querySql = "SELECT tzms_stpinsid,(select tzms_wflstpname from tzms_wflstp_tBase where tzms_wflstp_tId=A.tzms_wflstpid) as tzms_wflstpname,tzms_abstract,tzms_stpproid,(SELECT top 1 tzms_name FROM tzms_tea_defn_tBase " + 
                		"WHERE convert(varchar(36),tzms_user_uniqueid) = (case  when A.tzms_stpproid is null then '' ELSE A.tzms_stpproid end)) tzms_name,CONVERT(varchar(100), tzms_tskprodt, 20) tzms_tskprodt,tzms_tskprorec from tzms_stpins_tbl A where tzms_wflinsid=? order by tzms_stpstartdt,tzms_tskprodt";
                listData = jdbcTemplate.queryForList(querySql,new Object[]{tzms_wflinsid});
                mapRet.put("root", listData);
                mapRet.put("total", listData.size());
                return jacksonUtil.Map2json(mapRet);
            }
        } catch (Exception e) {
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return jacksonUtil.Map2json(mapRet);
    }
}
