Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jugMsGroupStore',
    model: 'KitchenSink.view.judgesManagement.judgesGroupMg.jugGroupMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_MSPS_GROP_COM',
	pageID: 'TZ_MSPS_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_MSPS_GROP_COM.TZ_MSPS_LIST_STD.TZ_MSPS_GR_TBL","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
