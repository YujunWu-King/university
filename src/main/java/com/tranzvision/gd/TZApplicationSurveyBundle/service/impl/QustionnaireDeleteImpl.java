package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：张超
 * @date ：Created in 2019/12/4 15:56
 * @description：用于调查问卷管理中的删除按钮功能，只删除非进行中的数据
 * @modified By：2019年12月4日14:51:47
 * @version: 1.0
 * Comid: TZ_ZXDC_WJGL_COM           pageId: TZ_ZXDC_DELETE_STD
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QustionnaireDeleteImpl")
public class QustionnaireDeleteImpl extends FrameworkImpl {
    @Autowired
    SqlQuery sqlQuery;
    /**
     * 调查问卷管理删除未开始数据
     */
    public String tzDelete(String[] actData, String[] errMsg) {
        String strRet = "";
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            int dataLength = actData.length;
            Boolean flag=true;
            for (int num = 0; num < dataLength; num++) {

                // 表单内容
                String strForm = actData[num];
                // 解析json
                jacksonUtil.json2Map(strForm);

                //获取问卷ID
                String  wjID = jacksonUtil.getString("TZ_DC_WJ_ID");
                String sqlZT="SELECT TZ_DC_WJ_ZT  FROM TZ_ZXDC_WJ_VW WHERE TZ_DC_WJ_ID=?";
                String  ZT = sqlQuery.queryForObject(sqlZT, new Object[]{wjID}, "String");
                if(!StringUtils.equals(ZT,"1")){
                    String sql="DELETE FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID=?";
                    int update = sqlQuery.update(sql, new Object[]{wjID});
                }else{
                    flag=false;
                }
            }
            if(flag){
                errMsg[0] = "0";
                errMsg[1] = "删除成功";
            }else{
                errMsg[0] = "2";
                errMsg[1] = "部分数据删除失败,存在正在进行中的在线调查";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg[0] = "1";
            errMsg[1] = "删除失败。" + e.getMessage();
        }

        return strRet;
    }
}
