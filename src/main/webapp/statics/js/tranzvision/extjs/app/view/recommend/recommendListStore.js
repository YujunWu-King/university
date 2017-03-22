Ext.define('KitchenSink.view.recommend.recommendListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.recommendListStore',
    model: 'KitchenSink.view.recommend.recommendListModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_TJR_MANAGER_COM',
		pageID: 'TZ_TJR_INFO_STD',
		tzStoreParams: '{"cfgSrhId":"TZ_TJR_MANAGER_COM.TZ_TJR_INFO_STD.TZ_GD_TJRCFG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
		proxy: Ext.tzListProxy()
	
});
