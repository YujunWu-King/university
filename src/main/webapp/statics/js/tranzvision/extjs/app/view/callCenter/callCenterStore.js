Ext.define('KitchenSink.view.callCenter.callCenterStore', {
    extend: 'Ext.data.Store',
    alias: 'store.callCenterStore',
    model: 'KitchenSink.view.callCenter.callCenterModel',
	autoLoad: false,
	pageSize: 13,
	comID: 'TZ_CALLCR_USER_COM',
	pageID: 'TZ_CALLC_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
