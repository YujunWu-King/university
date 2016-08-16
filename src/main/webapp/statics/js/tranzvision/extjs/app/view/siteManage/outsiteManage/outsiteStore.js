Ext.define('KitchenSink.view.siteManage.outsiteManage.outsiteStore', {// 站点管理列表获取数据js
	extend : 'Ext.data.Store',
	alias : 'store.outsiteMGStore',
	model : 'KitchenSink.view.siteManage.outsiteManage.outsiteModel',// json格式
	comID : 'TZ_GD_WWZDGL_COM',
	pageID : 'TZ_GD_WWZDGL_STD',
	tzStoreParams : '',
	autoLoad : true,
	pageSize : 10,
	proxy : Ext.tzListProxy()
});