package com.tranzvision.gd.TZCostRelatedAEBundle.impl;

import com.tranzvision.gd.TZCostRelatedAEBundle.dao.*;
import com.tranzvision.gd.TZCostRelatedAEBundle.model.*;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZY
 * @Date: 2019/7/23 16:18
 * @Description: 项目分摊，奖学金，缴费计划，到账，开票等主入口
 */
@Service("com.tranzvision.gd.TZCostRelatedAEBundle.service.impl.TzCostRelatedMainImpl")
public class TzCostRelatedMainImpl {
    @Autowired
    private SqlQuery sqlQuery;
    @Autowired
    private GetSeqNum getSeqNum;
    @Autowired
    private TzJxjDefnTMapper tzJxjDefnTMapper;
    @Autowired
    private TzKsJxjTMapper tzKsJxjTMapper;
    @Autowired
    private TzJfPlanTMapper tzJfPlanTMapper;
    @Autowired
    private TzKpjlTblMapper tzKpjlTblMapper;
    @Autowired
    private TzXyxfftTblMapper tzXyxfftTblMapper;

    public void tzCostRelatedMain(){
        //查询EEDJ教务管理员的oprid
        String querySql = "SELECT OPRID FROM ps_tz_aq_yhxx_tbl WHERE TZ_DLZH_ID = 'EEDAdmin'";
        String oprid = sqlQuery.queryForObject(querySql,new Object[]{},"String");
        oprid = oprid == null ? "" : oprid;
        //执行奖学金导入
        this.executeScholarship(oprid);
        //执行学生奖学金导入
        this.executeScholarshipStu(oprid);
        //执行缴费计划导入
        this.executePayPlan(oprid);
        //执行到账记录导入
        this.executeBill(oprid);
        //执行开票记录导入
        this.executeInvoice(oprid);
        //执行项目学费分摊导入
        this.executeTuition(oprid);
    }


