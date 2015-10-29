Ext.define('KitchenSink.view.siteManage.siteManage.area.typeManges.areaTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.areaMTypeStore',
    model: 'KitchenSink.view.siteManage.siteManage.area.typeManges.areaTypeModel',//json格式
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_ZD_QYLXSZ_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()			
});
