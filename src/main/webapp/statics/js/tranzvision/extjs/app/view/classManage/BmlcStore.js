Ext.define('KitchenSink.view.classManage.BmlcStore', {
    extend: 'Ext.data.Store',
    alias: 'store.BmlcStore',
    model: 'KitchenSink.view.classManage.BmlcModel',
    comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJJB_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});