UPDATE TZ_JC_FWQDX_T 
SET 
    TZ_YXZT = ?,
    TZ_ZJXTSJ = NOW(),
    TZ_BS_SLID = ?,
    TZ_CZXT_LX = ?
WHERE
    TZ_JG_ID = ? AND TZ_JCFWQ_MC = ?
        AND TZ_FWQ_IP = ?
        AND (TZ_YXZT = 'STARTING'
        OR (TZ_ZJXTSJ <= SUBTIME(NOW(), '00:30:00')
        AND TZ_YXZT IN ('STOPPING' , 'RUNNING')))