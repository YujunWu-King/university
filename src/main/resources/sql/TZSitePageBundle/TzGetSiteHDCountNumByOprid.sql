 select 
 	count(1) 
 from 
 	PS_TZ_SITEI_COLU_T PT1, 
 	PS_TZ_LM_NR_GL_T PT2,  
 	PS_TZ_ART_REC_TBL PT3,  
 	PS_TZ_ART_HD_TBL PT4 
 where     
 	PT1.TZ_SITEI_ID = PT2.TZ_SITE_ID  
 	and PT1.TZ_COLU_ID = PT2.TZ_COLU_ID 
 	and PT2.TZ_ART_ID = PT3.TZ_ART_ID 
 	and PT3.TZ_ART_ID = PT4.TZ_ART_ID  
 	and PT2.TZ_ART_PUB_STATE = 'Y' 
 	and PT1.TZ_SITEI_ID = ? 
 	and PT1.TZ_COLU_ID =? 
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