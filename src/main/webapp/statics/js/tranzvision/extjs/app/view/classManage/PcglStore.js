Ext.define('KitchenSink.view.classManage.PcglStore', {
    extend: 'Ext.data.Store',
    alias: 'store.PcglStore',
    model: 'KitchenSink.view.classManage.PcglModel',
    comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJJB_STD',
	tzStoreParams: '',
    pageSize: 100,
    proxy: Ext.tzListProxy()
});