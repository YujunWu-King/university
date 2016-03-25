/*功能菜单树节点定义表*/
delete from PS_TZ_AQ_CDJD_TBL where TREE_NAME='TZ_GD_GNCD_MENU' and TZ_MENU_NUM in (select TREE_NODE from PSTREENODE where TREE_NODE='DYTMBA' or TREE_NODE like 'DYTMBA_%');

/*树节点（PS表）*/
/*
select A.* from PSTREENODE A,(
		select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE where TREE_NAME='TZ_GD_GNCD_MENU' and TREE_NODE='DYTMBA'
    ) B 
where 
	TREE_NAME='TZ_GD_GNCD_MENU'
    and A.TREE_NODE_NUM>=B.TREE_NODE_NUM
    and A.TREE_NODE_NUM_END<=B.TREE_NODE_NUM_END;
    
select * from PSTREENODE where TREE_NODE='DYTMBA' or TREE_NODE like 'DYTMBA_%';
*/
delete from PSTREENODE where TREE_NODE='DYTMBA' or TREE_NODE like 'DYTMBA_%';

/*功能菜单树定义表*/
/*
select * from PS_TZ_AQ_CDSDY_TBL;
*/

/*组件授权信息*/
delete from PS_TZ_AQ_COMSQ_TBL where CLASSID in (select CLASSID from PSCLASSDEFN where CLASSID like 'DYTMBA_%');

/*许可权定义表*/
delete from PSCLASSDEFN where CLASSID like 'DYTMBA_%';

/*用户角色表（PS表）*/
delete from PSROLEUSER where ROLENAME in (select ROLENAME from PSROLEDEFN where ROLENAME like 'DYTMBA_%');

/*角色定义表（PS表）*/
delete from PSROLEDEFN where ROLENAME like 'DYTMBA_%';
