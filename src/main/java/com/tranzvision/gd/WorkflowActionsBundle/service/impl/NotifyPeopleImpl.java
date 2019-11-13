package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: ZY
 * @Date: 2019/1/17 17:23
 * @Description:   知会人逻辑处理
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.NotifyPeopleImpl")
public class NotifyPeopleImpl extends FrameworkImpl {

    @Autowired
    private SqlQuery jdbcTemplate;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TZGDObject tzgdObject;


    /**
     * 查询知会人列表
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
                String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
                listData = this.queryNotifyPeopleList(tzms_wflinsid,tzms_stpinsid);
                mapRet.put("root", listData);
                mapRet.put("total", listData.size());
                return jacksonUtil.Map2json(mapRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return jacksonUtil.Map2json(mapRet);
    }




    @Override
    public String tzOther(String oprType, String strParams, String[] errorMsg) {
        String strRet = "";
        try{
            //当前登录的机构;
            //String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
            if("addNotifyPeople".equals(oprType)){
                strRet = this.addNotifyPeople(strParams,errorMsg);
            }else if("cancelNotifyPeople".equals(oprType)){
                strRet = this.cancelNotifyPeople(strParams,errorMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return strRet;
    }


    /**
     * 新增知会人
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String addNotifyPeople(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            /**
             * 加签人字符串（","隔开）
             */
            String tzms_stpproidStr = jacksonUtil.getString("tzms_stpproid");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)&&tzms_stpproidStr!=null&&!"".equals(tzms_stpproidStr)) {
                String[] tzms_stpproids = tzms_stpproidStr.split(",");
                List<Map<String,Object>> list = this.queryNotifyPeopleList(tzms_wflinsid,tzms_stpinsid);
                String selectSql = "Select 'Y' from tzms_not_psn_tbl where  tzms_stpinsid = ? and tzms_stpproid = ?";
                String insertSql = "insert into tzms_not_psn_tbl(tzms_stpinsid,tzms_stpproid,tzms_asign_dtime) values(?,?,?)";
                for (int i = 0; i < tzms_stpproids.length; i++) {
                    String flag = jdbcTemplate.queryForObject(selectSql,new Object[]{tzms_stpinsid,tzms_stpproids[i]},"String");
                    if(flag!=null&&!"".equals(flag)&&"Y".equals(flag)){
                        //mapRet.put("ret",-1);
                        //mapRet.put("msg","知会人已存在");
                    }else{
                        jdbcTemplate.update(insertSql,new Object[]{tzms_stpinsid,tzms_stpproids[i],new Date()});
                    }
                }
                List<Map<String,Object>> listNew = this.queryNotifyPeopleList(tzms_wflinsid,tzms_stpinsid);
                mapRet.put("ret",0);
                mapRet.put("msg","success");
                mapRet.put("root", listNew);
                mapRet.put("total", listNew.size());
                return jacksonUtil.Map2json(mapRet);

            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            return "";
        }

        return "";
    }


    /**
     * 删除知会人
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String cancelNotifyPeople(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            String fields = "";

            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            /**
             * 加签人字符串（","隔开）
             */
            String tzms_stpproidStr = jacksonUtil.getString("tzms_stpproid");

            if(tzms_wflinsid!=null&&!"".equals(tzms_wflinsid)&&tzms_stpinsid!=null&&!"".equals(tzms_stpinsid)&&tzms_stpproidStr!=null&&!"".equals(tzms_stpproidStr)) {
                String[] tzms_stpproids = tzms_stpproidStr.split(",");

                String deleteSql = "delete from tzms_not_psn_tbl where tzms_stpproid = ?";
                for (int i = 0; i < tzms_stpproids.length; i++) {
                    jdbcTemplate.update(deleteSql,new Object[]{tzms_stpproids[i]});
                }

                List<Map<String,Object>> listNew = this.queryNotifyPeopleList(tzms_wflinsid,tzms_stpinsid);
                mapRet.put("root", listNew);
                mapRet.put("total", listNew.size());
                return jacksonUtil.Map2json(mapRet);

            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
            return "";
        }

        return "";
    }

    /**
     * 查询知会人list
     * @param wflinsId
     * @param stpinsId
     * @return
     */
    public List<Map<String,Object>> queryNotifyPeopleList(String wflinsId,String stpinsId){
        List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
        try {
            String queryUserIdSql = "Select tzms_stpproid,tzms_asign_stpinsid from tzms_not_psn_tbl where tzms_stpinsid = ?";
            String queryNameSql1 = "SELECT TOP 1 tzms_domain_zhid,tzms_staff_id,tzms_name FROM tzms_tea_defn_tBase WHERE tzms_user_uniqueid = ?";
            String queryNameSql2 = "Select TOP 1 C.tzms_org_name from tzms_tea_defn_tBase A,tzms_org_user_tBase B,tzms_org_structure_tree_tbase C where A.tzms_tea_defn_tid = B.tzms_tea_defnid AND B.tzms_org_uniqueid = C.tzms_org_structure_tree_tid AND A.tzms_user_uniqueid = ?";
            List<Map<String,Object>> list = jdbcTemplate.queryForList(queryUserIdSql,new Object[]{stpinsId});
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> map = list.get(i);
                String tzms_stpproid = map.get("tzms_stpproid")==null?"": String.valueOf(map.get("tzms_stpproid"));
                if(!"".equals(tzms_stpproid)){
                    Map<String,Object> map2 = jdbcTemplate.queryForMap(queryNameSql1,new Object[]{tzms_stpproid});
                    if(map2!=null){
                        String tzms_name = map2.get("tzms_name")==null ? "": String.valueOf(map2.get("tzms_name"));
                        String tzms_staff_id = map2.get("tzms_staff_id")==null ? "": String.valueOf(map2.get("tzms_staff_id"));
                        String tzms_domain_zhid = map2.get("tzms_domain_zhid")==null ? "": String.valueOf(map2.get("tzms_domain_zhid"));
                        String tzms_org_name = jdbcTemplate.queryForObject(queryNameSql2,new Object[]{tzms_stpproid},"String");
                        map.put("tzms_domain_zhid",tzms_domain_zhid);
                        map.put("tzms_staff_id",tzms_staff_id);
                        map.put("tzms_name",tzms_name);
                        map.put("tzms_org_name",tzms_org_name == null ? "":tzms_org_name);
                        listData.add(map);
                    }
                }
            }
            //String querySql = tzgdObject.getSQLText("SQL.WorkflowActionsBundle.TzGetNotifyPeopleList");
            //list = jdbcTemplate.queryForList(querySql,new Object[]{stpinsId});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;

    }
}
