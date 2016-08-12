Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetCCStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkDetCCStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetCCModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{"queryID": "CC"}',
    proxy: Ext.tzListProxy()
});
