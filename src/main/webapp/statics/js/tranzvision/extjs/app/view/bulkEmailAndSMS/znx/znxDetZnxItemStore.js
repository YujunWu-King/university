Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxDetZnxItemStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxItemModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZNX_GL_COM',
    pageID: 'TZ_ZNX_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
