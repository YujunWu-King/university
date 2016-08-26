Ext.define('KitchenSink.view.siteManage.outsiteManage.tempStore', {
	extend : 'Ext.data.Store',
	alias : 'store.tempStore',
	model : 'KitchenSink.view.siteManage.outsiteManage.tempModel',
	comID : 'TZ_GD_WWZDGL_COM',
	pageID : 'TZ_GD_WWMBGL_STD',
	autoLoad : true,
	pageSize : 5,
	proxy : Ext.tzListProxy()
});
