package com.tranzvision.gd.util.captcha;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ：张超
 * @date ：Created in 2019/11/5 16:13
 * @description：用于登录密码的校验的功能  完成以下校验
 * 校验规则：1.在弱密码口令列表里面  2.密码包含用户名  3.连续数字或字母  4.相同的数字或字母
 * 5.密码必须要有数字和字母。6.密码位数大于6位
 * @modified By：
 * @version: 1.0
 */
public class PasswordCheck {
    private String userName;//用户名

    private String password;//密码

    private String weakPassword;//弱密码

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeakPassword() {
        return weakPassword;
    }

    public void setWeakPassword(String weakPassword) {
        this.weakPassword = weakPassword;
    }

    //构造器
    public PasswordCheck(String userName, String password, String weakPassword){
        this.userName=userName;
        this.password=password;
        this.weakPassword=weakPassword;
    }

    public Boolean weakLoginPassword(){
        GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select TZ_HARDCODE_VAL  from  PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT='TZ_WEAKPASSWORD'");
        String WearkPassword="";
        for (Map<String, Object> map:list){
            WearkPassword=map.get("TZ_HARDCODE_VAL")==null?"":String.valueOf(map.get("TZ_HARDCODE_VAL"));
        }
        if(!"Y".equals(WearkPassword)){//Y标识启用弱密码校验，否则不能校验
            return true;
        }
        //6.密码位数小于6位
        if(password.length()<6){
            return false;
        }
        //1.在弱密码口令列表里面
        if(existWeakPassword()){
            return false;
        }
        // 2.密码包含用户名
        if(StringUtils.contains(password,userName)){
            return  false;
        }
        //3.连续数字或字母
        if(weakContinuity(password)){
            return false;
        }
        //4.相同的数字或字母
        if(ContainEqualsMark(password)){
            return false;
        }
        //5.密码必须要有数字和字母
        if(weakContainLetterAndNum(password)){
            return false;
        }
        return true;
    }
    //校验连续数字和字母的方法
    private  Boolean weakContinuity(String password){
        //密码长度小于2位
        if(password.length()<2){
            return true;
        }
        password=password.toLowerCase();
        //连续的值
        Integer front=null;
        Integer after=null;
        Integer falg=999999;
        Integer derail=0;
        for(int i=1;i<password.length();i++){
            front=Integer.valueOf(password.charAt(i-1));
            after=Integer.valueOf(password.charAt(i));
            if(after-front==falg){
                derail++;
            }else{
                falg=after-front;
                derail=0;
            }
            if(derail>0&&(falg==-1||falg==1)){
                return true;
            }
        }
        return  false;
    }
    //密码中必须包含字母和数字
    private  Boolean weakContainLetterAndNum(String password){
        Integer charNum=null;
        Integer NuM=0;
        Integer Letter=0;
        for (int i=0;i<password.length();i++){
            charNum=Integer.valueOf(password.charAt(i));
            if(charNum>47&&charNum<58){
                NuM=-1;
            }else if((charNum>64&&charNum<90)||(charNum>96&&charNum<123)){
                Letter=-1;
            }
        }
        if(NuM*Letter==1){
            return false;
        }
        return true;
    }
    //密码包含相同的字母和数字
    private  Boolean ContainEqualsMark(String password){
        if(password.length()<3){
            return true;
        }
        byte[] bytes = password.getBytes();
        Arrays.sort(bytes);
        for(int i=2;i<bytes.length;i++){
            if(((bytes[i]>47&&bytes[i]<58)||(bytes[i]>64&&bytes[i]<90)||(bytes[i]>96&&bytes[i]<123))&&(bytes[i]==bytes[i-1]&&bytes[i-1]==bytes[i-2])){
                return true;
            }
        }
        return false;
    }
    //判断弱密码是否已经拥有
    private Boolean existWeakPassword(){
        GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
        String sql="select count(1) as num from PS_TZ_WEAK_PASSWORD where TZ_PWD_VAL=?";
        System.out.println(weakPassword);
//        String string = sqlQuery.queryForObject(sql, new Object[]{"aa"}, "String");
        String string =jdbcTemplate.queryForObject(sql,new Object[]{weakPassword},String.class);
        int num = Integer.parseInt(string);
        return num>0?true:false;
    }
}
