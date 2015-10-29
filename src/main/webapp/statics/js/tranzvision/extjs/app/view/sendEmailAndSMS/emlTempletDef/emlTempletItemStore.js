Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emlTempletItemStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletItemModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_EML_TMPL_MG_COM',
    pageID: 'TZ_EML_TMPL_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
