Ext.define('KitchenSink.view.siteManage.siteManage.websiteStore', {//站点管理列表获取数据js
    extend: 'Ext.data.Store',
    alias: 'store.websiteMGStore',
    model: 'KitchenSink.view.siteManage.siteManage.siteModel',//json格式
	comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ZDGL_STD',
	tzStoreParams: '',
	autoLoad: true,
	pageSize: 10,
	proxy: Ext.tzListProxy()	
});