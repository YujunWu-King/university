 SELECT TMP.TZ_APP_S_TEXT 
 ,TMP.TZ_APP_L_TEXT
   FROM (SELECT A.TZ_APP_INS_ID 
	 , A.TZ_APP_TPL_ID 
	 , A.TZ_D_XXX_BH 
	 , A.TZ_APP_S_TEXT 
	 , A.TZ_APP_L_TEXT,
     if(@ptzappinsid = A.TZ_APP_INS_ID 
     	AND @ptzapptplid = A.TZ_APP_TPL_ID 
     	AND @ptzdxxxbh = A.TZ_D_XXX_BH,
     	@rank:=@rank+1,@rank:=1) AS rank,
      @ptzappinsid := A.TZ_APP_INS_ID ptzappinsid,
      @ptzapptplid := A.TZ_APP_TPL_ID ptzapptplid,
      @ptzdxxxbh := A.TZ_D_XXX_BH ptzdxxxbh
     FROM PS_TZ_APP_CC_VW2 A,
     (select 
     	@ptzappinsid := null ,
     	@ptzapptplid := null,
     	@ptzdxxxbh := null ,@rank:=0) c 
     WHERE TZ_APP_INS_ID = ?
   		AND TZ_D_XXX_BH = ?
   		AND TZ_XXX_BH LIKE ?
   		AND TZ_XXX_NO = ?
   		AND TZ_COM_LMC = ?
   	 ) TMP where rank = ?