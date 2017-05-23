UPDATE PS_TZ_CS_KS_TBL A 
SET 
    TZ_KSH_CSJG = 'Y'
WHERE
    TZ_CLASS_ID = ?
        AND TZ_APPLY_PC_ID = ?
        AND NOT EXISTS( SELECT 
            'X'
        FROM
            PS_TZ_CS_KSFM_T B,
            PS_TZ_BIAOQZ_BQ_T C
        WHERE
            B.TZ_CLASS_ID = A.TZ_CLASS_ID
                AND B.TZ_APPLY_PC_ID = A.TZ_APPLY_PC_ID
                AND B.TZ_APP_INS_ID = A.TZ_APP_INS_ID
                AND B.TZ_FMQD_ID = C.TZ_BIAOQ_ID
                AND TZ_OUT_FLG = 'Y')