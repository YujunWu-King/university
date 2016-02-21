select 
	A.OPRID,
	B.TZ_DLZH_ID,
	B.TZ_REALNAME,
	B.TZ_MOBILE,
	B.TZ_EMAIL
from
	PS_TZ_CLS_ADMIN_T A
left join
	PS_TZ_AQ_YHXX_TBL B
on
	A.OPRID = B.OPRID
where
	A.TZ_CLASS_ID=?
order by OPRID asc
limit ?,?