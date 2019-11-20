package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;


import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: ZY
 * @Date: 2019/7/3 17:14
 * @Description: 报名表 高级设置 解析及同步配置数据
 */
@Service
public class TzOnlineAppSyncImpl {
    @Autowired
    public SqlQuery jdbcTemplate;
    //记录日志
    private static final Logger logger = Logger.getLogger("AppSyncSetData");
    /**
     * 基本数据Map
     */
    private Map<String,List<Map<String,Object>>> baseMap;
    /**
     * sql List
     */
    private List<String> sqlList;

    /**
     * 定义一个HashMap 用来存储文本字段长度，防止sqlserver在严格模式下，字符过长无法插入或更新;
     * key ：tableName + "-" + columnName
     * value ：属性：类型（dataType）、长度（length）
     */
    public static HashMap<String,Map<String,Object>>  fieldMap = new HashMap<>();

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public Map<String, List<Map<String, Object>>> getBaseMap() {
        return baseMap;
    }

    public void setBaseMap(Map<String, List<Map<String, Object>>> baseMap) {
        this.baseMap = baseMap;
    }

    public List<String> getSqlList() {
        return sqlList;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }

    /**
     * 报名表 高级配置 同步主函数
     * @param numAppInsId
     * @param strTplId
     * @param strAppOprId
     */
    public void appSyncMain(Long numAppInsId, String strTplId, String strAppOprId){
        this.setBaseMap(new HashMap<>());
        this.setSqlList(new ArrayList<>());
        //查询报名表所属oprid
        String queryOpridSql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? limit 0,1";
        strAppOprId = jdbcTemplate.queryForObject(queryOpridSql,new Object[]{numAppInsId},"String");
        logger.info("【App Sync Begin ...】=============================================================================");
        logger.info("【"+new Date()+"】");
        logger.info("【App Sync : "+ numAppInsId + " tplId : " + strTplId + " oprId : "+ strAppOprId + "】");

        //查询基本表数据 构建基本数据结构
        String baseQuerySql = "SELECT DISTINCT C.TZ_SYNC_TABLE FROM PS_TZ_APPXX_SYNC_T B, PS_TZ_APPSYNC_CONFIG C WHERE B.TZ_QY_BZ = 'Y' AND B.TZ_SYNC_TYPE = C.TZ_APPSYNC_ID AND B.TZ_APP_TPL_ID = ?";
        //List<Map<String,Object>> baseList = sqlQuery.queryForList(baseQuerySql,new Object[]{strTplId});
        List<Map<String,Object>> baseList = jdbcTemplate.queryForList(baseQuerySql,new Object[]{strTplId});
        if(baseList!=null&&baseList.size()>0){
            for (int i = 0; i < baseList.size(); i++) {
                String TZ_SYNC_TABLE = baseList.get(i).get("TZ_SYNC_TABLE") == null ? "" : String.valueOf(baseList.get(i).get("TZ_SYNC_TABLE"));
                if (TZ_SYNC_TABLE.length()>0){
                    baseMap.put(TZ_SYNC_TABLE,new ArrayList<Map<String,Object>>());
                }
            }
        }
        //System.out.println("baseMap"+baseMap);
        //查询所有配置记录
        String mainQuerySql = "SELECT A.TZ_COM_LMC,A.TZ_D_XXX_BH, A.TZ_XXX_BH,A.TZ_XXX_NO,A.TZ_LINE_NUM, B.TZ_SYNC_TYPE, B.TZ_SYNC_SEP, B.TZ_IS_MULTILINE , C.TZ_SYNC_TABLE, C.TZ_SYNC_FILED, A.TZ_XXX_MC, A.TZ_XXX_CCLX FROM PS_TZ_TEMP_FIELD_V A, PS_TZ_APPXX_SYNC_T B, PS_TZ_APPSYNC_CONFIG C WHERE A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND A.TZ_XXX_NO = B.TZ_XXX_BH AND B.TZ_QY_BZ = 'Y' AND B.TZ_SYNC_TYPE <> '' AND B.TZ_SYNC_TYPE = C.TZ_APPSYNC_ID AND C.TZ_IS_ENABLE = '1' AND A.TZ_APP_TPL_ID = ?";
        //List<Map<String,Object>> syncListData = sqlQuery.queryForList(mainQuerySql,new Object[]{strTplId});
        List<Map<String,Object>> syncListData = jdbcTemplate.queryForList(mainQuerySql,new Object[]{strTplId});
        if(syncListData!=null && syncListData.size()>0){
            for (int i = 0; i < syncListData.size(); i++) {
                Map<String,Object> syncMapData = syncListData.get(i);
                String TZ_COM_LMC = syncMapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(syncMapData.get("TZ_COM_LMC"));
                String TZ_D_XXX_BH = syncMapData.get("TZ_D_XXX_BH") == null ? "" : String.valueOf(syncMapData.get("TZ_D_XXX_BH"));
                String TZ_XXX_BH = syncMapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(syncMapData.get("TZ_XXX_BH"));
                String TZ_XXX_NO = syncMapData.get("TZ_XXX_NO") == null ? "" : String.valueOf(syncMapData.get("TZ_XXX_NO"));
                Integer TZ_LINE_NUM = Integer.valueOf(syncMapData.get("TZ_LINE_NUM") == null ? "1" : String.valueOf(syncMapData.get("TZ_LINE_NUM")));
                String TZ_SYNC_TYPE = syncMapData.get("TZ_SYNC_TYPE") == null ? "" : String.valueOf(syncMapData.get("TZ_SYNC_TYPE"));
                String TZ_SYNC_SEP = syncMapData.get("TZ_SYNC_SEP") == null ? "" : String.valueOf(syncMapData.get("TZ_SYNC_SEP"));
                String TZ_IS_MULTILINE = syncMapData.get("TZ_IS_MULTILINE") == null ? "" : String.valueOf(syncMapData.get("TZ_IS_MULTILINE"));
                String TZ_SYNC_TABLE = syncMapData.get("TZ_SYNC_TABLE") == null ? "" : String.valueOf(syncMapData.get("TZ_SYNC_TABLE"));
                String TZ_SYNC_FILED = syncMapData.get("TZ_SYNC_FILED") == null ? "" : String.valueOf(syncMapData.get("TZ_SYNC_FILED"));
                String TZ_XXX_MC = syncMapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(syncMapData.get("TZ_XXX_MC"));
                String TZ_XXX_CCLX = syncMapData.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(syncMapData.get("TZ_XXX_CCLX"));
                //System.out.println(TZ_COM_LMC+"----------"+TZ_D_XXX_BH+"----------"+TZ_XXX_BH+"----------"+TZ_XXX_NO+"------------"+TZ_LINE_NUM);

                try{
                    String index = TZ_XXX_BH.substring((TZ_D_XXX_BH+TZ_XXX_NO).length(),TZ_XXX_BH.length());
                    Integer num = 1;
                    if(index.length()>0){
                        num = Integer.valueOf(index.substring(1,index.length()))+1;
                    }
                    String value = this.analysisData(numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                            TZ_SYNC_SEP, TZ_IS_MULTILINE,TZ_XXX_MC,TZ_XXX_CCLX);
                    if(num>baseMap.get(TZ_SYNC_TABLE).size()){
                        int addCount = num - baseMap.get(TZ_SYNC_TABLE).size();
                        for (int j = 0; j < addCount; j++) {
                            baseMap.get(TZ_SYNC_TABLE).add(new HashMap<>());
                        }
                    }
                    baseMap.get(TZ_SYNC_TABLE).get(num-1).put(TZ_SYNC_FILED,value);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("【Base Analysis Err】:"+e);
                }
            }

        }
        //System.out.println(baseMap);
        //this.setBaseMap(baseMap);
        this.createSqlData(numAppInsId,strTplId,strAppOprId);
        this.saveSyncData(numAppInsId,strTplId,strAppOprId);
    }

