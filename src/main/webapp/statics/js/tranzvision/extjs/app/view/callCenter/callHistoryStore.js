Ext.define('KitchenSink.view.callCenter.callHistoryStore', {
    extend: 'Ext.data.Store',
    alias: 'store.callHistoryStore',
    model: 'KitchenSink.view.callCenter.callCenterModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_CALLCR_USER_COM',
	pageID: 'TZ_CALLC_LIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW"}',
	proxy: Ext.tzListProxy()
});
