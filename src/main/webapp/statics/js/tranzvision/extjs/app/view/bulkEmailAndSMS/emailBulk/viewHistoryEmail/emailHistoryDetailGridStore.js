Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailHistoryDetailGridStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailHistoryDetailGridStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailHistoryDetailGridModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_EMLQ_VIEWTY_COM',
    pageID: 'TZ_EMLQ_VWTY_D_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
