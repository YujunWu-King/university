Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsHistoryPanelStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryPanelModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_SMSQ_VIEWTY_COM',
    pageID: 'TZ_SMSQ_VIEWTY_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
