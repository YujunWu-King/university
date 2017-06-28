Ext.define('KitchenSink.view.callCenter.userAppListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userAppListStore',
    model: 'KitchenSink.view.callCenter.userAppListModel',
    comID: 'TZ_CALLCR_USER_COM',
	pageID: 'TZ_CALLC_USER_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});