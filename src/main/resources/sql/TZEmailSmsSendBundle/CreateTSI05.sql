select a.TZ_DYNAMIC_FLAG , a.TZ_SMS_SERV_ID,a.TZ_SMS_CONTENT, a.TZ_YMB_ID  
from PS_TZ_SMSTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b 
where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=? AND a.TZ_KEY_ID = ? limit 0,1