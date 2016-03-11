Ext.define('KitchenSink.view.security.user.userRoleNbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userRoleNbStore',
    model: 'KitchenSink.view.security.user.userRoleModel',
  //  pageSize: 10,
	//autoLoad: true,
	comID: 'TZ_AQ_NB_YHZHGL_COM',
	pageID: 'TZ_NB_YHZHXX_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
