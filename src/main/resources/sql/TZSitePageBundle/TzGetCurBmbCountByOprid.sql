 SELECT 
 	count(1)
 from  PS_TZ_CLASS_INF_T A,PS_TZ_FORM_WRK_T B
  where A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.OPRID=?
  AND A.TZ_IS_APP_OPEN='Y' and 
  A.TZ_APP_START_DT IS NOT NULL AND 
  A.TZ_APP_START_TM IS NOT NULL AND 
  A.TZ_APP_END_DT IS NOT NULL AND 
  A.TZ_APP_END_TM IS NOT NULL AND 
  str_to_date(concat(DATE_FORMAT(A.TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() 
  AND str_to_date(concat(DATE_FORMAT(A.TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()