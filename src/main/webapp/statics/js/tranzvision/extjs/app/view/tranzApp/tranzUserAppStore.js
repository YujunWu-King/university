Ext.define('KitchenSink.view.tranzApp.tranzUserAppStore', {
    extend: 'Ext.data.Store',
    alias: 'store.tranzUserAppStore',
    model: 'KitchenSink.view.tranzApp.tranzUserAppModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_GD_TRANZAPP_COM',
	pageID: 'TZ_GD_TRANZAPP_STD',
    tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
