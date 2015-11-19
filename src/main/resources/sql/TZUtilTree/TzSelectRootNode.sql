select 
	TREE_NODE 
from 
	PSTREENODE 
where 
	ltrim(rtrim(SETID))='' 
	and TREE_NAME=? 
	and (
		ltrim(rtrim(PARENT_NODE_NAME))='' 
		or PARENT_NODE_NAME is null
	)