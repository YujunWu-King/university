Ext.define('KitchenSink.view.orgmgmt.orgListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.orgListStore',
    model: 'KitchenSink.view.orgmgmt.orgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_ORGGL_COM',
	pageID: 'TZ_GD_ORGGL_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_GD_ORGGL_COM.TZ_GD_ORGGL_STD.TZ_GD_JG_VW"}',
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
