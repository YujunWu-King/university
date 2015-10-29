Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletStore', {
    extend: 'Ext.data.Store',
    alias: 'store.smsTempletStore',
    model: 'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_SMS_TMPL_MG_COM',
    pageID: 'TZ_SMS_TMPL_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.TZ_SMSTMPL_VW","condition":{"TZ_JG_ID_1-operator": "01","TZ_JG_ID_1-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()		
});
