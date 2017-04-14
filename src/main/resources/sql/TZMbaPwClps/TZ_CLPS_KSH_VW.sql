SELECT 
    A.TZ_CLASS_ID,
    A.TZ_APPLY_PC_ID,
    B.OPRID,
    B.TZ_REALNAME,
    B.TZ_MSH_ID,
    A.TZ_APP_INS_ID,
    CASE
        WHEN B.TZ_GENDER = 'F' THEN '女'
        WHEN B.TZ_GENDER = 'M' THEN '男'
        WHEN B.TZ_GENDER = 'N' THEN '未知'
        ELSE ''
    END AS TZ_GENDER,
    CASE
        WHEN A.TZ_MSHI_ZGFLG = 'Y' THEN '有'
        WHEN A.TZ_MSHI_ZGFLG = 'N' THEN '无'
        ELSE ''
    END AS TZ_MSHI_ZGFLG
FROM
    PS_TZ_CLPS_KSH_TBL A,
    (SELECT 
        W.TZ_APP_INS_ID,
            W.OPRID,
            U.TZ_REALNAME,
            Y.TZ_MSH_ID,
            U.TZ_GENDER
    FROM
        PS_TZ_FORM_WRK_T W, PS_TZ_REG_USER_T U, PS_TZ_AQ_YHXX_TBL Y
    WHERE
        W.OPRID = U.OPRID AND W.OPRID = Y.OPRID) B
WHERE
    A.TZ_APP_INS_ID = B.TZ_APP_INS_ID