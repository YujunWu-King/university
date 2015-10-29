Ext.define('KitchenSink.view.template.kjgl.auditKjYtStore', {
    extend: 'Ext.data.Store',
    alias: 'store.auditKjYtStore',
    model: 'KitchenSink.view.template.kjgl.auditKjYtModel',
    pageSize:10,
    comID: 'TZ_KJGL_COM',
    pageID: 'TZ_KJGL_INFO_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
