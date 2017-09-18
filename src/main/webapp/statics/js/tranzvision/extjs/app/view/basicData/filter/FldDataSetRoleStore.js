Ext.define('KitchenSink.view.basicData.filter.FldDataSetRoleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.FldDataSetRoleStore',
    model: 'KitchenSink.view.basicData.filter.FldDataSetRoleModel',
    autoLoad: true,
    pageSize: 15,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FLDDST_FLD_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});