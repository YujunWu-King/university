Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxDetReceiverStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxDetReceiverStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.znxDetReceiverModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_ZNX_GL_COM',
    pageID: 'TZ_ZNX_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
