//专业方向store
Ext.define('KitchenSink.view.classManage.userStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userStore',
    model: 'KitchenSink.view.classManage.userModel',
	autoLoad: true,
	pageSize: 0,
	comID: 'TZ_GD_BJGL_COM',
	pageID: 'TZ_BJ_USER_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
