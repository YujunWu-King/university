Ext.define('KitchenSink.view.security.com.pageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.pageStore',
    model: 'KitchenSink.view.security.com.pageModel',
	comID: 'TZ_AQ_COMREG_COM',
	pageID: 'TZ_AQ_COMREG_STD',
	tzStoreParams:  '{"comID":""}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
