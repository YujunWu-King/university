select 
	ROLENAME,
	TZ_ROLE_TYPE,
	(
		select 
			X.TZ_ZHZ_CMS 
		from 
			PS_TZ_TRANS_ITEM_VW X 
		where 
			X.TZ_ZHZJH_ID='TZ_ROLE_TYPE' 
			AND X.TZ_ZHZ_ID = TZ_ROLE_TYPE
	) TZ_ROLE_TYPE_DESC,
	(
		select 
			DESCR 
		from 
			PSROLEDEFN 
		where 
			ROLENAME=A.ROLENAME
	) ROLE_DESC  
from 
	PS_TZ_JG_ROLE_T A  
where TZ_JG_ID=? 
order by ROLENAME 
limit ?,?