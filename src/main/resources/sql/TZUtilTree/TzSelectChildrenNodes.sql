select TREE_NODE 
  from PSTREENODE 
 where SETID=? 
   and TREE_NAME=? 
   and PARENT_NODE_NAME=? 
  order by TREE_NODE_NUM asc