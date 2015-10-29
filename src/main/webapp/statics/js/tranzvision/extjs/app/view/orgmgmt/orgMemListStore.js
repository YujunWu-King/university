Ext.define('KitchenSink.view.orgmgmt.orgMemListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.orgMemListStore',
    model: 'KitchenSink.view.orgmgmt.userModel',
	pageSize: 10,
	comID: 'TZ_GD_ORGGL_COM',
	pageID: 'TZ_GD_ORGDEF_STD',
	tzStoreParams: '{"queryType":"USER","cfgSrhId":"TZ_GD_ORGGL_COM.TZ_GD_ORGDEF_STD.TZ_JG_USER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.orgId+'"}}',
	//tzStoreParams: '{"orgId":"","queryType":"USER"}',
	proxy: Ext.tzListProxy()
});
