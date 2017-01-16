Ext.define('KitchenSink.view.ZNX.znxTempletDef.znxTempletItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxTempletItemStore',
    model: 'KitchenSink.view.ZNX.znxTempletDef.znxTempletItemModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_ZNX_TMPL_MG_COM',
    pageID: 'TZ_ZNX_TMPL_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
