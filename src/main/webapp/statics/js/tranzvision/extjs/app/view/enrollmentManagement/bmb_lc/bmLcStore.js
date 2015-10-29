Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmLcStore', {//报名审核--列表获取数据js
    extend: 'Ext.data.Store',
    alias: 'store.bmLcStore',
    model: 'KitchenSink.view.enrollmentManagement.bmb_lc.bmLcModel',//json格式
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMSH_LCFB_STD',
	//tzStoreParams: '{"bj_id":"2"}',
	tzStoreParams: '{}',
	autoLoad: true,
	pageSize: 10,
	proxy: Ext.tzListProxy()	
});