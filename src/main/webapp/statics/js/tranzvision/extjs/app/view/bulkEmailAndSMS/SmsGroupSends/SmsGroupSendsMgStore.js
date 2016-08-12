Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.SmsGroupSendsMgStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_SMSQ_COM',
    pageID: 'TZ_SMSQ_MGR_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.TZ_SMSQ_LIST_V"}',
    proxy: Ext.tzListProxy()
});
