Ext.define('KitchenSink.view.judgesManagement.judgeAccount.judgeCLAccStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeCLAccStore',
    model: 'KitchenSink.view.judgesManagement.judgeAccount.judgeAccModel',
	autoLoad: true,
	pageSize: 10,
	
	comID: 'TZ_JUDGE_CLACC_COM',
	pageID: 'TZ_JUDCLACC_GL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_JUDGE_CLACC_COM.TZ_JUDCLACC_GL_STD.TZ_PWZHGLCL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
