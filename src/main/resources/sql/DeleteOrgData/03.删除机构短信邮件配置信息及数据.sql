/*邮件服务器定义表*/
delete from PS_TZ_EMLS_DEF_TBL where TZ_JG_ID in ('DYTMBA');

/*元模板定义表*/
delete from PS_TZ_TMP_DEFN_TBL where TZ_JG_ID in ('DYTMBA');

/*模板参数表*/
delete from PS_TZ_TMP_PARA_TBL where TZ_JG_ID in ('DYTMBA');

/*动态数据采集记录关键字规则表*/
delete from PS_TZ_TMP_RRKF_TBL where TZ_JG_ID in ('DYTMBA');

/*短信模板定义表*/
delete from PS_TZ_SMSTMPL_TBL where TZ_JG_ID in ('DYTMBA');

/*邮件模板定义表*/
delete from PS_TZ_EMALTMPL_TBL where TZ_JG_ID in ('DYTMBA');

/*听众成员表*/
delete from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID in (select TZ_AUDIENCE_ID from PS_TZ_AUDIENCE_T where TZ_JG_ID in ('DYTMBA'));

/*听众表*/
delete from PS_TZ_AUDIENCE_T where TZ_JG_ID in ('DYTMBA');

/*邮件模板实例表*/
delete from PS_TZ_YJMBSHLI_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*短信模板实例表*/
delete from PS_TZ_DXMBSHLI_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*任务执行实例表*/
delete from PS_TZ_RWZXSHIL_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*短信邮件发送任务参数实例表*/
delete from PS_TZ_DXYJCSHU_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*任务通用附件表*/
delete from PS_TZ_RW_FJIAN_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*任务个性化附件表*/
/*
delete from PS_TZ_AUDFJIAN_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));
*/

/*短信邮件任务明细表*/
delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*邮件发送历史正文表*/
delete from PS_TZ_YJZWLSHI_TBL where TZ_RWSL_ID in (
	select TZ_RWSL_ID from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (
		select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA')
	)
);

/*邮件发送历史附件表*/
delete from PS_TZ_YJFJLSHI_TBL where TZ_RWSL_ID in (
	select TZ_RWSL_ID from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (
		select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA')
	)
);

/*短信发送历史正文表*/
delete from PS_TZ_DXZWLSHI_TBL where TZ_RWSL_ID in (
	select TZ_RWSL_ID from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (
		select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA')
	)
);

/*短信发送错误日志表*/
/*
delete from PS_TZ_DXFSRIZH_TBL where TZ_RWSL_ID in (
	select TZ_RWSL_ID from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (
		select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA')
	)
);
*/

/*邮件发送历史表*/
delete from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*短信发送历史表*/
delete from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*邮件发送错误日志表*/
delete from PS_TZ_YJFSRIZH_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA'));

/*短信邮件发送任务表*/
delete from PS_TZ_DXYJFSRW_TBL where TZ_JG_ID in ('DYTMBA');