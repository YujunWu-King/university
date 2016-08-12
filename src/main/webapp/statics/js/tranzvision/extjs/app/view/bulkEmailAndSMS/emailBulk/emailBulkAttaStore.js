Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkAttaStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
