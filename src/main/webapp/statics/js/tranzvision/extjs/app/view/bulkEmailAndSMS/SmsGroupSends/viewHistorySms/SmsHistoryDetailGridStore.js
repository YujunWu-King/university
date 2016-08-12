Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryDetailGridStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsHistoryDetailGridStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsHistoryDetailGridModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_SMSQ_VIEWTY_COM',
    pageID: 'TZ_SMSQ_VWTY_D_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
