SELECT 'X' CAN_START_FLAG FROM TZ_JC_FWQDX_T
WHERE TZ_JG_ID=?
  AND TZ_JCFWQ_MC=?
  AND TZ_FWQ_IP=?
  AND (TZ_YXZT='STARTING' OR (    TZ_ZJXTSJ<=SUBTIME(NOW(),'00:30:00')
                              AND TZ_YXZT IN('STOPPING','RUNNING')
                             )
      )