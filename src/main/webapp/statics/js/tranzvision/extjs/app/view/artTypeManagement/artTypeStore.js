Ext.define('KitchenSink.view.artTypeManagement.artTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artTypeStore',
    model: 'KitchenSink.view.artTypeManagement.artTypeModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_ART_TYPE_MG_COM',
    pageID: 'TZ_ART_TYPE_MG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_ART_TYPE_MG_COM.TZ_ART_TYPE_MG_STD.TZ_ART_TYPE_VW"}',
	proxy: Ext.tzListProxy()		
});
