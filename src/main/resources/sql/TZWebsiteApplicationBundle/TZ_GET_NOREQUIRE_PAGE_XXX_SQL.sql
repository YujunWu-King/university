SELECT TZ_XXX_BH 
 , TZ_XXX_MC 
 , TZ_COM_LMC 
  FROM PS_TZ_APP_XXXPZ_T
 WHERE TZ_APP_TPL_ID = ? 
   AND TZ_PAGE_NO = ?
  ORDER BY TZ_XXX_SLID