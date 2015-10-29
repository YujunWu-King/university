Ext.define('KitchenSink.view.template.sitetemplate.websiteStore', {
    extend: 'Ext.data.Store',
    alias: 'store.websiteStore',
    model: 'KitchenSink.view.template.sitetemplate.siteModel',//json格式
	comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_MBGL_STD',
	tzStoreParams: '',
	autoLoad: true,
	pageSize: 10,
	proxy: Ext.tzListProxy()	
});