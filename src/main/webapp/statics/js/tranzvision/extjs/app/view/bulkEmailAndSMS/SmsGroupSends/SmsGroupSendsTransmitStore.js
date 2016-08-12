Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsTransmitStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsGroupSendsTransmitStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsTransmitModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_SMSQ_COM',
    pageID: 'TZ_SMSQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