    /**
     * 导入奖学金
     */
    public void executeScholarship(String oprid){
        String queryTemporarySql = "SELECT A.TZ_JXJ_ID, A.TZ_JXJ_NAME, A.TZ_JXJ_JE, S.tzms_school_year_tId, P.tzms_pro_classify_tId , T.TZ_JXJ_TYPE_ID FROM TZ_JXJ_TEMP A LEFT JOIN tzms_school_year_t S ON S.tzms_schyear = A.TZ_XN OR S.tzms_schyear LIKE '%' + A.TZ_XN LEFT JOIN tzms_pro_classify_t P ON A.TZ_PROTYPE = P.tzms_pro_classify_name LEFT JOIN TZ_JXJ_TYPE_T T ON T.TZ_PRJ_TYPE = P.tzms_pro_classify_tId AND T.TZ_XN_ID = S.tzms_school_year_tId";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_JXJ_DEFN_T(TZ_JXJ_ID,TZ_JXJ_NAME,TZ_JXJ_TYPE_ID,TZ_JXJ_JE,TZ_JXJ_YX_STAT,TZ_ADD_TYPE,ROW_ADDED_OPRID,ROW_ADDED_DTTM,ROW_LASTMANT_OPRID,ROW_LASTMANT_DTTM) VALUES(?,?,?,?,?,?,?,?,?,?)";
        String queryJXJTypeSql = "SELECT TZ_JXJ_TYPE_ID FROM TZ_JXJ_TYPE_T WHERE convert(varchar(36),TZ_PRJ_TYPE) = ? AND convert(varchar(36),TZ_XN_ID) = ?";
        String insertJXJTypeSql = "INSERT INTO TZ_JXJ_TYPE_T(TZ_JXJ_TYPE_ID,TZ_JXJ_TYPE_NAME,TZ_PRJ_TYPE,TZ_XN_ID) VALUES(?,?,?,?)";
        List<TzJxjDefnT> insertList = new ArrayList<>();
        if(temporaryList != null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_JXJ_ID = temporaryList.get(i).get("TZ_JXJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_ID"));
                    String TZ_JXJ_NAME = temporaryList.get(i).get("TZ_JXJ_NAME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_NAME"));
                    double TZ_JXJ_JE = temporaryList.get(i).get("TZ_JXJ_JE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JXJ_JE")));
                    String tzms_school_year_tId = temporaryList.get(i).get("tzms_school_year_tId") == null ? "" : String.valueOf(temporaryList.get(i).get("tzms_school_year_tId"));
                    String tzms_pro_classify_tId = temporaryList.get(i).get("tzms_pro_classify_tId") == null ? "" : String.valueOf(temporaryList.get(i).get("tzms_pro_classify_tId"));
                    String TZ_JXJ_TYPE_ID = temporaryList.get(i).get("TZ_JXJ_TYPE_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_TYPE_ID"));
                    if(TZ_JXJ_ID.length() > 0 && TZ_JXJ_NAME.length() > 0){
                        //判断是否使用历史学年
                        if(tzms_school_year_tId.length()==0){
                            tzms_school_year_tId = sqlQuery.queryForObject("SELECT TOP 1 tzms_school_year_tId FROM tzms_school_year_t WHERE tzms_schyear = ? ",new Object[]{"历史学年"},"String");
                        }
                        if(TZ_JXJ_TYPE_ID.length() == 0 && tzms_school_year_tId.length() > 0 && tzms_pro_classify_tId.length()>0){
                            TZ_JXJ_TYPE_ID = sqlQuery.queryForObject(queryJXJTypeSql,new Object[]{tzms_pro_classify_tId,tzms_school_year_tId},"String");
                            if(TZ_JXJ_TYPE_ID == null){
                                //需要新增奖学金类别
                                TZ_JXJ_TYPE_ID = String.valueOf(getSeqNum.getSeqNum("TZ_JXJ_TYPE_T", "TZ_JXJ_TYPE_ID"));
                                sqlQuery.update(insertJXJTypeSql,new Object[]{TZ_JXJ_TYPE_ID,"其他",tzms_pro_classify_tId,tzms_school_year_tId});
                            }
                        }
                        /*TzJxjDefnT tzJxjDefnT = new TzJxjDefnT();
                        tzJxjDefnT.setTzJxjId("HIS"+TZ_JXJ_ID);
                        tzJxjDefnT.setTzJxjName(TZ_JXJ_NAME);
                        tzJxjDefnT.setTzJxjTypeId(TZ_JXJ_TYPE_ID);
                        tzJxjDefnT.setTzJxjJe(TZ_JXJ_JE);
                        tzJxjDefnT.setTzJxjYxStat("1");
                        tzJxjDefnT.setTzAddType("2");
                        tzJxjDefnT.setRowAddedOprid(oprid);
                        tzJxjDefnT.setRowAddedDttm(new Date());
                        tzJxjDefnT.setRowLastmantOprid(oprid);
                        tzJxjDefnT.setRowLastmantDttm(new Date());
                        insertList.add(tzJxjDefnT);*/
                        sqlQuery.update(insertSql,new Object[]{"HIS"+TZ_JXJ_ID,TZ_JXJ_NAME,TZ_JXJ_TYPE_ID,TZ_JXJ_JE,"1","2",oprid,new Date(),oprid,new Date()});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //批量新增
            try{
                //tzJxjDefnTMapper.insertForeach(insertList);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 导入奖学金
     */
    public void executeScholarshipOld(String oprid){
        String queryTemporarySql = "SELECT * FROM TZ_JXJ_TEMP";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_JXJ_DEFN_T(TZ_JXJ_ID,TZ_JXJ_NAME,TZ_JXJ_TYPE_ID,TZ_JXJ_JE,TZ_JXJ_YX_STAT,TZ_ADD_TYPE,ROW_ADDED_OPRID,ROW_ADDED_DTTM,ROW_LASTMANT_OPRID,ROW_LASTMANT_DTTM) VALUES(?,?,?,?,?,?,?,?,?,?)";
        String queryXNSql = "SELECT tzms_school_year_tId FROM tzms_school_year_t WHERE tzms_schyear = ? OR tzms_schyear LIKE ?";
        String queryTypeSql = "SELECT tzms_pro_classify_tId FROM tzms_pro_classify_t WHERE tzms_pro_classify_name = ?";
        String queryJXJTypeSql = "SELECT TZ_JXJ_TYPE_ID FROM TZ_JXJ_TYPE_T WHERE convert(varchar(36),TZ_PRJ_TYPE) = ? AND convert(varchar(36),TZ_XN_ID) = ?";
        String insertJXJTypeSql = "INSERT INTO TZ_JXJ_TYPE_T(TZ_JXJ_TYPE_ID,TZ_JXJ_TYPE_NAME,TZ_PRJ_TYPE,TZ_XN_ID) VALUES(?,?,?,?)";
        if(temporaryList != null){
            for (int i = 0; i < temporaryList.size(); i++) {
                String TZ_JXJ_ID = temporaryList.get(i).get("TZ_JXJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_ID"));
                String TZ_JXJ_NAME = temporaryList.get(i).get("TZ_JXJ_NAME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_NAME"));
                double TZ_JXJ_JE = temporaryList.get(i).get("TZ_JXJ_JE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JXJ_JE")));
                String TZ_XN = temporaryList.get(i).get("TZ_XN") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_XN"));
                String TZ_PROTYPE = temporaryList.get(i).get("TZ_PROTYPE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_PROTYPE"));
                if(TZ_JXJ_ID.length() > 0 && TZ_JXJ_NAME.length() > 0){
                    String TZ_XN_ID = sqlQuery.queryForObject(queryXNSql,new Object[]{TZ_XN,"%"+TZ_XN},"String");
                    String TZ_PRJ_TYPE = sqlQuery.queryForObject(queryTypeSql,new Object[]{TZ_PROTYPE},"String");
                    String TZ_JXJ_TYPE_ID = "";
                    if(TZ_XN_ID!=null && TZ_PRJ_TYPE != null){
                        TZ_JXJ_TYPE_ID = sqlQuery.queryForObject(queryJXJTypeSql,new Object[]{TZ_PRJ_TYPE,TZ_XN_ID},"String");
                    }
                    if(TZ_JXJ_TYPE_ID == null){
                        //需要新增奖学金类别
                        TZ_JXJ_TYPE_ID = String.valueOf(getSeqNum.getSeqNum("TZ_JXJ_TYPE_T", "TZ_JXJ_TYPE_ID"));
                        sqlQuery.update(insertJXJTypeSql,new Object[]{TZ_JXJ_TYPE_ID,"其他",TZ_PRJ_TYPE,TZ_XN_ID});
                    }
                    //temporaryList.get(i).put("TZ_JXJ_TYPE_ID",TZ_JXJ_TYPE_ID);
                    sqlQuery.update(insertSql,new Object[]{"HIS"+TZ_JXJ_ID,TZ_JXJ_NAME,TZ_JXJ_TYPE_ID,TZ_JXJ_JE,"1","2",oprid,new Date(),oprid,new Date()});
                }
            }
        }
    }

    /**
     * 导入奖学金学生关系
     */
    public void executeScholarshipStu(String oprid){
        String queryTemporarySql = "SELECT A.TZ_BMH_ID,A.TZ_JXJ_ID,A.TZ_REMARKS,F.TZ_APP_INS_ID FROM TZ_KS_JXJ_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_BMH_ID = F.TZ_MSH_ID";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_KS_JXJ_T(TZ_APP_INS_ID,TZ_JXJ_ID,TZ_REMARKS) VALUES(?,?,?)";
        List<TzKsJxjT> insertList = new ArrayList<>();
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                String TZ_APP_INS_ID = temporaryList.get(i).get("TZ_APP_INS_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_APP_INS_ID"));
                String TZ_JXJ_ID = temporaryList.get(i).get("TZ_JXJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JXJ_ID"));
                String TZ_REMARKS = temporaryList.get(i).get("TZ_REMARKS") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_REMARKS"));
                    if(TZ_APP_INS_ID.length() > 0 && TZ_JXJ_ID.length() > 0){

                        /*TzKsJxjT tzKsJxjT = new TzKsJxjT();
                        tzKsJxjT.setTzAppInsId(Long.valueOf(TZ_APP_INS_ID));
                        tzKsJxjT.setTzJxjId("HIS"+TZ_JXJ_ID);
                        tzKsJxjT.setTzRemarks(TZ_REMARKS);
                        insertList.add(tzKsJxjT);*/
                        sqlQuery.update(insertSql,new Object[]{TZ_APP_INS_ID,"HIS"+TZ_JXJ_ID,TZ_REMARKS});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                //tzKsJxjTMapper.insertForeach(insertList);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    /**
     * 导入缴费计划
     */
    public void executePayPlan(String oprid){
        String queryTemporarySql = "SELECT A.TZ_ID , CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE Cast(F.TZ_APP_INS_ID as varchar) END AS TZ_OBJ_ID , CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN '2' ELSE '1' END AS TZ_OBJ_TYPE, C.TZ_LXBH AS TZ_OBJ_PRJ_ID, A.TZ_OBJ_NAME, A.TZ_JF_DATE , ISNULL(A.TZ_JF_TZ_JE, 0) AS TZ_JF_TZ_JE , ISNULL(A.TZ_JF_BQYS, 0) AS TZ_JF_BQYS , ISNULL(( SELECT SUM(ISNULL(J.TZ_JXJ_JE, 0)) FROM TZ_JXJ_DEFN_T J, TZ_KS_JXJ_T D WHERE J.TZ_JXJ_ID = D.TZ_JXJ_ID AND D.TZ_APP_INS_ID = F.TZ_APP_INS_ID ),0) AS TZ_JF_JM_JE FROM TZ_PAYPLAN_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_ID = F.TZ_MSH_ID LEFT JOIN PS_TZ_CLASS_INF_T C ON F.TZ_CLASS_ID = C.TZ_CLASS_ID";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_JF_PLAN_T(TZ_JFPL_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_OBJ_PRJ_ID,TZ_OBJ_NAME,TZ_JF_TYPE,TZ_JF_DATE,TZ_JF_BZ_JE,TZ_JF_TZ_JE,TZ_JF_JM_JE,TZ_JF_BQYS,TZ_JF_STAT,ROW_ADDED_OPRID,ROW_ADDED_DTTM,ROW_LASTMANT_OPRID,ROW_LASTMANT_DTTM) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String temporaryVariables = "";
        int counter = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TzJfPlanT> insertList = new ArrayList<>();
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_ID = temporaryList.get(i).get("TZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_ID"));
                    String TZ_OBJ_ID = temporaryList.get(i).get("TZ_OBJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_ID"));
                    String TZ_OBJ_TYPE = temporaryList.get(i).get("TZ_OBJ_TYPE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_TYPE"));
                    String TZ_OBJ_PRJ_ID = temporaryList.get(i).get("TZ_OBJ_PRJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_PRJ_ID"));
                    String TZ_OBJ_NAME = temporaryList.get(i).get("TZ_OBJ_NAME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_NAME"));
                    String TZ_JF_DATE = temporaryList.get(i).get("TZ_JF_DATE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JF_DATE"));
                    double TZ_JF_TZ_JE = temporaryList.get(i).get("TZ_JF_TZ_JE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JF_TZ_JE")));
                    double TZ_JF_BQYS = temporaryList.get(i).get("TZ_JF_BQYS") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JF_BQYS")));
                    double TZ_JF_JM_JE = temporaryList.get(i).get("TZ_JF_JM_JE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JF_JM_JE")));
                    if(TZ_OBJ_ID.length() > 0 && !"NULL".equals(TZ_OBJ_ID.toUpperCase())){
                        //奖学金只需添加至第一条缴费计划，因此增加计数器，判断相同id是否多个缴费计划
                        if(temporaryVariables.equals(TZ_ID)){
                            counter++;
                        }else {
                            temporaryVariables = TZ_ID;
                            counter = 1;
                        }
                        double TZ_JF_BZ_JE = 0d;
                        if(counter == 1){
                            TZ_JF_BZ_JE = TZ_JF_TZ_JE + TZ_JF_BQYS + TZ_JF_JM_JE;
                        }else {
                            TZ_JF_BZ_JE = TZ_JF_TZ_JE + TZ_JF_BQYS;
                        }
                        //执行插入
                        String TZ_JFPL_ID = "HIS"+getSeqNum.getSeqNum("TZ_JF_PLAN_T","TZ_JFPL_ID");
                        /*TzJfPlanT tzJfPlanT = new TzJfPlanT();
                        tzJfPlanT.setTzJfplId(TZ_JFPL_ID);
                        tzJfPlanT.setTzObjId(TZ_OBJ_ID);
                        tzJfPlanT.setTzObjType(TZ_OBJ_TYPE);
                        tzJfPlanT.setTzObjPrjId(TZ_OBJ_PRJ_ID);
                        tzJfPlanT.setTzObjName(TZ_OBJ_NAME);
                        tzJfPlanT.setTzJfType("2");
                        tzJfPlanT.setTzJfDate(sdf.parse(TZ_JF_DATE));
                        tzJfPlanT.setTzJfBzJe(BigDecimal.valueOf(TZ_JF_BZ_JE));
                        tzJfPlanT.setTzJfTzJe(BigDecimal.valueOf(TZ_JF_TZ_JE));
                        tzJfPlanT.setTzJfJmJe(BigDecimal.valueOf(TZ_JF_JM_JE));
                        tzJfPlanT.setTzJfBqys(BigDecimal.valueOf(TZ_JF_BQYS));
                        tzJfPlanT.setRowAddedOprid(oprid);
                        tzJfPlanT.setRowAddedDttm(new Date());
                        tzJfPlanT.setRowLastmantOprid(oprid);
                        tzJfPlanT.setRowLastmantDttm(new Date());
                        insertList.add(tzJfPlanT);*/
                        sqlQuery.update(insertSql,new Object[]{TZ_JFPL_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_OBJ_PRJ_ID,TZ_OBJ_NAME,"2",TZ_JF_DATE,TZ_JF_BZ_JE,TZ_JF_TZ_JE,TZ_JF_JM_JE,TZ_JF_BQYS,"1",oprid,new Date(),oprid,new Date()});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                //tzJfPlanTMapper.insertForeach(insertList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 导入到账记录
     */
    public void executeBill(String oprid){
        String queryTemporarySql = "SELECT A.TZ_ID , CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE Cast(F.TZ_APP_INS_ID as varchar) END AS TZ_OBJ_ID , CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN '2' ELSE '1' END AS TZ_OBJ_TYPE, A.TZ_JR_ID, A.TZ_DZ_JIE, A.TZ_DZ_DATE, A.TZ_SK_TYPE , A.TZ_NOTES, A.TZ_LK_UINT FROM TZ_DZJL_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_ID = F.TZ_MSH_ID LEFT JOIN PS_TZ_CLASS_INF_T C ON F.TZ_CLASS_ID = C.TZ_CLASS_ID";
        String insertSql = "INSERT INTO TZ_DZJL_TBL(TZ_DZ_ID,TZ_JR_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_DZ_JIE,TZ_NOTES,TZ_SK_TYPE,TZ_DZ_DATE,TZ_LK_UINT,TZ_SYNC_STA,TZ_LR_OPRID,TZ_LR_DTTM,TZ_JF_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String querySql = "SELECT TOP 1 TZ_JFPL_ID,(ISNULL(TZ_JF_BZ_JE, 0) - ISNULL(TZ_JF_TZ_JE, 0) - ISNULL(TZ_JF_JM_JE, 0) - ISNULL(TZ_JF_BQSS, 0)) TZ_JF_BQCE FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ? AND TZ_OBJ_TYPE = ? AND (TZ_JF_STAT = '1' OR TZ_JF_STAT = '2' OR TZ_JF_STAT IS NULL OR TZ_JF_STAT = '') ORDER BY TZ_JF_DATE";
        String updateSql = "UPDATE TZ_JF_PLAN_T SET TZ_JF_BQSS = ISNULL(TZ_JF_BQSS, 0) + convert(decimal(10,2),?),TZ_JF_STAT = ? WHERE TZ_JFPL_ID = ?";
        String JFQuerySql = "SELECT SUM(CASE  WHEN TZ_JF_STAT = '1' OR TZ_JF_STAT IS NULL OR TZ_JF_STAT = '' THEN 1 ELSE 0 END) AS UnpaidFees, SUM(CASE TZ_JF_STAT WHEN '2' THEN 1 ELSE 0 END) AS PartialPayment , SUM(CASE TZ_JF_STAT WHEN '3' THEN 1 ELSE 0 END) AS Paymented, SUM(1) AS PayCount FROM TZ_JF_PLAN_T WHERE TZ_OBJ_ID = ?";
        String updateJFSql = "UPDATE PS_TZ_FORM_WRK_T SET TZ_JFZT = ? WHERE TZ_APP_INS_ID = ?";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_ID = temporaryList.get(i).get("TZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_ID"));
                    String TZ_OBJ_ID = temporaryList.get(i).get("TZ_OBJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_ID"));
                    String TZ_OBJ_TYPE = temporaryList.get(i).get("TZ_OBJ_TYPE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_TYPE"));
                    String TZ_JR_ID = temporaryList.get(i).get("TZ_JR_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_JR_ID"));
                    double TZ_DZ_JIE = temporaryList.get(i).get("TZ_DZ_JIE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_DZ_JIE")));
                    String TZ_DZ_DATE = temporaryList.get(i).get("TZ_DZ_DATE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_DZ_DATE"));
                    String TZ_SK_TYPE = temporaryList.get(i).get("TZ_SK_TYPE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_SK_TYPE"));
                    String TZ_NOTES = temporaryList.get(i).get("TZ_NOTES") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_NOTES"));
                    String TZ_LK_UINT = temporaryList.get(i).get("TZ_LK_UINT") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_LK_UINT"));
                    if(TZ_ID.length() > 0 && TZ_OBJ_ID.length() >0 && !"NULL".equals(TZ_OBJ_ID.toUpperCase())){
                        //解析付款方式
                        if(TZ_SK_TYPE.contains("现金")){
                            TZ_SK_TYPE = "1";
                        }else if(TZ_SK_TYPE.contains("POS")){
                            TZ_SK_TYPE = "3";
                        }else {
                            TZ_SK_TYPE = "2";
                        }
                        if(TZ_DZ_JIE != 0d){
                            //新增到账信息
                            String TZ_DZ_ID = "HIS" + getSeqNum.getSeqNum("TZ_DZJL_TBL", "TZ_DZ_ID");
                            sqlQuery.update(insertSql,new Object[]{TZ_DZ_ID,TZ_JR_ID,TZ_OBJ_ID,TZ_OBJ_TYPE,TZ_DZ_JIE,TZ_NOTES,TZ_SK_TYPE,TZ_DZ_DATE,TZ_LK_UINT,"1",oprid,new Date(),"2"});
                            while (TZ_DZ_JIE != 0d){
                                Map<String,Object> map = sqlQuery.queryForMap(querySql,new Object[]{TZ_OBJ_ID,TZ_OBJ_TYPE});
                                if(map!=null){
                                    String TZ_JFPL_ID = map.get("TZ_JFPL_ID")==null?"": String.valueOf(map.get("TZ_JFPL_ID"));
                                    //获取应收差额
                                    double TZ_JF_BQCE = map.get("TZ_JF_BQCE")==null ? 0d: Double.parseDouble(String.valueOf(map.get("TZ_JF_BQCE")));

                                    if(TZ_DZ_JIE < TZ_JF_BQCE){
                                        sqlQuery.update(updateSql,new Object[]{TZ_DZ_JIE,"2",TZ_JFPL_ID});
                                        TZ_DZ_JIE = 0d;
                                    }else if(TZ_DZ_JIE == TZ_JF_BQCE){
                                        sqlQuery.update(updateSql,new Object[]{TZ_DZ_JIE,"3",TZ_JFPL_ID});
                                        TZ_DZ_JIE = 0d;
                                    }else if(TZ_DZ_JIE > TZ_JF_BQCE){
                                        sqlQuery.update(updateSql,new Object[]{TZ_JF_BQCE,"3",TZ_JFPL_ID});
                                        TZ_DZ_JIE = TZ_DZ_JIE - TZ_JF_BQCE;
                                    }
                                }else {
                                    break;
                                }
                            }

                        }
                        //判断学生是否完成缴费 更新form_wrk表状态
                        if("1".equals(TZ_OBJ_TYPE)){
                            Map<String,Object> jfMap = sqlQuery.queryForMap(JFQuerySql,new Object[]{TZ_OBJ_ID});
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
                                sqlQuery.update(updateJFSql,new Object[]{TZ_JFZT,TZ_OBJ_ID});
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导入开票信息(暂废弃)
     */
    public void executeInvoiceOld(String oprid){
        String queryTemporarySql = "SELECT A.TZ_ID , CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END AS TZ_OBJ_ID, A.TZ_KP_JINE, A.TZ_FP_NO, A.TZ_FP_TITLE, A.TZ_KP_TIME , A.TZ_NAME, L.tzms_fyxbh FROM TZ_KPJL_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_ID = F.TZ_MSH_ID LEFT JOIN PS_TZ_CLASS_INF_T C ON F.TZ_CLASS_ID = C.TZ_CLASS_ID LEFT JOIN tzms_lxxmb_def_t L ON L.tzms_item_num = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_KPJL_TBL(TZ_KP_NBID,TZ_DZ_ID,TZ_KP_JINE,TZ_KP_DATE,TZ_KPFY_NO,TZ_FP_NO,TZ_FP_TITLE,TZ_KP_STATUS,TZ_NAME,TZ_FP_TYPE,TZ_KP_OPRID,TZ_KP_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        //优先查询到账金额与开票金额相等的 且开票金额未饱和 到账记录
        String queryEqualSql = "SELECT D.TZ_DZ_ID FROM TZ_DZJL_TBL D LEFT JOIN TZ_KPJL_TBL K ON D.TZ_DZ_ID = K.TZ_DZ_ID WHERE TZ_OBJ_ID = ? AND TZ_DZ_JIE = ? GROUP BY D.TZ_DZ_ID, D.TZ_DZ_JIE HAVING D.TZ_DZ_JIE > SUM(K.TZ_KP_JINE)";
        //未满足上述条件 则 按时间获取第一条未饱和的到账记录
        String checkSql = "SELECT 'Y' FROM TZ_DZJL_TBL D LEFT JOIN TZ_KPJL_TBL K ON D.TZ_DZ_ID = K.TZ_DZ_ID WHERE D.TZ_DZ_ID = ? GROUP BY D.TZ_DZ_ID, D.TZ_DZ_JIE HAVING D.TZ_DZ_JIE = SUM(K.TZ_KP_JINE)";
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_ID = temporaryList.get(i).get("TZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_ID"));
                    String TZ_OBJ_ID = temporaryList.get(i).get("TZ_OBJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_ID"));
                    String TZ_FP_NO = temporaryList.get(i).get("TZ_FP_NO") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_FP_NO"));
                    String TZ_FP_TITLE = temporaryList.get(i).get("TZ_FP_TITLE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_FP_TITLE"));
                    String TZ_KP_TIME = temporaryList.get(i).get("TZ_KP_TIME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_KP_TIME"));
                    String TZ_NAME = temporaryList.get(i).get("TZ_NAME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_NAME"));
                    String tzms_fyxbh = temporaryList.get(i).get("tzms_fyxbh") == null ? "" : String.valueOf(temporaryList.get(i).get("tzms_fyxbh"));
                    double TZ_KP_JINE = temporaryList.get(i).get("TZ_KP_JINE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_KP_JINE")));
                    if(TZ_ID.length() > 0 && TZ_OBJ_ID.length() > 0 && !"NULL".equals(TZ_OBJ_ID.toUpperCase()) ){
                        String TZ_DZ_ID = "";
                        //校验当前已使用的到账记录是否饱和
                        String checkEqualFlag = sqlQuery.queryForObject(queryEqualSql,new Object[]{TZ_OBJ_ID,TZ_KP_JINE},"String");
                        if(checkEqualFlag != null && checkEqualFlag.length()>0){
                            TZ_DZ_ID = checkEqualFlag;
                        }else {

                        }



                        String TZ_KP_NBID = "HIS" + getSeqNum.getSeqNum("TZ_KPJL_TBL", "TZ_KP_NBID");
                        sqlQuery.update(insertSql,new Object[]{TZ_KP_NBID,TZ_DZ_ID,TZ_KP_JINE,TZ_KP_TIME,tzms_fyxbh,TZ_FP_NO,TZ_FP_TITLE,"2",TZ_NAME,"1",oprid,TZ_KP_TIME});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导入开票信息
     */
    public void executeInvoice(String oprid){
        String queryTemporarySql = "SELECT A.TZ_ID ,CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END AS TZ_OBJ_ID, CASE  WHEN ( SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D WHERE D.TZ_DZ_JIE = A.TZ_KP_JINE AND D.TZ_OBJ_ID = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END ) IS NULL THEN ( SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D WHERE D.TZ_DZ_JIE <> A.TZ_KP_JINE AND D.TZ_OBJ_ID = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END ) WHEN ( SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D WHERE D.TZ_DZ_JIE <> A.TZ_KP_JINE AND D.TZ_OBJ_ID = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END ) IS NULL THEN ( SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D WHERE D.TZ_OBJ_ID = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END ) ELSE ( SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D WHERE D.TZ_DZ_JIE = A.TZ_KP_JINE AND D.TZ_OBJ_ID = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END ) END AS TZ_DZ_ID, A.TZ_KP_JINE, A.TZ_FP_NO, A.TZ_FP_TITLE, A.TZ_KP_TIME , A.TZ_NAME, L.tzms_fyxbh FROM TZ_KPJL_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_ID = F.TZ_MSH_ID LEFT JOIN PS_TZ_CLASS_INF_T C ON F.TZ_CLASS_ID = C.TZ_CLASS_ID LEFT JOIN tzms_lxxmb_def_t L ON L.tzms_item_num = CASE  WHEN SUBSTRING(A.TZ_ID, 1, 3) = 'EDP' THEN C.TZ_LXBH ELSE CONVERT(VARCHAR(15), F.TZ_APP_INS_ID) END";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        String insertSql = "INSERT INTO TZ_KPJL_TBL(TZ_KP_NBID,TZ_DZ_ID,TZ_KP_JINE,TZ_KP_DATE,TZ_KPFY_NO,TZ_FP_NO,TZ_FP_TITLE,TZ_KP_STATUS,TZ_NAME,TZ_FP_TYPE,TZ_KP_OPRID,TZ_KP_TIME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String checkSql = "SELECT 'Y' FROM TZ_DZJL_TBL D LEFT JOIN TZ_KPJL_TBL K ON D.TZ_DZ_ID = K.TZ_DZ_ID WHERE D.TZ_DZ_ID = ? GROUP BY D.TZ_DZ_ID, D.TZ_DZ_JIE HAVING ISNULL(D.TZ_DZ_JIE,0) = ISNULL(SUM(K.TZ_KP_JINE),0)";
        String queryDZEqualSql = "SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D LEFT JOIN TZ_KPJL_TBL K ON D.TZ_DZ_ID = K.TZ_DZ_ID WHERE TZ_OBJ_ID = ? AND TZ_DZ_JIE = ? GROUP BY D.TZ_DZ_ID, D.TZ_DZ_JIE, D.TZ_LR_DTTM HAVING ISNULL(D.TZ_DZ_JIE,0) > ISNULL(SUM(K.TZ_KP_JINE),0) ORDER BY D.TZ_LR_DTTM ASC";
        String queryDZSql = "SELECT TOP 1 D.TZ_DZ_ID FROM TZ_DZJL_TBL D LEFT JOIN TZ_KPJL_TBL K ON D.TZ_DZ_ID = K.TZ_DZ_ID WHERE TZ_OBJ_ID = ? GROUP BY D.TZ_DZ_ID, D.TZ_DZ_JIE, D.TZ_LR_DTTM HAVING ISNULL(D.TZ_DZ_JIE,0) > ISNULL(SUM(K.TZ_KP_JINE),0) ORDER BY D.TZ_LR_DTTM ASC";
        List<TzKpjlTbl> insertList = new ArrayList<>();
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_ID = temporaryList.get(i).get("TZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_ID"));
                    String TZ_OBJ_ID = temporaryList.get(i).get("TZ_OBJ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_OBJ_ID"));
                    String TZ_DZ_ID = temporaryList.get(i).get("TZ_DZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_DZ_ID"));
                    String TZ_FP_NO = temporaryList.get(i).get("TZ_FP_NO") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_FP_NO"));
                    String TZ_FP_TITLE = temporaryList.get(i).get("TZ_FP_TITLE") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_FP_TITLE"));
                    String TZ_KP_TIME = temporaryList.get(i).get("TZ_KP_TIME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_KP_TIME"));
                    String TZ_NAME = temporaryList.get(i).get("TZ_NAME") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_NAME"));
                    String tzms_fyxbh = temporaryList.get(i).get("tzms_fyxbh") == null ? "" : String.valueOf(temporaryList.get(i).get("tzms_fyxbh"));
                    double TZ_KP_JINE = temporaryList.get(i).get("TZ_KP_JINE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_KP_JINE")));
                    if(TZ_ID.length() > 0 && TZ_OBJ_ID.length() > 0 && !"NULL".equals(TZ_DZ_ID.toUpperCase())){
                        //校验当前已使用的到账记录是否饱和
                        String checkFlag = sqlQuery.queryForObject(checkSql,new Object[]{TZ_DZ_ID},"String");
                        if(checkFlag!=null && "Y".equals(checkFlag)){
                            //已饱和，获取未饱和且到账金额一致的一条到账记录
                            String dzEqualId = sqlQuery.queryForObject(queryDZEqualSql,new Object[]{TZ_OBJ_ID,TZ_KP_JINE},"String");
                            if(dzEqualId != null && dzEqualId.length()>0){
                                TZ_DZ_ID = dzEqualId;
                            }else {
                                //获取未饱和的 一条到账记录
                                String dzId = sqlQuery.queryForObject(queryDZSql,new Object[]{TZ_OBJ_ID},"String");
                                if(dzId != null && dzId.length()>0){
                                    TZ_DZ_ID = dzId;
                                }
                            }
                        }
                        String TZ_KP_NBID = "HIS" + getSeqNum.getSeqNum("TZ_KPJL_TBL", "TZ_KP_NBID");
                        /*TzKpjlTbl tzKpjlTbl = new TzKpjlTbl();
                        tzKpjlTbl.setTzKpNbid(TZ_KP_NBID);
                        tzKpjlTbl.setTzDzId(TZ_DZ_ID);
                        tzKpjlTbl.setTzKpJine(BigDecimal.valueOf(TZ_KP_JINE));
                        tzKpjlTbl.setTzKpDate(sdf.parse(TZ_KP_TIME));
                        tzKpjlTbl.setTzKpfyNo(tzms_fyxbh);
                        tzKpjlTbl.setTzFpNo(TZ_FP_NO);
                        tzKpjlTbl.setTzFpTitle(TZ_FP_TITLE);
                        tzKpjlTbl.setTzKpStatus("1");
                        tzKpjlTbl.setTzName(TZ_NAME);
                        tzKpjlTbl.setTzFpType("1");
                        tzKpjlTbl.setTzKpOprid(oprid);
                        tzKpjlTbl.setTzKpTime(sdf2.parse(TZ_KP_TIME));
                        insertList.add(tzKpjlTbl);*/
                        sqlQuery.update(insertSql,new Object[]{TZ_KP_NBID,TZ_DZ_ID,TZ_KP_JINE,TZ_KP_TIME,tzms_fyxbh,TZ_FP_NO,TZ_FP_TITLE,"2",TZ_NAME,"1",oprid,TZ_KP_TIME});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                //tzKpjlTblMapper.insertForeach(insertList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 导入学费摊销
     */
    public void executeTuition(String oprid){
        String queryTemporarySql = "SELECT A.TZ_ID, F.TZ_APP_INS_ID, C.TZ_LXBH , SUBSTRING(A.TZ_DATE, 1, 4) AS TZ_TX_YEAR , SUBSTRING(A.TZ_DATE, 5, 6) AS TZ_TX_MON , ISNULL(A.TZ_XF_JINE, 0) AS TZ_XF_JINE , ISNULL(A.TZ_JXJ_JINE, 0) AS TZ_JXJ_JINE , ISNULL(A.TZ_XF_JINE, 0) - ISNULL(A.TZ_JXJ_JINE, 0) AS TZ_TX_JINE FROM TZ_XYXFFT_TEMP A LEFT JOIN PS_TZ_FORM_WRK_T F ON A.TZ_ID = F.TZ_MSH_ID LEFT JOIN PS_TZ_CLASS_INF_T C ON F.TZ_CLASS_ID = C.TZ_CLASS_ID";
        List<Map<String,Object>> temporaryList = sqlQuery.queryForList(queryTemporarySql,new Object[]{});
        System.out.println("temporaryList================="+temporaryList.size());
        String insertSql = "INSERT INTO TZ_XYXFFT_TBL(tzms_pro_nbid,TZ_APP_INS_ID,TZ_TX_YEAR,TZ_TX_MON,TZ_TX_JINE,TZ_JXJ_JINE,TZ_XF_JINE,TZ_OPE_OPRID,TZ_OPE_DTTM) VALUES(?,CONVERT(bigint,?),?,?,?,?,?,?,?)";
        List<TzXyxfftTbl> insertList = new ArrayList<>();
        if(temporaryList!=null){
            for (int i = 0; i < temporaryList.size(); i++) {
                try{
                    String TZ_ID = temporaryList.get(i).get("TZ_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_ID"));
                    String TZ_LXBH = temporaryList.get(i).get("TZ_LXBH") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_LXBH"));
                    String TZ_APP_INS_ID = temporaryList.get(i).get("TZ_APP_INS_ID") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_APP_INS_ID"));
                    String TZ_TX_YEAR = temporaryList.get(i).get("TZ_TX_YEAR") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_TX_YEAR"));
                    String TZ_TX_MON = temporaryList.get(i).get("TZ_TX_MON") == null ? "" : String.valueOf(temporaryList.get(i).get("TZ_TX_MON"));
                    double TZ_XF_JINE = temporaryList.get(i).get("TZ_XF_JINE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_XF_JINE")));
                    double TZ_JXJ_JINE = temporaryList.get(i).get("TZ_JXJ_JINE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_JXJ_JINE")));
                    double TZ_TX_JINE = temporaryList.get(i).get("TZ_TX_JINE") == null ? 0d : Double.parseDouble(String.valueOf(temporaryList.get(i).get("TZ_TX_JINE")));
                    if(TZ_ID.length() > 0 && TZ_LXBH.length()>0 && TZ_APP_INS_ID.length()>0 && !"NULL".equals(TZ_APP_INS_ID.toUpperCase())){
                        /*TzXyxfftTbl tzXyxfftTbl = new TzXyxfftTbl();
                        tzXyxfftTbl.setTzmsProNbid(TZ_LXBH);
                        tzXyxfftTbl.setTzAppInsId(Long.valueOf(TZ_APP_INS_ID));
                        tzXyxfftTbl.setTzTxYear(TZ_TX_YEAR);
                        tzXyxfftTbl.setTzTxMon(TZ_TX_MON);
                        tzXyxfftTbl.setTzTxJine(BigDecimal.valueOf(TZ_TX_JINE));
                        tzXyxfftTbl.setTzJxjJine(BigDecimal.valueOf(TZ_JXJ_JINE));
                        tzXyxfftTbl.setTzXfJine(BigDecimal.valueOf(TZ_XF_JINE));
                        tzXyxfftTbl.setTzOpeOprid(oprid);
                        tzXyxfftTbl.setTzOpeDttm(new Date());
                        insertList.add(tzXyxfftTbl);*/
                        sqlQuery.update(insertSql,new Object[]{TZ_LXBH,TZ_APP_INS_ID,TZ_TX_YEAR,TZ_TX_MON,TZ_TX_JINE,TZ_JXJ_JINE,TZ_XF_JINE,oprid,new Date()});
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                //tzXyxfftTblMapper.insertForeach(insertList);
            }catch (Exception e){
                e.printStackTrace();
            }
            //更新项目分摊表
            sqlQuery.update("DELETE FROM TZ_XMXFFT_TBL");
            //sqlQuery.update("INSERT INTO TZ_XMXFFT_TBL (tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON, TZ_TX_JINE, TZ_JXJ_JINE , TZ_XF_JINE, TZ_OPE_DTTM) SELECT tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON , SUM(ISNULL(TZ_TX_JINE, 0)) AS TZ_TX_JINE , SUM(ISNULL(TZ_JXJ_JINE, 0)) AS TZ_JXJ_JINE , SUM(ISNULL(TZ_XF_JINE, 0)) AS TZ_XF_JINE , GETDATE() AS TZ_OPE_DTTM FROM TZ_XYXFFT_TBL GROUP BY tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON");
            //sqlQuery.update("INSERT INTO TZ_XMXFFT_TBL(tzms_pro_nbid,TZ_TX_YEAR,TZ_TX_MON,TZ_XF_JINE,TZ_JXJ_JINE,TZ_TX_JINE,TZ_OPE_OPRID,TZ_OPE_DTTM,TZ_SYNC_STA) SELECT C.TZ_LXBH AS tzms_pro_nbid, SUBSTRING(A.TZ_DATE, 1, 4) AS TZ_TX_YEAR , SUBSTRING(A.TZ_DATE, 5, 6) AS TZ_TX_MON , SUM(ISNULL(A.TZ_XF_JINE, 0)) AS TZ_XF_JINE , SUM(ISNULL(A.TZ_JXJ_JINE, 0)) AS TZ_JXJ_JINE , SUM(ISNULL(A.TZ_XF_JINE, 0) - ISNULL(A.TZ_JXJ_JINE, 0)) AS TZ_TX_JINE , 'TZ_26960' AS TZ_OPE_OPRID, GETDATE() AS TZ_OPE_DTTM, 'Y' AS TZ_SYNC_STA FROM TZ_XYXFFT_TEMP A, PS_TZ_FORM_WRK_T F, PS_TZ_CLASS_INF_T C WHERE A.TZ_ID = F.TZ_MSH_ID AND F.TZ_CLASS_ID = C.TZ_CLASS_ID GROUP BY C.TZ_LXBH, A.TZ_DATE");
            //优化，更新内训及立项项目
            sqlQuery.update("INSERT INTO TZ_XMXFFT_TBL (tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON, TZ_XF_JINE, TZ_JXJ_JINE , TZ_TX_JINE, TZ_OPE_OPRID, TZ_OPE_DTTM, TZ_SYNC_STA) SELECT C.TZ_LXBH AS tzms_pro_nbid, SUBSTRING(A.TZ_DATE, 1, 4) AS TZ_TX_YEAR , SUBSTRING(A.TZ_DATE, 5, 6) AS TZ_TX_MON , SUM(ISNULL(A.TZ_XF_JINE, 0)) AS TZ_XF_JINE , SUM(ISNULL(A.TZ_JXJ_JINE, 0)) AS TZ_JXJ_JINE , SUM(ISNULL(A.TZ_XF_JINE, 0) - ISNULL(A.TZ_JXJ_JINE, 0)) AS TZ_TX_JINE , 'TZ_26960' AS TZ_OPE_OPRID, GETDATE() AS TZ_OPE_DTTM, 'Y' AS TZ_SYNC_STA FROM TZ_XYXFFT_TEMP A, PS_TZ_FORM_WRK_T F, PS_TZ_CLASS_INF_T C WHERE A.TZ_ID = F.TZ_MSH_ID AND F.TZ_CLASS_ID = C.TZ_CLASS_ID GROUP BY C.TZ_LXBH, A.TZ_DATE UNION SELECT tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON , SUM(ISNULL(TZ_XF_JINE, 0)) AS TZ_XF_JINE , SUM(ISNULL(TZ_JXJ_JINE, 0)) AS TZ_JXJ_JINE , SUM(ISNULL(TZ_TX_JINE, 0)) AS TZ_TX_JINE , 'TZ_26960' AS TZ_OPE_OPRID, GETDATE() AS TZ_OPE_DTTM, 'Y' AS TZ_SYNC_STA FROM TZ_XYXFFT_TBL GROUP BY tzms_pro_nbid, TZ_TX_YEAR, TZ_TX_MON");
        }

    }

}
