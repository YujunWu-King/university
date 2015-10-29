Ext.define('KitchenSink.view.orgmgmt.adminUserStore', {
    extend: 'Ext.data.Store',
    alias: 'store.adminUserStore',
    model: 'KitchenSink.view.orgmgmt.adminUserModel',
    pageSize: 5,
	comID: 'TZ_GD_ORGGL_COM',
	pageID: 'TZ_GD_ORGDEF_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/orgmgmt/users.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	*/
});
