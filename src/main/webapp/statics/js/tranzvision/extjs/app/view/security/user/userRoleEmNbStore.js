Ext.define('KitchenSink.view.security.user.userRoleEmNbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userRoleEmNbStore',
    model: 'KitchenSink.view.security.user.userRoleModel',
  //  pageSize: 10,
	//autoLoad: true,
	comID: 'TZ_EM_NB_YHZHGL_COM',
	pageID: 'TZ_EMNB_YHZHXX_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
