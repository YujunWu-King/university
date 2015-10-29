Ext.define('KitchenSink.view.materialsReview.judgeAccount.judgeAccStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeAccStore',
    model: 'KitchenSink.view.materialsReview.judgeAccount.judgeAccModel',
	autoLoad: true,
	pageSize: 10,
	
	comID: 'TZ_JUDGE_ACC_COM',
	pageID: 'TZ_JUDACC_GL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_JUDGE_ACC_COM.TZ_JUDACC_GL_STD.TZ_PWZHGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
