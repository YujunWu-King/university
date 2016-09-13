Ext.define('KitchenSink.view.siteManage.outsiteManage.menuStore', {
	extend : 'Ext.data.Store',
	alias : 'store.menuStore',
	model : 'KitchenSink.view.siteManage.outsiteManage.menuModel',
	comID : 'TZ_GD_WWZDGL_COM',
	pageID : 'TZ_GD_WWCDGL_STD',
	autoLoad : false,
	pageSize : 5,
	proxy : Ext.tzListProxy()
});
