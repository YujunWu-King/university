Ext.define('KitchenSink.view.classManage.DjzlDxNrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.DjzlDxNrStore',
    model: 'KitchenSink.view.classManage.DjzlDxNrModel',
	pageSize: 10,
	comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_BJ_ZLDX_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});