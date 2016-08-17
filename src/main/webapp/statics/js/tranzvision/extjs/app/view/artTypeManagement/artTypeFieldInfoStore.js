Ext.define('KitchenSink.view.artTypeManagement.artTypeFieldInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artTypeFieldInfoStore',
    model: 'KitchenSink.view.artTypeManagement.artTypeFieldInfoModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_ART_TYPE_MG_COM',
    pageID: 'TZ_ARTTYPE_INF_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
