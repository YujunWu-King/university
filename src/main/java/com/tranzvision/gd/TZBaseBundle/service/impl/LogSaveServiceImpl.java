package com.tranzvision.gd.TZBaseBundle.service.impl;


import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**

/**
 *@ClassName: LogSaveServiceImpl
 *@Description:将日志记录到数据库
 *@Author: JJL
 *@Date: 2019/12/27 16:01
 */
@Service("com.tranzvision.gd.TZBaseBundle.service.impl.LogSaveServiceImpl")
public class LogSaveServiceImpl {
    @Autowired
    private SqlQuery jdbcTemplate;

    /**
     * @Author: JJL
     * @Description:保存日志到数据库
     * @Date: 2019/12/27 16:11
     * @Param: [OPRID, userType, inputParam, outputParam, comID, pageID, operateType]
     * @Return: void
     **/
    public void SaveLogToDataBase(String OPRID,String inputParam,String outputParam,String result,String failReason,String comID,String pageID,String operateType){
        try{
            System.out.println("enter SaveLogToDataBase");
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateStr=sdf.format(date);
            int month = cal.get(Calendar.MONTH)+1;
            String currentMonthStr=String.valueOf(month);
            if(currentMonthStr.length()==1){
                currentMonthStr="0"+currentMonthStr;
            }
            //用户类型
            String userType="";
            //根据OPRID查询用户类型
            String getUserType="SELECT TZ_RYLX FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
            String TZ_RYLX=jdbcTemplate.queryForObject(getUserType,new Object[]{OPRID},"String");
            //注册用户
            if("ZCYH".equals(TZ_RYLX)){
                userType="注册用户";
            }
            //内部用户
            if("NBYH".equals(TZ_RYLX)){
                String getRole="select ROLENAME from PSROLEUSER where ROLEUSER=?";
                List<String> roleList=jdbcTemplate.queryForList(getRole,new Object[]{OPRID},"String");
                if(roleList!=null&&roleList.size()>0){
                    for (int i = 0; i <roleList.size() ; i++) {
                        //面试评委
                        if("TZ_MSPW".equals(roleList.get(i))){
                            userType="面试评委";
                            break;
                        }
                        //材料评委
                        if("TZ_CLPW".equals(roleList.get(i))){
                            userType="材料评委";
                            break;
                        }
                        //面试秘书
                        if("TZ_MSMS".equals(roleList.get(i))){
                            userType="面试秘书";
                            break;
                        }
                        //管理员
                        if(roleList.get(i).contains("GLY")){
                            userType="管理员";
                            break;
                        }
                    }
                }
            }
            String insertsql="insert into PS_TZ_LOG"+currentMonthStr+"(ADDED_DTTM,OPRID,USER_TYPE,INPUT_PARAM,OUTPUT_PARAM,RESULT,FAILURE_REASON,TZ_COM_ID,TZ_PAGE_ID,OPERATE_TYPE)" +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(insertsql,new Object[]{currentDateStr,OPRID,userType,inputParam,outputParam,result,failReason,comID,pageID,operateType});
            System.out.println("end SaveLogToDataBase");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
