/*递交资料模板详情表*/
delete from PS_TZ_SBMINF_STP_T where TZ_SBMINF_TMP_ID in (select TZ_SBMINF_TMP_ID from PS_TZ_SBMINF_TMP_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*递交资料模板常用回复短语表*/
delete from PS_TZ_SBMINF_REP_T where TZ_SBMINF_TMP_ID in (select TZ_SBMINF_TMP_ID from PS_TZ_SBMINF_TMP_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*递交资料模型*/
delete from PS_TZ_SBMINF_TMP_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*项目分类表*/
delete from PS_TZ_PRJ_TYPE_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*项目与专业方向关系表*/
delete from PS_TZ_PRJ_MAJOR_T where TZ_PRJ_ID in (select TZ_PRJ_ID from PS_TZ_PRJ_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*项目管理人员关系表*/
delete from PS_TZ_PRJ_ADMIN_T where TZ_PRJ_ID in (select TZ_PRJ_ID from PS_TZ_PRJ_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*项目详情表*/
delete from PS_TZ_PRJ_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*班级更多信息表*/
delete from PS_TZ_CLS_MORINF_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级批次列表*/
delete from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级与专业方向关系表*/
delete from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级管理人员关系表*/
delete from PS_TZ_CLS_ADMIN_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级报名流程步骤实例表*/
delete from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级报名流程步骤回复短语表*/
delete from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级递交资料详情表*/
delete from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级递交资料常用回复短语表*/
delete from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID in (select TZ_CLASS_ID from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*班级详情表*/
delete from PS_TZ_CLASS_INF_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*班级个性化属性定义表*/
delete from PS_TZ_CLS_ATTR_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*班级个性化属性下拉值定义*/
delete from PS_TZ_C_ATTR_OPT_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');