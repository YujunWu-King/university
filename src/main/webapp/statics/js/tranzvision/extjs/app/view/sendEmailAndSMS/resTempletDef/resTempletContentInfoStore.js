Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resTempletContentInfoStore',
    model: 'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletContentInfoModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_RES_TMPL_MG_COM',
    pageID: 'TZ_RES_TMPL_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
