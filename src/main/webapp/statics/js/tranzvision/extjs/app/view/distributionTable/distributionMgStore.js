Ext.define('KitchenSink.view.distributionTable.distributionMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.distributionMgStore',
    model: 'KitchenSink.view.distributionTable.distributionMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_DISTRI_TAB_COM',
	pageID: 'TZ_DISTRI_GL_STD ',
	tzStoreParams: '{"cfgSrhId":"TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.TZ_FBDZ_TBL","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
