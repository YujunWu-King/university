Ext.define('KitchenSink.view.security.role.rolePlstStore', {
    extend: 'Ext.data.Store',
    alias: 'store.rolePlstStore',
    model: 'KitchenSink.view.security.role.rolePlstModel',
    pageSize: 10,
    comID: 'TZ_AQ_ROLE_COM',
    pageID: 'TZ_ROLE_INFO_STD',
    tzStoreParams: '{"roleName":""}',
    proxy: Ext.tzListProxy()
});
