Ext.define('KitchenSink.view.sendEmailAndSMS.paraMg.paraStore', {
    extend: 'Ext.data.Store',
    alias: 'store.paraStore',
    model: 'KitchenSink.view.sendEmailAndSMS.paraMg.paraModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PARA_MG_COM',
    pageID: 'TZ_PARA_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PARA_MG_COM.TZ_PARA_MG_STD.TZ_EX_PARA_VW"}',
	proxy: Ext.tzListProxy()
});
