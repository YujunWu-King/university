  SELECT A.TZ_XXX_BH, ATTACHUSERFILE, ATTACHSYSFILENAME,TZ_ACCESS_PATH
    FROM PS_TZ_FORM_ATT_T A, PS_TZ_FORM_ATT2_T C
   WHERE     A.TZ_APP_INS_ID = ?
         AND A.TZ_XXX_BH NOT IN (SELECT TEMP.TZ_XXX_BH
                                   FROM    PS_TZ_TEMP_FIELD_T TEMP
                                        LEFT JOIN
                                           PS_TZ_APP_XXXPZ_T APP
                                        ON     TEMP.TZ_APP_TPL_ID =
                                                  APP.TZ_APP_TPL_ID
                                           AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH
                                  WHERE APP.TZ_APP_TPL_ID = ?)
         AND A.TZ_XXX_BH = C.TZ_XXX_BH
         AND C.TZ_APP_INS_ID = ?
ORDER BY A.TZ_XXX_BH