    /**
     * 校验数据格式及长度，确保数据可以插入及更新
     * @param tableName
     * @param columnName
     * @param value
     * @return
     */
    private String checkField(String tableName, String columnName, String value){
        try{
            Map<String,Object> attributeMap = TzOnlineAppSyncImpl.fieldMap.get(tableName.toUpperCase() + "-" + columnName.toUpperCase());
            if(attributeMap != null){
                String dataType = String.valueOf(attributeMap.get("dataType"));
                int length = attributeMap.get("length") == null ? 0: Integer.parseInt(String.valueOf(attributeMap.get("length")));
                if(dataType.toLowerCase().contains("char")){
                    try{
                        value = this.splitString(value,length);
                    }catch (UnsupportedEncodingException e){
                        logger.error("【splitString Err】:"+e.toString());
                    }
                }else if("date".equals(dataType.toLowerCase())){
                    try{
                        value = dateFormat.format(dateFormat.parse(value));
                    }catch (Exception e){
                        logger.info("【checkField dateFormat ERROR ...】  "+e.toString());
                        value = dateFormat.format(new Date());
                    }
                }

            }else {
                String queryFieldSql = "SELECT CONCAT(UPPER(TABLE_NAME),'-',UPPER(COLUMN_NAME)) keyName,LOWER(DATA_TYPE) " +
                        "AS dataType,CHARACTER_MAXIMUM_LENGTH AS length FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_NAME = ? AND COLUMN_NAME = ? " ;
                Map<String,Object> queryMap = jdbcTemplate.queryForMap(queryFieldSql,new Object[]{tableName,columnName});
                if(queryMap != null){
                    String keyName = String.valueOf(queryMap.get("keyName"));
                    String dataType = String.valueOf(queryMap.get("dataType"));
                    int length = queryMap.get("length") == null ? 0 : Integer.parseInt(String.valueOf(queryMap.get("length")));
                    Map<String,Object> attributeAddMap = new HashMap<>();
                    attributeAddMap.put("dataType",dataType);
                    attributeAddMap.put("length",length);
                    TzOnlineAppSyncImpl.fieldMap.put(keyName,attributeAddMap);
                    return  checkField(tableName,columnName,value);
                }
            }
        }catch (Exception e){
            logger.info("【checkField ERROR ...】  "+e.toString());
        }
        return value;
    }


