Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsReceiverStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsGroupSendsReceiverStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsReceiverModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_SMSQ_COM',
    pageID: 'TZ_SMSQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
