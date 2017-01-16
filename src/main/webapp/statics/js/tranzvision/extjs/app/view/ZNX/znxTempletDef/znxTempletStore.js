Ext.define('KitchenSink.view.ZNX.znxTempletDef.znxTempletStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxTempletStore',
    model: 'KitchenSink.view.ZNX.znxTempletDef.znxTempletModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_ZNX_TMPL_MG_COM',
    pageID: 'TZ_ZNX_TMPL_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_ZNX_TMPL_MG_COM.TZ_ZNX_TMPL_MG_STD.TZ_ZNXTMPL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()		
});
