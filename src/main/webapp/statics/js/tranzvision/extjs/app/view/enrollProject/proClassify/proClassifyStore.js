Ext.define('KitchenSink.view.enrollProject.proClassify.proClassifyStore', {
    extend: 'Ext.data.Store',
    alias: 'store.proClassifyStore',
    model: 'KitchenSink.view.enrollProject.proClassify.proClassifyModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_ZS_XMLBSZ_COM',
	pageID: 'TZ_ZS_XMLBSZ_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.TZ_PRJ_TYPE_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
