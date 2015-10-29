Ext.define('KitchenSink.view.template.kjgl.auditKjJygzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.auditKjJygzStore',
    model: 'KitchenSink.view.template.kjgl.auditKjJygzModel',
    pageSize:10,
    comID: 'TZ_KJGL_COM',
    pageID: 'TZ_KJGL_INFO_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});
