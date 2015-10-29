Ext.define('KitchenSink.view.classManage.GlryStore', {
    extend: 'Ext.data.Store',
    alias: 'store.GlryStore',
    model: 'KitchenSink.view.classManage.GlryModel',
    comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJJB_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});