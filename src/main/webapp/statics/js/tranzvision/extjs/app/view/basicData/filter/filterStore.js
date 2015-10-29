Ext.define('KitchenSink.view.basicData.filter.filterStore', {
    extend: 'Ext.data.Store',
    alias: 'store.filterStore',
    model: 'KitchenSink.view.basicData.filter.filterModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_FILTER_COM',
	pageID: 'TZ_GD_FILTERGL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_FILTER_COM.TZ_GD_FILTERGL_STD.TZ_FILTERGL_VW","condition":{}}',
	proxy: Ext.tzListProxy()
});
