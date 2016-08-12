Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkDetSenderStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{"queryID": "sender"}',
    proxy: Ext.tzListProxy()
});
