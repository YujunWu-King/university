Ext.define('KitchenSink.view.security.user.userRoleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userRoleStore',
    model: 'KitchenSink.view.security.user.userRoleModel',
  //  pageSize: 10,
	//autoLoad: true,
	comID: 'TZ_AQ_YHZHGL_COM',
	pageID: 'TZ_AQ_YHZHXX_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
