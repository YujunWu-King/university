Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resTempletParaInfoStore',
    model: 'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletParaInfoModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_RES_TMPL_MG_COM',
    pageID: 'TZ_RES_TMPL_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
