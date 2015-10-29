Ext.define('KitchenSink.view.orgmgmt.orgInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.orgInfoStore',
    model: 'KitchenSink.view.orgmgmt.orgModel',
	autoLoad: true,
	pageSize: 1,
	comID: 'TZ_GD_ORGGL_COM',
	pageID: 'TZ_GD_ORGGL_STD',
	tzStoreParams: '{}',
	/*proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/orgmgmt/orgs.json',
		reader: {
			type: 'json',
			rootProperty: 'root'
		}
	}*/
	proxy: Ext.tzListProxy()
});
