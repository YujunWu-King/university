Ext.define('KitchenSink.view.orgmgmt.userStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userStore',
    model: 'KitchenSink.view.orgmgmt.userModel',
	autoLoad: true,
	pageSize: 5,
	comID: 'TZ_GD_ORGGL_COM',
	pageID: 'TZ_GD_ORGMEM_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
