package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: ZY
 * @Date: 2019/1/17 17:23
 * @Description:   加签逻辑处理
 *
 * 若步骤实例状态值为：处理中，未签收，已签收。则可以更新步骤实例签核类型，签核流转
 */
@Service("com.tranzvision.gd.WorkflowActionsBundle.service.impl.SignSelectImpl")
public class SignSelectImpl extends FrameworkImpl {

    @Autowired
    private SqlQuery jdbcTemplate;
    @Autowired
    private TZGDObject tzgdObject;


    /**
     * 查询加签人列表
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
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        mapRet.put("total", 0);
        mapRet.put("root", listData);
        mapRet.put("isHqIns", false);
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            if(jacksonUtil.containsKey("tzms_wflinsid") && jacksonUtil.containsKey("tzms_stpinsid")){
                //获取步骤实例Id
                // 实例ID;
                String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
                // 步骤实例id;
                String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
                
                listData = this.querySignPeopleList(tzms_wflinsid,tzms_stpinsid);
                mapRet.put("root", listData);
                mapRet.put("total", listData.size());
                
                //查询是不是会签任务
                String tzms_asgmethod = jdbcTemplate.queryForObject("Select tzms_asgmethod from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?",
                		new Object[]{tzms_wflinsid, tzms_stpinsid}, "String");
                if("2".equals(tzms_asgmethod)) {
                	mapRet.replace("isHqIns", true);
                }
            }
        } catch (Exception e) {
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }
        return jacksonUtil.Map2json(mapRet);
    }


    @Override
    public String tzOther(String oprType, String strParams, String[] errorMsg) {
        String strRet = "";
        try{
            if("updateSignSelect".equals(oprType)){
                strRet = this.updateSignSelect(strParams,errorMsg);
            }else if("addSignPeople".equals(oprType)){
                strRet = this.addSignPeople(strParams,errorMsg);
            }else if("cancelSignPeople".equals(oprType)){
                strRet = this.cancelSignPeople(strParams,errorMsg);
            }else if("updateSignOrder".equals(oprType)){
                strRet = this.updateSignOrder(strParams,errorMsg);
            }else if("querySignSelect".equals(oprType)){
                strRet = this.querySignSelect(strParams,errorMsg);
            }
        }catch(Exception e){
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = e.toString();
        }

        return strRet;
    }



    /**
     * 查询 加签属性
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String querySignSelect(String strParams, String[] errorMsg){
        Map<String,Object> map = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");

            if(tzms_wflinsid!=null && !"".equals(tzms_wflinsid) 
            		&& tzms_stpinsid!=null && !"".equals(tzms_stpinsid)) {
            	
                String signSelectSql = "Select tzms_asign_type,tzms_asign_operte from tzms_stpins_tbl where tzms_wflinsid = ? and tzms_stpinsid = ?";
                Map<String,Object> mapQuery = jdbcTemplate.queryForMap(signSelectSql,new Object[]{tzms_wflinsid,tzms_stpinsid});
                
                if(mapQuery != null) {
                	String tzms_asign_type = mapQuery.get("tzms_asign_type")==null?"": mapQuery.get("tzms_asign_type").toString();
                    String tzms_asign_operte = mapQuery.get("tzms_asign_operte")==null?"": mapQuery.get("tzms_asign_operte").toString();
                    
                    if("".equals(tzms_asign_type)){
                        tzms_asign_type = "1";
                    }
                    if("".equals(tzms_asign_operte)){
                        tzms_asign_operte = "1";
                    }
                    map.put("tzms_asign_type", tzms_asign_type);
                    map.put("tzms_asign_operte", tzms_asign_operte);
                }
            }else {
            	errorMsg[0] = "1";
                errorMsg[1] = "查询加签属性参数不正确";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = "系统错误，" + e.toString();
            return "";
        }

        return jacksonUtil.Map2json(map);
    }


    /**
     * 更新签核属性及步骤
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String updateSignSelect(String strParams, String[] errorMsg){
        Map<String,Object> map = new HashMap<>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 实例ID;
            String tzms_wflinsid = jacksonUtil.getString("tzms_wflinsid");
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            //签核类型
            String signType = jacksonUtil.getString("signType");
            //签核流转
            String levelType = jacksonUtil.getString("levelType");

            if(tzms_wflinsid!=null && !"".equals(tzms_wflinsid) 
            		&& tzms_stpinsid!=null && !"".equals(tzms_stpinsid)) {
            	//步骤实例状态
                String status = jdbcTemplate.queryForObject("Select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
                		new Object[]{tzms_wflinsid, tzms_stpinsid}, "String");
                
                List<Map<String,Object>> list = this.querySignPeopleList(tzms_wflinsid, tzms_stpinsid);
                map.put("root", list);
                map.put("total", list.size());
                
                if ("1".equals(status) || "3".equals(status) || "6".equals(status) || "8".equals(status)) {
                    String updateSql = "update tzms_stpins_tbl set tzms_asign_type = ? ,tzms_asign_operte = ? where tzms_wflinsid = ? and tzms_stpinsid = ?";
                    jdbcTemplate.update(updateSql, new Object[]{signType, levelType, tzms_wflinsid, tzms_stpinsid});
                    
                    map.put("ret", "0");
                    map.put("msg", "success");
                } else {
                	errorMsg[0] = "1";
                    errorMsg[1] = "任务已结束，签核属性不可更改！";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = "系统错误，" + e.toString();
            return "";
        }

        return jacksonUtil.Map2json(map);
    }


    /**
     * 更新加签人顺序
     * @param strParams
     * @param errorMsg
     * @return
     */
    @SuppressWarnings("unchecked")
	public String updateSignOrder(String strParams, String[] errorMsg){
        // 返回值;
        Map<String, Object> mapRet = new HashMap<String, Object>();
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            jacksonUtil.json2Map(strParams);
            // 步骤实例id;
            String tzms_stpinsid = jacksonUtil.getString("tzms_stpinsid");
            
            if(jacksonUtil.containsKey("data")) {
            	//加签人列表
                List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
                if(list != null && list.size() > 0) {
                	for(Map<String,Object> map : list) {
                		String tzms_stpproid = map.get("tzms_stpproid") == null ? "" : map.get("tzms_stpproid").toString();
                        String tzms_step_xh = map.get("tzms_step_xh") == null ? "" : map.get("tzms_step_xh").toString();
                        
                        jdbcTemplate.update("update tzms_asign_psn_tbl set tzms_step_xh=? where tzms_stpinsid=? and tzms_stpproid=?", 
                        		new Object[]{ tzms_step_xh, tzms_stpinsid, tzms_stpproid });
                	}
                }
            }else {
            	errorMsg[0] = "1";
                errorMsg[1] = "调整加签人顺序失败，参数错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = "系统错误，" + e.toString();
            return "";
        }
        return jacksonUtil.Map2json(mapRet);
    }


