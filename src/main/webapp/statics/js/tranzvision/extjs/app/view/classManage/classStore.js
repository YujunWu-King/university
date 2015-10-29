Ext.define('KitchenSink.view.classManage.classStore', {//班级管理列表获取数据js
    extend: 'Ext.data.Store',
    alias: 'store.classStore',
    model: 'KitchenSink.view.classManage.classModel',
	comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_GD_BJCX_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.TZ_GD_BJGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	autoLoad: true,
	pageSize:30 ,
	proxy: Ext.tzListProxy()
});