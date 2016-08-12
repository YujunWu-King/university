Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlTmplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailBulkDetEmlTmplStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlTmplModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_EMLQ_COM',
    pageID: 'TZ_EMLQ_DET_STD',
    tzStoreParams: '{"queryID": "emltmpl"}',
    proxy: Ext.tzListProxy()
});
