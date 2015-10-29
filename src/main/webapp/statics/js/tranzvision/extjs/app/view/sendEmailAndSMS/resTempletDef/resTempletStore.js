Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resTempletStore',
    model: 'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_RES_TMPL_MG_COM',
    pageID: 'TZ_RES_TMPL_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_MG_STD.TZ_TMP_DEFN_VW"}',
	proxy: Ext.tzListProxy()		
});
