select 
	A.TZ_ATTRIBUTE_ID,
	A.TZ_ATTRIBUTE_NAME,
	A.TZ_CONTROL_TYPE,
	B.TZ_ATTRIBUTE_VALUE
from 
	PS_TZ_CLS_ATTR_T A
left join ( 
		select * from PS_TZ_CLS_MORINF_T where TZ_CLASS_ID = ?
    ) B
on 
	A.TZ_ATTRIBUTE_ID=B.TZ_ATTRIBUTE_ID
where 
	A.TZ_JG_ID=? 
	and A.TZ_IS_USED='Y'
order by A.TZ_SORT_NUM asc