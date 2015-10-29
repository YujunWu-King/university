Ext.define('KitchenSink.view.basicData.filter.FldStore', {
    extend: 'Ext.data.Store',
    alias: 'store.FldStore',
    model: 'KitchenSink.view.basicData.filter.FldModel',
    autoLoad: true,
    pageSize: 8,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_GD_FLDTZ_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});