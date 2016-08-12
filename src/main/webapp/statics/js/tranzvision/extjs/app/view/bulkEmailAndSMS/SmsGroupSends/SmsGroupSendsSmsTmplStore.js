Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsTmplStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsGroupSendsSmsTmplStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsTmplModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_SMSQ_COM',
    pageID: 'TZ_SMSQ_DET_STD',
    tzStoreParams: '{"queryID": "smstmpl"}',
    proxy: Ext.tzListProxy()
});
