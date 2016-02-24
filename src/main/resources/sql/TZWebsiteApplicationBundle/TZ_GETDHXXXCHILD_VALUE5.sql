 SELECT TMP.TZ_IS_CHECKED 
   FROM (SELECT A.TZ_APP_INS_ID 
	 , A.TZ_APP_TPL_ID 
	 , A.TZ_D_XXX_BH 
	 , A.TZ_IS_CHECKED ,
     if(@ptzappinsid = A.TZ_APP_INS_ID 
     	AND @ptzapptplid = A.TZ_APP_TPL_ID 
     	AND @ptzdxxxbh = A.TZ_D_XXX_BH 
     	AND @ptappvalue = A.TZ_APP_S_TEXT 
     	AND @ptxxxlxzmc = A.TZ_XXXKXZ_MC,
     	@rank:=@rank+1,@rank:=1) AS rank,
      @ptzappinsid := A.TZ_APP_INS_ID ptzappinsid,
      @ptzapptplid := A.TZ_APP_TPL_ID ptzapptplid,
      @ptzdxxxbh := A.TZ_D_XXX_BH ptzdxxxbh,
      @ptappvalue := A.TZ_APP_S_TEXT ptappvalue,
      @ptxxxlxzmc := A.TZ_XXXKXZ_MC
     FROM PS_TZ_APP_DHCC_VW2 A,
     (select @ptzappinsid := null ,
     		 @ptzapptplid := null,
     		 @ptzdxxxbh := null,
     		 @ptappvalue :=null,
     		 @ptxxxlxzmc :=null,
     		 @rank:=0) c 
     WHERE TZ_APP_INS_ID = ?
   AND TZ_D_XXX_BH = ?
   AND TZ_XXX_BH LIKE ?
   AND TZ_XXX_NO = ?
   AND TZ_XXXKXZ_MC = ?
   ) TMP where rank = ?