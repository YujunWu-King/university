Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jugClGroupStore',
    model: 'KitchenSink.view.judgesManagement.judgesGroupMg.jugGroupMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_CLPS_GROP_COM',
	pageID: 'TZ_CLJUP_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_CLPS_GROP_COM.TZ_CLJUP_LIST_STD.TZ_CLPS_GR_TBL","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
