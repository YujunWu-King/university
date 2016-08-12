Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsGroupSendsSmsItemStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsItemModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_SMSQ_COM',
    pageID: 'TZ_SMSQ_DET_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
