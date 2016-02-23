select 
	A.TZ_ART_ID,
	A.TZ_ZXBM_XXX_ID,
	A.TZ_PX_XH,
	A.TZ_ZXBM_XXX_NAME,
	A.TZ_ZXBM_XXX_BT,
	A.TZ_ZXBM_XXX_ZSXS,
	B.TZ_ZXBM_XXX_NAME as EN_NAME
from 
	PS_TZ_ZXBM_XXX_T A
left join (
		select * from PS_TZ_ZXBM_XXX_E_T where LANGUAGE_CD='ENG' 
	) B
    
on (
		A.TZ_ZXBM_XXX_ID=B.TZ_ZXBM_XXX_ID
        and A.TZ_ART_ID=B.TZ_ART_ID
	)
where 
	A.TZ_ART_ID=?
order by A.TZ_PX_XH