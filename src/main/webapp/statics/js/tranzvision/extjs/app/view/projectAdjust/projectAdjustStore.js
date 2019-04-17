Ext.define('KitchenSink.view.projectAdjust.projectAdjustStore', {//班级管理列表获取数据js
    extend: 'Ext.data.Store',
    alias: 'store.classStore',
    model: 'KitchenSink.view.projectAdjust.projectAdjustModel',
	comID: 'TZ_PROADJUST_COM',
	pageID: 'TZ_PROADJUST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_PROADJUST_COM.TZ_PROADJUST_STD.ps_tz_proadjust_t","condition":{}}',
	autoLoad: true,
	pageSize:10 ,
	proxy: Ext.tzListProxy()
});