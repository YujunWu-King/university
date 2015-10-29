Ext.define('KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeAccStore',
    model: 'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccModel',
	autoLoad: true,
	pageSize: 10,
	
	comID: 'TZ_GSM_JUDGE_ACC_COM',
	pageID: 'TZ_GSM_JUD_GL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GSM_JUDGE_ACC_COM.TZ_GSM_JUD_GL_STD.TZ_PWZHGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
