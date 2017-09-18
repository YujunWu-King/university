Ext.define('KitchenSink.view.callCenter.callActListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.callActListStore',
    model: 'KitchenSink.view.callCenter.callActListModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_CALLCR_USER_COM',
	pageID: 'TZ_CALLC_ACT_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_ACT_STD.TZ_CCALL_HD_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
