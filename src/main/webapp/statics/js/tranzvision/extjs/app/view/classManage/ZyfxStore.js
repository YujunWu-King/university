Ext.define('KitchenSink.view.classManage.ZyfxStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ZyfxStore',
    model: 'KitchenSink.view.classManage.ZyfxModel',
    comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJJB_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});