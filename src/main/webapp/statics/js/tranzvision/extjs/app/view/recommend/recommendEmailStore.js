Ext.define('KitchenSink.view.recommend.recommendEmailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.recommendEmailStore',
    model: 'KitchenSink.view.recommend.recommendEmailModel',
		autoLoad: false,
		pageSize: 10,
		comID: 'TZ_TJR_MANAGER_COM',
		pageID: 'TZ_TJR_EMAIL_STD',
		tzStoreParams: '{}',
		proxy: Ext.tzListProxy()
	
});