Ext.define('KitchenSink.view.classManage.DjzlStore', {
    extend: 'Ext.data.Store',
    alias: 'store.DjzlStore',
    model: 'KitchenSink.view.classManage.DjzlModel',
    comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJJB_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});