    /**
     * 新增加签人
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String addSignPeople(String strParams, String[] errorMsg){
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

            if(tzms_wflinsid!=null && !"".equals(tzms_wflinsid) 
            		&& tzms_stpinsid!=null && !"".equals(tzms_stpinsid) && tzms_stpproidStr!=null) {
            	
                String[] tzms_stpproids = tzms_stpproidStr.split(",");
                List<Map<String,Object>> list = this.querySignPeopleList(tzms_wflinsid,tzms_stpinsid);
                
                int order = list.size() + 1;
                for (int i = 0; i < tzms_stpproids.length; i++) {
                    if(tzms_stpproids[i].length() > 0){
                        String flag = jdbcTemplate.queryForObject("Select 'Y' from tzms_asign_psn_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
                        		new Object[]{tzms_stpinsid, tzms_stpproids[i]}, "String");
                        
                        if(!"Y".equals(flag)){
                            int rtn = jdbcTemplate.update("insert into tzms_asign_psn_tbl(tzms_stpinsid,tzms_stpproid,tzms_step_xh,tzms_asign_dtime) values(?,?,?,?)", 
                            		new Object[]{ tzms_stpinsid, tzms_stpproids[i], order, new Date() });
                            if(rtn > 0) {
                            	order++;
                            }
                        }
                    }
                }
                
                List<Map<String,Object>> listNew = this.querySignPeopleList(tzms_wflinsid,tzms_stpinsid);
                mapRet.put("ret", 0);
                mapRet.put("msg", "success");
                mapRet.put("root", listNew);
                mapRet.put("total", listNew.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg[0] = "1";
            errorMsg[1] = "系统错误，" + e.toString();
            return "";
        }

        return jacksonUtil.Map2json(mapRet);
    }



    /**
     * 撤销加签人
     * @param strParams
     * @param errorMsg
     * @return
     */
    public String cancelSignPeople(String strParams, String[] errorMsg){
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

            if(tzms_wflinsid!=null && !"".equals(tzms_wflinsid) 
            		&& tzms_stpinsid!=null && !"".equals(tzms_stpinsid) 
            		&& tzms_stpproidStr!=null && !"".equals(tzms_stpproidStr)) {
            	
                //查询加签人
                List<Map<String,Object>> list = this.querySignPeopleList(tzms_wflinsid,tzms_stpinsid);
                List<Map<String,Object>> listNewSignPeopleList = new ArrayList<>();
                //不可删除的加签人列表
                List<String> notCancelList = new ArrayList<>();

                if(list!=null){
                    int order = 1;
                    for (int i = 0; i < list.size(); i++) {
                        Map<String,Object> signPeopleMap = list.get(i);
                        String tzms_stpproid = signPeopleMap.get("tzms_stpproid")==null?"": String.valueOf(signPeopleMap.get("tzms_stpproid"));
                        String cancelFlag = signPeopleMap.get("cancelFlag")==null?"": String.valueOf(signPeopleMap.get("cancelFlag"));
                        if("false".equals(cancelFlag)){
                            notCancelList.add(tzms_stpproid);
                        }
                        if(!tzms_stpproidStr.contains(tzms_stpproid)||"false".equals(cancelFlag)){
                            signPeopleMap.put("tzms_step_xh",order);
                            listNewSignPeopleList.add(signPeopleMap);
                            order++;

                        }
                    }
                }

                //删除加签人
                String[] tzms_stpproids = tzms_stpproidStr.split(",");
                String deleteSql = "delete from tzms_asign_psn_tbl where tzms_stpproid = ?";
                for (int i = 0; i < tzms_stpproids.length; i++) {
                    if(!notCancelList.contains(tzms_stpproids[i])){
                        jdbcTemplate.update(deleteSql,new Object[]{tzms_stpproids[i]});
                    }
                }

                if(listNewSignPeopleList.size()>0){
                    String updateOrderSql = "update tzms_asign_psn_tbl set tzms_step_xh = ? where tzms_stpproid = ? and tzms_stpinsid = ?";
                    for (int i = 0; i < listNewSignPeopleList.size(); i++) {
                        Map<String,Object> signPeopleMap = listNewSignPeopleList.get(i);
                        String tzms_stpproid = signPeopleMap.get("tzms_stpproid")==null?"": String.valueOf(signPeopleMap.get("tzms_stpproid"));
                        String tzms_step_xh = signPeopleMap.get("tzms_step_xh")==null?"": String.valueOf(signPeopleMap.get("tzms_step_xh"));
                        jdbcTemplate.update(updateOrderSql,new Object[]{tzms_step_xh,tzms_stpproid,tzms_stpinsid});
                    }
                }
                List<Map<String,Object>> listNew = this.querySignPeopleList(tzms_wflinsid,tzms_stpinsid);
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
     * 查询加签人list
     * @param wflinsId
     * @param stpinsId
     * @return
     */
    public List<Map<String,Object>> querySignPeopleList(String wflinsId,String stpinsId){
        List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
        try {
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            
            String queryUserIdSql = tzgdObject.getSQLText("SQL.WorkflowActionsBundle.TzGetSignPeopleList1");
            String queryNameSql1 = "SELECT TOP 1 tzms_domain_zhid,tzms_staff_id,tzms_name FROM tzms_tea_defn_tBase WHERE tzms_user_uniqueid = ?";
            String queryNameSql2 = "Select TOP 1 C.tzms_org_name from tzms_tea_defn_tBase A,tzms_org_user_tBase B,tzms_org_structure_tree_tbase C where A.tzms_tea_defn_tid = B.tzms_tea_defnid AND B.tzms_org_uniqueid = C.tzms_org_structure_tree_tid AND A.tzms_user_uniqueid = ?";
            
            list = jdbcTemplate.queryForList(queryUserIdSql,new Object[]{ wflinsId, stpinsId, stpinsId });
            if(list!=null){
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
                            map.put("tzms_staff_id",tzms_staff_id);
                            map.put("tzms_domain_zhid",tzms_domain_zhid);
                            map.put("tzms_name",tzms_name);
                            map.put("tzms_org_name",tzms_org_name == null ? "":tzms_org_name);
                            listData.add(map);
                        }
                    }
                }
            }
        } catch (TzSystemException e) {
            e.printStackTrace();
        }
        return listData;

    }
}
