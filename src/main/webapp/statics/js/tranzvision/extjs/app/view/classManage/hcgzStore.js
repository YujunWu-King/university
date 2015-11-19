Ext.define('KitchenSink.view.classManage.hcgzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.hcgzStore',
    model: 'KitchenSink.view.classManage.hcgzModel',
    comID: 'TZ_GD_BJGL_COM',
    pageID: 'TZ_GD_BJJB_STD',
    tzStoreParams: '',
    pageSize: 0,
    proxy: Ext.tzListProxy()
});