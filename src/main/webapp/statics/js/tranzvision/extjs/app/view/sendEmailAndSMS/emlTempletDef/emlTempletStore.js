Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emlTempletStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_EML_TMPL_MG_COM',
    pageID: 'TZ_EML_TMPL_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_MG_STD.TZ_EMALTMPL_VW","condition":{"TZ_JG_ID_1-operator": "01","TZ_JG_ID_1-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()		
});
