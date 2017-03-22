Ext.define('KitchenSink.view.recommend.recommendInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.recommendInfoStore',
    model: 'KitchenSink.view.recommend.recommendInfoModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_TJR_MANAGER_COM',
		pageID: 'TZ_TJR_DETAIL_STD',
		tzStoreParams: '{"cfgSrhId":"TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.TZ_GD_TJRINF_VW","condition":{}}',
		proxy: Ext.tzListProxy()
	
});
