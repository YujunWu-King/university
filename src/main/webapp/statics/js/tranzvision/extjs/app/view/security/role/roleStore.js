Ext.define('KitchenSink.view.security.role.roleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.roleStore',
    model: 'KitchenSink.view.security.role.roleModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_AQ_ROLE_COM',
    pageID: 'TZ_ROLE_LIST_STD',
    tzStoreParams: '{cfgSrhId: "TZ_AQ_ROLE_COM.TZ_ROLE_LIST_STD.PSROLEDEFN_VW"}',
    proxy: Ext.tzListProxy()
});
