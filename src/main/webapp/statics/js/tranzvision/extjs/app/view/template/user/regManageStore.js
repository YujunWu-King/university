Ext.define('KitchenSink.view.template.user.regManageStore', {
	extend: 'Ext.data.Store',
	alias: 'store.regManageStore',
	model: 'KitchenSink.view.template.user.regManageModel',
	comID: 'TZ_USER_REG_COM',
	pageID: 'TZ_REGGL_STD',
	pageSize: 100,
	autoLoad: false,
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});