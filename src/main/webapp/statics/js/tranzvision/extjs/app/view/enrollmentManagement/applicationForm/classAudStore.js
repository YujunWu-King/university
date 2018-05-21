Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classAudStore', {
    extend: 'Ext.data.Store',
    alias: 'store.classAudStore',
    model: 'KitchenSink.view.audienceManagement.newAudWindowModel',
	autoLoad: false,
	pageSize: 5,
	comID: 'TZ_AUD_COM',
	pageID: 'TZ_AUD_NEW_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{}}',
	proxy: Ext.tzListProxy()
});
