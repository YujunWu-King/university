Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkDetEmlItemStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlItemModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
