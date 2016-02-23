select 
	A.TZ_XXX_TRANS_ID,
	A.TZ_PX_XH,
	A.TZ_XXX_TRANS_NAME,
	B.TZ_OPT_VALUE
from 
	PS_TZ_XXX_TRANS_T A
left join (
		select * from PS_TZ_XXX_TR_EN_T where LANGUAGE_CD='ENG' 
	) B
on (
		A.TZ_XXX_TRANS_ID=B.TZ_XXX_TRANS_ID
		and A.TZ_ART_ID=B.TZ_ART_ID
	)
where 
	A.TZ_ART_ID=? 
	and A.TZ_ZXBM_XXX_ID=?
order by A.TZ_PX_XH