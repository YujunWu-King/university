Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailHistoryPanelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailHistoryPanelStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailHistoryPanelModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_EMLQ_VIEWTY_COM',
    pageID: 'TZ_EMLQ_VIEWTY_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
