select TZ_AUDCY_ID 
from PS_TZ_AUDCYUAN_T 
where TZ_AUDIENCE_ID=?
order by cast(TZ_AUDCY_ID as UNSIGNED INTEGER) limit ?,1