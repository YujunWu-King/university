select 
	PT2.TZ_COLU_ID,
	PT3.TZ_ART_ID,
	PT4.TZ_NACT_NAME,  
	PT4.TZ_START_DT ,
	PT4.TZ_HD_CS,
	PT4.TZ_QY_ZXBM ,
	PT4.TZ_APPF_DT,
	concat(PT4.TZ_APPF_DT,' ', PT4.TZ_APPF_TM) as TZ_APPF_TM,
	PT4.TZ_APPE_DT,
	concat(PT4.TZ_APPE_DT,' ', PT4.TZ_APPE_TM) as TZ_APPE_TM  
from 
	PS_TZ_LM_NR_GL_T PT2, 
	PS_TZ_ART_REC_TBL PT3, 
	PS_TZ_ART_HD_TBL PT4  
where     
	PT2.TZ_ART_ID = PT3.TZ_ART_ID 
	and PT3.TZ_ART_ID = PT4.TZ_ART_ID  
	and PT2.TZ_ART_PUB_STATE = 'Y' 
	and PT2.TZ_SITE_ID = ? 
	and exists (
 		select 
 			'Y' 
 		from 
 			PS_TZ_NAUDLIST_T A 
 		left join
 			(
 				SELECT 
 					TZ_LYDX_ID, TZ_ZY_SJ 
 				from 
 					PS_TZ_LXFSINFO_TBL 
 				where 
 					TZ_LXFS_LY = 'HDBM'
 			) B 
 		on
 			(
 				A.TZ_HD_BMR_ID = B.TZ_LYDX_ID 
 			)
 		where  
 			A.TZ_ART_ID = PT2.TZ_ART_ID 
 			and (A.TZ_NREG_STAT='1' or A.TZ_NREG_STAT='4') 
 			and A.OPRID = ?
 	)  
order by 
	PT2.TZ_MAX_ZD_SEQ desc, 
	PT4.TZ_START_DT asc 
limit ?,?