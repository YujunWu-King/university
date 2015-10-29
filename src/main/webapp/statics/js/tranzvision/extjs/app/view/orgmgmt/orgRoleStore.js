Ext.define('KitchenSink.view.orgmgmt.orgRoleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.orgRoleStore',
    model: 'KitchenSink.view.orgmgmt.orgRoleModel',
	autoLoad: false,
    pageSize: 10,
    comID: 'TZ_GD_ORGGL_COM',
    pageID: 'TZ_GD_ORGDEF_STD',
    tzStoreParams: '{"orgId":"","queryType":"ROLE}',
    proxy: Ext.tzListProxy()
});
