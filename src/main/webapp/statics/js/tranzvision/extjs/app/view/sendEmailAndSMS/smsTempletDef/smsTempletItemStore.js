Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.smsTempletItemStore',
    model: 'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletItemModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_SMS_TMPL_MG_COM',
    pageID: 'TZ_SMS_TMPL_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
