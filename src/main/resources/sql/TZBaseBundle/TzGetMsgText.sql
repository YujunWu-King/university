select 
	TMP.TZ_MSG_TEXT 
from ( 
 		select 
 			A.TZ_XXJH_ID, 
 			if(B.TZ_MSG_ID is null,A.TZ_MSG_ID,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_ID,B.TZ_MSG_ID)) TZ_MSG_ID, 
 			if(B.TZ_MSG_ID is null,A.TZ_MSG_TEXT,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_TEXT,B.TZ_MSG_TEXT)) TZ_MSG_TEXT
 		from 
 			PS_TZ_PT_XXDY_TBL A
 		left join
 			PS_TZ_PT_XXDY_TBL B
 		on
 			(
 				A.TZ_XXJH_ID=B.TZ_XXJH_ID
 				and upper(A.TZ_JG_ID)=upper(B.TZ_JG_ID)
 				and A.TZ_MSG_ID=B.TZ_MSG_ID
 				and upper(?)=upper(B.TZ_LANGUAGE_ID)
 			)
 		where  
   			upper(A.TZ_LANGUAGE_ID)=( 
 				select 
 					upper(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD 
  				from 
  					PS_TZ_HARDCD_PNT 
 				where 
 					TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' 
 			) 
   			and A.TZ_XXJH_ID=? 
   			and upper(A.TZ_JG_ID)=upper(?) 
   	) TMP 
where 
	TMP.TZ_MSG_ID=?