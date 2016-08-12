Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetReceiverStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkDetReceiverStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetReceiverModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
