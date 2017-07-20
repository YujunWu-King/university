SELECT TZ_JG_ID,TZ_JCFWQ_MC,TZ_YXZT FROM TZ_JC_FWQDX_T
WHERE TZ_FWQ_IP=?
  AND (TZ_YXZT='STARTING' OR (    TZ_ZJXTSJ<=SUBTIME(NOW(),'00:30:00')
                              AND TZ_YXZT IN('STOPPING','RUNNING')
                             )
      )