    /**
     * 按字节截取字符串，不截取半个中文
     * @param str
     * @param size
     * @return
     */
    public static String splitString(String str,int size) throws UnsupportedEncodingException {
        //默认UTF-8 中文一般三个字节表示，gbk两个字节，UTF-8变化就是%3，gbk变化就是%3
        byte[] bytes = str.getBytes("GBK");
        //如果截取长度大于bytes长度，则直接打印字符串
        if(size >= bytes.length){
            return str;
        }
        //如果是中文，bytes为负数。最后一个不是中文，则直接分割
        if(bytes[size-1] > 0){
            String splitString = new String(bytes,0,size,"GBK");
            return splitString;
        }
        //字节负数统计并进行求模​
        int num = 0;
        //循环到需要分割的长度，后面的不需要
        for(int i = 0 ; i < size; i++ ){
            if(bytes[i]<0){
                num++;
                num = num % 2;
            }
        }
        String splitString = new String(bytes,0,size-num,"GBK");
        return splitString;
    }

    /**
     * 读取数据，创建sql语句
     */
    private void createSqlData(Long numAppInsId, String strTplId, String strAppOprId){
        try{
            System.out.println("createSqlData Start");
            for (Map.Entry<String, List<Map<String,Object>>> entry : baseMap.entrySet()) {
                String tableName =  entry.getKey();
                System.out.println("tableName=====>"+tableName);
                //查询表是否为多行容器表，若是增加序号字段 TZ_SEQ
                Integer multiLineTable = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM information_schema.`COLUMNS` a,information_schema.`TABLES` b " +
                        "WHERE b.TABLE_SCHEMA = 'TZGDBASE' AND b.TABLE_TYPE = 'BASE TABLE' AND a.TABLE_NAME = b.TABLE_NAME AND b.TABLE_NAME=? " +
                        "AND a.COLUMN_NAME = 'TZ_SEQ'",new Object[]{tableName},"Integer");
                if(tableName!=null){
                    if(!"PS_TZ_REG_USER_T".equals(tableName)){
                        //如果非注册表，清除所有历史数据
                        jdbcTemplate.update("delete "+tableName+" where TZ_APP_INS_ID=?",new Object[]{numAppInsId});
                    }
                    List<Map<String,Object>> insertListData = entry.getValue();
                    if(insertListData.size()>0){
                        for (int i = 0; i < insertListData.size(); i++) {
                            Map<String,Object> insertMapData = insertListData.get(i);
                            //对注册表性别，证件类型做特殊化处理
                            if("PS_TZ_REG_USER_T".equals(tableName)){
                                Boolean tzGenderIsExists = insertMapData.containsKey("TZ_GENDER");
                                Boolean nationalIdTypeIsExists = insertMapData.containsKey("NATIONAL_ID_TYPE");
                                String queryTzYhzcId = "SELECT A.TZ_OPT_ID FROM PS_TZ_YHZC_XXZ_TBL A LEFT JOIN PS_TZ_REG_USER_T B ON A.TZ_SITEI_ID = B.TZ_SITEI_ID LEFT JOIN PS_TZ_AQ_YHXX_TBL C ON A.TZ_JG_ID = C.TZ_JG_ID WHERE B.OPRID = C.OPRID AND B.OPRID = ? AND A.TZ_REG_FIELD_ID = ? AND A.TZ_OPT_VALUE = ?";
                                if(tzGenderIsExists){
                                    String TZ_GENDER = jdbcTemplate.queryForObject(queryTzYhzcId,new Object[]{strAppOprId,"TZ_GENDER",insertMapData.get("TZ_GENDER")},"String");
                                    insertMapData.replace("TZ_GENDER",TZ_GENDER == null ? "" : TZ_GENDER);
                                }
                                if(nationalIdTypeIsExists){
                                    String NATIONAL_ID_TYPE = jdbcTemplate.queryForObject(queryTzYhzcId,new Object[]{strAppOprId,"NATIONAL_ID_TYPE",insertMapData.get("NATIONAL_ID_TYPE")},"String");
                                    insertMapData.replace("NATIONAL_ID_TYPE",NATIONAL_ID_TYPE == null ? "" : NATIONAL_ID_TYPE);
                                }
                            }
                            //结束
                            List<String> keyList = new ArrayList<>();
                            List<Object> valueList = new ArrayList<>();
                            for (Map.Entry<String, Object> insertMap : insertMapData.entrySet()) {
                                String key = insertMap.getKey();
                                Object value = insertMap.getValue();
                                if(key.length() > 0 && String.valueOf(value).length() > 0){
                                    keyList.add(key);
                                    valueList.add(this.checkField(tableName,key, String.valueOf(value)));
                                }
                            }
                            //判断用户是否填写内容，对于多行容器，未填写内容则不生成Sql
                            Boolean flag = true;
                            int strCount = 0;
                            int valueCount = valueList.size();
                            for (Object str:valueList) {
                                if( String.valueOf(str).length()==0){
                                    strCount++;
                                }
                            }
                            if(valueCount>strCount){
                                //判断表记录是否存在，存在则更新表；不存在新增
                                Integer count = 0;
                                if("PS_TZ_REG_USER_T".equals(tableName)){
                                    //对PS_TZ_REG_USER_T做特殊化处理
                                    count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_REG_USER_T WHERE OPRID = ?",new Object[]{strAppOprId},"Integer");
                                }else {
                                    /*//判断是否是多条记录
                                    if(multiLineTable > 0){
                                        count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM "+tableName+" WHERE TZ_APP_INS_ID = ? AND TZ_SEQ = ?",new Object[]{numAppInsId,i+1},"Integer");
                                    }else {
                                        count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM "+tableName+" WHERE TZ_APP_INS_ID = ?",new Object[]{numAppInsId},"Integer");
                                    }*/

                                }
                                //判断更新还是新增
                                if(count!=null && count>0){
                                    //更新
                                    StringBuffer updateSql = new StringBuffer();
                                    updateSql.append("UPDATE ");
                                    updateSql.append(tableName);
                                    updateSql.append(" SET ");
                                    for (int j = 0; j < keyList.size(); j++) {
                                        if(j>0){
                                            updateSql.append(",");
                                        }
                                        updateSql.append(keyList.get(j));
                                        updateSql.append("='");
                                        updateSql.append(valueList.get(j));
                                        updateSql.append("'");
                                    }
                                    if("PS_TZ_REG_USER_T".equals(tableName)){
                                        //对PS_TZ_REG_USER_T做特殊化处理
                                        updateSql.append(" WHERE OPRID = '");
                                        updateSql.append(strAppOprId);
                                        updateSql.append("'");
                                    }else {
                                        updateSql.append(" WHERE TZ_APP_INS_ID = '");
                                        updateSql.append(numAppInsId);
                                        updateSql.append("'");
                                        if(multiLineTable > 0){
                                            // 多行容器根据顺序更新
                                            updateSql.append(" AND TZ_SEQ = '");
                                            updateSql.append(i+1);
                                            updateSql.append("'");
                                        }
                                    }

                                    this.getSqlList().add(String.valueOf(updateSql));
                                }else {
                                    //新增
                                    StringBuffer insertSql = new StringBuffer();
                                    insertSql.append("INSERT INTO ");
                                    insertSql.append(tableName);
                                    insertSql.append("(");
                                    if("PS_TZ_REG_USER_T".equals(tableName)){
                                        insertSql.append("OPRID");
                                    }else {
                                        insertSql.append("TZ_APP_INS_ID");
                                    }

                                    for (int j = 0; j < keyList.size(); j++) {
                                        insertSql.append(",");
                                        insertSql.append(keyList.get(j));
                                    }
                                    //多行容器应保存顺序
                                    if(multiLineTable > 0){
                                        insertSql.append(",");
                                        insertSql.append("TZ_SEQ");
                                    }
                                    insertSql.append(") ");
                                    insertSql.append("VALUES('");
                                    if("PS_TZ_REG_USER_T".equals(tableName)){
                                        insertSql.append(strAppOprId);
                                    }else {
                                        insertSql.append(numAppInsId);
                                    }
                                    insertSql.append("'");
                                    for (int j = 0; j < valueList.size(); j++) {
                                        insertSql.append(",'");
                                        insertSql.append(valueList.get(j));
                                        insertSql.append("'");
                                    }
                                    //多行容器应保存顺序
                                    if(multiLineTable > 0){
                                        insertSql.append(",'");
                                        insertSql.append((i+1));
                                        insertSql.append("'");
                                    }
                                    insertSql.append(")");
                                    this.getSqlList().add(String.valueOf(insertSql));
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("createSqlData End");
        }catch (Exception e){
            logger.info("【CREATE SQL ERROR ...】  "+e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 同步SQL数据
     */
    private void saveSyncData(Long numAppInsId, String strTplId, String strAppOprId){
        //清空历史数据
        /*for (String tableName : baseMap.keySet()){
            String deleteSql = "DELETE FROM "+tableName+" WHERE TZ_APP_INS_ID = ?";
            jdbcTemplate.update(deleteSql,new Object[]{numAppInsId});
        }*/
        logger.info("【APP SQL Execute Begin ...】  "+numAppInsId);
        if(sqlList!=null && sqlList.size()>0){
            for (int i = 0; i < sqlList.size(); i++) {
                try{
                    //System.out.println(i+"：   "+sqlList.get(i));
                    logger.info("【APP :"+numAppInsId+" SQL】"+ i + " : " + sqlList.get(i));
                    //执行Sql
                    jdbcTemplate.update(sqlList.get(i));
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("【APP :"+numAppInsId+" SQL】"+ i + " : " + e.toString());
                }

            }
        }
        logger.info("【App Sync End ...】===================================================================");
        this.setBaseMap(new HashMap<>());
        this.setSqlList(new ArrayList<>());
    }


    /**
     * 解析所有组件
     * @param numAppInsId
     * @param strTplId
     * @param strAppOprId
     * @return
     */
    private String analysisData(Long numAppInsId, String strTplId, String strAppOprId,
                               String TZ_COM_LMC, String TZ_D_XXX_BH, String TZ_XXX_BH, String TZ_XXX_NO,
                               Integer TZ_LINE_NUM,String TZ_SYNC_SEP, String TZ_IS_MULTILINE, String TZ_XXX_MC, String TZ_XXX_CCLX){
        String value = "";
        try{
            if(TZ_COM_LMC.length()>0 && TZ_XXX_BH.length()>0){
                switch (TZ_COM_LMC){
                    case "SingleTextBox":
                        //处理单行文本框
                        String querySingleTextBoxSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(querySingleTextBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "DigitalTextBox":
                        //处理数字文本框
                        String queryDigitalTextBoxSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryDigitalTextBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "autoComplete":
                        //处理自动补全
                        String queryAutoCompleteSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryAutoCompleteSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "MultilineTextBox":
                        //处理多行文本框
                        String queryMultilineTextBoxSql = "SELECT TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryMultilineTextBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "DateInputBox":
                        //日期输入框
                        String queryDateInputBoxSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryDateInputBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "Radio":
                        //单选框
                        String queryRadioSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED = ?";
                        value = jdbcTemplate.queryForObject(queryRadioSql,new Object[]{numAppInsId,TZ_XXX_BH,"Y"},"String");
                        break;
                    case "Check":
                        //多选框
                        if(TZ_SYNC_SEP.length()==0)TZ_SYNC_SEP = "-";
                        String queryCheckSql = "SELECT STUFF((SELECT '"+TZ_SYNC_SEP+"'+TZ_APP_S_TEXT from PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED = ? for xml path('')),1,"+TZ_SYNC_SEP.length()+",'') AS TZ_APP_S_TEXT";
                        value = jdbcTemplate.queryForObject(queryCheckSql,new Object[]{numAppInsId,TZ_XXX_BH,"Y"},"String");
                        break;
                    case "CheckBox":
                        //复选框
                        String queryCheckBoxSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryCheckBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        if(value!=null && "Y".equals(value)){
                            value = "已选中" + TZ_XXX_MC;
                        }else {
                            value =  "未选中" + TZ_XXX_MC;
                        }
                        break;
                    case "Select":
                        //下拉框
                        String querySelectSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T A, PS_TZ_APP_CC_T B WHERE A.TZ_XXXKXZ_MC = B.TZ_APP_S_TEXT AND B.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH = ? AND B.TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(querySelectSql,new Object[]{numAppInsId,strTplId,TZ_XXX_NO,TZ_XXX_BH},"String");
                        break;
                    case "doubleSelect":
                        //双层联动下拉框
                        value = this.analysisdoubleSelect( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "imagesUpload":
                        //图片上传
                        String queryImagesUploadSql = "SELECT ATTACHUSERFILE,ATTACHSYSFILENAME,TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_INDEX";
                        List<Map<String,Object>> imagesList = jdbcTemplate.queryForList(queryImagesUploadSql,new Object[]{numAppInsId,TZ_XXX_BH});
                        if(imagesList!=null &&imagesList.size()>0){
                            for (int i = 0; i < imagesList.size(); i++) {
                                String ATTACHUSERFILE = imagesList.get(i).get("ATTACHUSERFILE") == null ? "": String.valueOf(imagesList.get(i).get("ATTACHUSERFILE"));
                                String ATTACHSYSFILENAME = imagesList.get(i).get("ATTACHSYSFILENAME") == null ? "": String.valueOf(imagesList.get(i).get("ATTACHSYSFILENAME"));
                                String TZ_ACCESS_PATH = imagesList.get(i).get("TZ_ACCESS_PATH") == null ? "": String.valueOf(imagesList.get(i).get("TZ_ACCESS_PATH"));
                                value += "序号："+(i+1) + "，文件名：" + ATTACHUSERFILE + "，文件地址："+TZ_ACCESS_PATH+ATTACHSYSFILENAME + "；\n";
                            }
                        }
                        break;
                    case "AttachmentUpload":
                        //附件上传
                        String queryAttachmentUploadSql = "SELECT ATTACHUSERFILE,ATTACHSYSFILENAME,TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? ORDER BY TZ_INDEX";
                        List<Map<String,Object>> attachmentList = jdbcTemplate.queryForList(queryAttachmentUploadSql,new Object[]{numAppInsId,TZ_XXX_BH});
                        if(attachmentList!=null &&attachmentList.size()>0){
                            for (int i = 0; i < attachmentList.size(); i++) {
                                String ATTACHUSERFILE = attachmentList.get(i).get("ATTACHUSERFILE") == null ? "": String.valueOf(attachmentList.get(i).get("ATTACHUSERFILE"));
                                String ATTACHSYSFILENAME = attachmentList.get(i).get("ATTACHSYSFILENAME") == null ? "": String.valueOf(attachmentList.get(i).get("ATTACHSYSFILENAME"));
                                String TZ_ACCESS_PATH = attachmentList.get(i).get("TZ_ACCESS_PATH") == null ? "": String.valueOf(attachmentList.get(i).get("TZ_ACCESS_PATH"));
                                value += "序号："+(i+1) + "，文件名：" + ATTACHUSERFILE + "，文件地址："+TZ_ACCESS_PATH+ATTACHSYSFILENAME + "；\n";
                            }
                        }
                        break;
                    case "VerificationCode":
                        //处理验证码框
                        String queryVerificationCodeBoxSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryVerificationCodeBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "RecommendTeacher":
                        //处理推荐老师框
                        String queryRecommendTeacherSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryRecommendTeacherSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "EnglishAlphabet":
                        //处理字母框
                        String queryEnglishAlphabetSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryEnglishAlphabetSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "Nationality":
                        //国家选择器
                        String queryNationalitySql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryNationalitySql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "Province":
                        //国家选择器
                        String queryProvinceSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryProvinceSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "City":
                        //城市选择器
                        String queryCitySql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryCitySql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                        break;
                    case "BirthdayAndAge":
                        //生日年龄组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "CertificateNum":
                        //证件组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "mobilePhone":
                        //通讯组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "MailingAddress":
                        //地址组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "YearsAndMonth":
                        //年月组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "DateComboBox":
                        //日期组合框
                        value = this.analysisDateComboBoxData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "Diploma":
                        //学历
                        String queryDiplomaSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T A, PS_TZ_APP_CC_T B WHERE A.TZ_XXXKXZ_MC = B.TZ_APP_S_TEXT AND B.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH = ? AND B.TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryDiplomaSql,new Object[]{numAppInsId,strTplId,TZ_XXX_NO,TZ_XXX_BH},"String");
                        break;
                    case "Degree":
                        //学位
                        String queryDegreeSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T A, PS_TZ_APP_CC_T B WHERE A.TZ_XXXKXZ_MC = B.TZ_APP_S_TEXT AND B.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH = ? AND B.TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryDegreeSql,new Object[]{numAppInsId,strTplId,TZ_XXX_NO,TZ_XXX_BH},"String");
                        break;
                    case "CompanyNature":
                        //公司性质
                        String queryCompanyNatureSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T A, PS_TZ_APP_CC_T B WHERE A.TZ_XXXKXZ_MC = B.TZ_APP_S_TEXT AND B.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH = ? AND B.TZ_XXX_BH = ?";
                        value = jdbcTemplate.queryForObject(queryCompanyNatureSql,new Object[]{numAppInsId,strTplId,TZ_XXX_NO,TZ_XXX_BH},"String");
                        break;
                    case "DegreeAndDiploma":
                        //学历学位组合框
                        // js组件存在错误 组合控件中上传附件属性为SingleTextBox 错误 暂搁置
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "University":
                        //院校选择器组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "EngLevl":
                        //英语水平组合框
                        //js组件存在错误 组合控件中组件 错误 暂搁置
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    case "workExperience":
                        //工作经历组合框
                        value = this.analysisComboData( numAppInsId,strTplId, strAppOprId,TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,TZ_LINE_NUM,
                                TZ_SYNC_SEP, TZ_IS_MULTILINE, TZ_XXX_MC, TZ_XXX_CCLX);
                        break;
                    default:
                        value = "";
                        break;
                }
            }
        }catch (Exception e){
            logger.info("【analysisData ERROR ...】  "+e.toString());
            e.printStackTrace();
        }
        value = value == null ? "" : value;
        //System.out.println("value:"+value);
        return value;
    }

    /**
     * 解析基本组合框
     * @param numAppInsId
     * @param strTplId
     * @param strAppOprId
     * @return
     */
    private String analysisComboData(Long numAppInsId, String strTplId, String strAppOprId,String TZ_COM_LMC, String TZ_D_XXX_BH, String TZ_XXX_BH, String TZ_XXX_NO,
                                    Integer TZ_LINE_NUM,String TZ_SYNC_SEP, String TZ_IS_MULTILINE,String TZ_XXX_MC, String TZ_XXX_CCLX){
        String value = "";
        try{
            String queryComboSql = "SELECT A.TZ_XXX_BH, B.TZ_COM_LMC,B.TZ_XXX_NO,B.TZ_XXX_CCLX FROM PS_TZ_APP_CC_T A, PS_TZ_TEMP_FIELD_V B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID = ? AND B.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH LIKE '"+TZ_D_XXX_BH+TZ_XXX_NO+"%' AND B.TZ_LINE_NUM = ? ORDER BY B.TZ_LINE_ORDER";
            List<Map<String,Object>> comboList = jdbcTemplate.queryForList(queryComboSql,new Object[]{numAppInsId,strTplId,TZ_LINE_NUM});
            if(comboList!=null && comboList.size()>0){
                for (int i = 0; i < comboList.size(); i++) {
                    TZ_XXX_BH =  comboList.get(i).get("TZ_XXX_BH") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_BH"));
                    TZ_COM_LMC =  comboList.get(i).get("TZ_COM_LMC") == null ? "" : String.valueOf(comboList.get(i).get("TZ_COM_LMC"));
                    TZ_XXX_NO =  comboList.get(i).get("TZ_XXX_NO") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_NO"));
                    TZ_XXX_CCLX =  comboList.get(i).get("TZ_XXX_CCLX") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_CCLX"));
                    //String singleValue = this.analysisData(numAppInsId, strTplId,strAppOprId, TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,
                    //TZ_LINE_NUM, TZ_SYNC_SEP, TZ_IS_MULTILINE,TZ_XXX_MC, TZ_XXX_CCLX);
                    if(value.length()==0){
                        value = this.analysisData(numAppInsId, strTplId,strAppOprId, TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,
                                TZ_LINE_NUM, TZ_SYNC_SEP, TZ_IS_MULTILINE,TZ_XXX_MC, TZ_XXX_CCLX);
                    }else {
                        value += TZ_SYNC_SEP + this.analysisData(numAppInsId, strTplId,strAppOprId, TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,
                                TZ_LINE_NUM, TZ_SYNC_SEP, TZ_IS_MULTILINE,TZ_XXX_MC, TZ_XXX_CCLX);
                    }
                }
            }
        }catch (Exception e){
            logger.info("【analysisComboData ERROR ...】  "+e.toString());
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 解析特殊组合框  DateComboBox ：初始日期，结束日期，是否至今
     * @param numAppInsId
     * @param strTplId
     * @param strAppOprId
     * @return
     */
    private String analysisDateComboBoxData(Long numAppInsId, String strTplId, String strAppOprId,String TZ_COM_LMC, String TZ_D_XXX_BH, String TZ_XXX_BH, String TZ_XXX_NO,
                                           Integer TZ_LINE_NUM,String TZ_SYNC_SEP, String TZ_IS_MULTILINE,String TZ_XXX_MC, String TZ_XXX_CCLX){
        String value = "";
        try{
            String queryComboSql = "SELECT A.TZ_XXX_BH, B.TZ_COM_LMC,B.TZ_XXX_NO,B.TZ_XXX_CCLX FROM PS_TZ_APP_CC_T A, PS_TZ_TEMP_FIELD_V B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID = ? AND B.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH LIKE '"+TZ_D_XXX_BH+TZ_XXX_NO+"%' AND B.TZ_LINE_NUM = ? ORDER BY B.TZ_LINE_ORDER";
            List<Map<String,Object>> comboList = jdbcTemplate.queryForList(queryComboSql,new Object[]{numAppInsId,strTplId,TZ_LINE_NUM});
            String data[] = new String[comboList == null ? 0 :comboList.size()];
            if(comboList!=null && comboList.size()>0){
                for (int i = 0; i < comboList.size(); i++) {
                    TZ_XXX_BH =  comboList.get(i).get("TZ_XXX_BH") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_BH"));
                    TZ_COM_LMC =  comboList.get(i).get("TZ_COM_LMC") == null ? "" : String.valueOf(comboList.get(i).get("TZ_COM_LMC"));
                    TZ_XXX_NO =  comboList.get(i).get("TZ_XXX_NO") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_NO"));
                    TZ_XXX_CCLX =  comboList.get(i).get("TZ_XXX_CCLX") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_CCLX"));
                    data[i] = this.analysisData(numAppInsId, strTplId,strAppOprId, TZ_COM_LMC, TZ_D_XXX_BH, TZ_XXX_BH, TZ_XXX_NO,
                            TZ_LINE_NUM, TZ_SYNC_SEP, TZ_IS_MULTILINE,TZ_XXX_MC, TZ_XXX_CCLX);
                }
            }
            String startDate = data[0];
            String endDate = data[1];
            String noDate = data[2];
            if(startDate.length()==0){
                startDate = "起始日期为空";
            }
            if(endDate.length()==0){
                endDate = "结束日期为空";
            }
            if(noDate.contains("已选中")){
                value = startDate + TZ_SYNC_SEP + "至今";
            }else {
                value = startDate + TZ_SYNC_SEP + endDate;
            }
        }catch (Exception e){
            logger.info("【analysisDateComboBoxData ERROR ...】  "+e.toString());
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 解析特殊组合框  doubleSelect ：双层联动下拉框
     * @param numAppInsId
     * @param strTplId
     * @param strAppOprId
     * @return
     */
    private String analysisdoubleSelect(Long numAppInsId, String strTplId, String strAppOprId,String TZ_COM_LMC, String TZ_D_XXX_BH, String TZ_XXX_BH, String TZ_XXX_NO,
                                            Integer TZ_LINE_NUM,String TZ_SYNC_SEP, String TZ_IS_MULTILINE,String TZ_XXX_MC, String TZ_XXX_CCLX){
        String value = "";
        try{
            String queryComboSql = "SELECT A.TZ_XXX_BH, B.TZ_COM_LMC,B.TZ_XXX_NO,B.TZ_XXX_CCLX FROM PS_TZ_APP_CC_T A, PS_TZ_TEMP_FIELD_V B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID = ? AND B.TZ_APP_TPL_ID = ? AND A.TZ_XXX_BH LIKE '"+TZ_D_XXX_BH+TZ_XXX_NO+"%' AND B.TZ_LINE_NUM = ? ORDER BY B.TZ_LINE_ORDER";
            List<Map<String,Object>> comboList = jdbcTemplate.queryForList(queryComboSql,new Object[]{numAppInsId,strTplId,TZ_LINE_NUM});
            String queryLTextBoxSql = "SELECT TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
            if(comboList!=null && comboList.size()>0){
                for (int i = 0; i < comboList.size(); i++) {
                    TZ_XXX_BH =  comboList.get(i).get("TZ_XXX_BH") == null ? "" : String.valueOf(comboList.get(i).get("TZ_XXX_BH"));
                    String content = jdbcTemplate.queryForObject(queryLTextBoxSql,new Object[]{numAppInsId,TZ_XXX_BH},"String");
                    if(content!=null && content.length()>0){
                        if(value.length()==0){
                            value += content;
                        }else {
                            value += TZ_SYNC_SEP + content;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.info("【analysisdoubleSelect ERROR ...】  "+e.toString());
            e.printStackTrace();
        }
        return value;
    }
}
